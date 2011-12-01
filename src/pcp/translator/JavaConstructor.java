/*
 * pcp - The Producer of C++ Programs
 * Copyright (C) 2011 Nabil Hassein, Thomas Huston, Mike Morreale, Marta Wilgan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pcp.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Printer;
import xtc.tree.Visitor;

/**
 * A constructor.
 *
 * @author Nabil Hassein
 * @author Thomas Huston
 * @author Mike Morreale
 * @author Marta Wilgan
 *
 * @version 1.3
 */
public class JavaConstructor extends Visitor implements Translatable {

  private JavaBlock body;
  private JavaClass cls;
  //private ThrowsClause exception;
  private Map<String, JavaExpression> initialize;
  private boolean isAbstract, isFinal;
  private String name;
  private LinkedHashMap<String, JavaType> parameters;
  private JavaStatement superCall, thisCall;
  private JavaVisibility visibility;


  // =========================== Constructors =======================
  
  /**
   * Creates the constructor.
   *
   * @param n The constructor declaration node.
   */
  public JavaConstructor(GNode n, JavaClass cls) {
    // Set the class
    this.cls = cls;

    // Initialize the maps
    initialize = new HashMap<String, JavaExpression>();
    parameters = new LinkedHashMap<String, JavaType>();
    
    // Set the default visibility
    visibility = JavaVisibility.PACKAGE_PRIVATE;

    // Get the name
    name = n.getString(2);

    // Dispatch over the child nodes
    for (Object o : n) {
      if (o instanceof Node) {
        dispatch((Node)o);
      }
    }

    // Create the body of the constructor
    body = new JavaBlock(n.getGeneric(5), cls, this);

    // Check if the first line of the constructor is a call to this
    if (0 < n.getNode(5).size() && n.getNode(5).getNode(0).hasName("ExpressionStatement") &&
        n.getNode(5).getNode(0).getNode(0).hasName("CallExpression"))
      if (n.getNode(5).getNode(0).getNode(0).getString(2).equals("this"))
        thisCall = new JavaStatement(n.getNode(5).getGeneric(0), body);
      else if (n.getNode(5).getNode(0).getNode(0).getString(2).equals("super"))
        superCall = new JavaStatement(n.getNode(5).getGeneric(0), body);
  }


  // ============================ Get Methods =======================

  /**
   * Gets the class constructor is for.
   *
   * @return The class.
   */
  public JavaClass getClassFrom() {
    return cls;
  }

  /**
   * Gets the mangled constructor name.
   *
   * @return The mangled name.
   */
  public String getMangledName() {
    StringBuilder mangled = new StringBuilder();
    mangled.append("$__" + name);
    Set<String> params = parameters.keySet();
    for (String param : params) {
      mangled.append("$" + parameters.get(param).getMangledType());
    }
    if (0 == params.size())
      mangled.append("$void");
    return mangled.toString();
  }

  /**
   * Gets the parameters of the constructor.
   *
   * @return The parameters.
   */
  public LinkedHashMap<String, JavaType> getParameters() {
    return parameters;
  }

  /**
   * Gets the visibility of the constructor.
   *
   * @return The visibility.
   */
  public JavaVisibility getVisibility() {
    return visibility;
  }

  /**
   * Checks if the specified variable is static.
   *
   * @return <code>True</code> if the variable is static;
   * <code>false</code> otherwise.
   */
  public boolean isVariableStatic(String name) {
    return cls.isVariableStatic(name);
  }


  // ============================ Set Methods =======================
  
  /**
   * Adds a variable to initialize in the constructor.
   *
   * @param name The name of the variable.
   * @param value The value of the variable.
   */
  public void addInitializer(String name, JavaExpression value) {
    initialize.put(name, value);
  }


  // =========================== Visit Methods ======================

  /**
   * Parses the body of the constructor.
   *
   * @param n The block node.
   */
  public void visitBlock(GNode n) {
    // Already initialized
  }

  /**
   * Parses the dimensions of the constructor.
   *
   * @param n The dimensions node.
   */
  public void visitDimensions(GNode n) {
    // TODO: What are dimensions in a method declaration?  
  }
     
  /**
   * Parses the parameters of the constructor.
   *
   * @param n The formal parameters node.
   */
  public void visitFormalParameters(GNode n) {
    for (Object o : n) {
      Node param = (Node)o;
      int j;
      if (param.getNode(1).hasName("Type")) 
        j = 1;
      else
        j = 2;
      JavaType paramType = new JavaType(param.getGeneric(j));
      if (null != param.getNode(j).get(1))
        paramType.setDimensions(param.getNode(j).getNode(1).size());
      parameters.put("$" + param.getString(j + 2), paramType);
    }
  }

  /**
   * Parses the modifiers of the constructor.
   *
   * @param n The modifiers node.
   */
  public void visitModifiers(GNode n) {
    for (Object o : n) {
      String m = ((GNode)o).getString(0);
      if (m.equals("public"))
        visibility = JavaVisibility.PUBLIC;
      else if (m.equals("private"))
        visibility = JavaVisibility.PRIVATE;
      else if (m.equals("protected"))
        visibility = JavaVisibility.PROTECTED;
    }
  }

  /**
   * Parses the exceptions thrown by the constructor.
   *
   * @param n The throws clause node.
   */
  public void visitThrowsClause(GNode n) {
    // TODO: handle exceptions
    //exception = new ThrowsClause(n);
  }


  // r======================== Translation Methods ===================

  /**
   * Translates the constructor into a declaration for
   * the C++ header struct and writes it to the
   * output stream.
   *
   * @param out The output stream.
   *
   * @return The output stream.
   */
  public Printer translateHeaderDeclaration(Printer out) {
    out.indent().p("__").p(cls.getName()).p("(");
    Set<String> params = parameters.keySet();
    int count = 0;
    for (String param : params) {
      if (parameters.get(param).isArray())
        out.p("__rt::Ptr<");
      parameters.get(param).translate(out);
      if (parameters.get(param).isArray())
        out.p(" >");
      out.p(" ").p(param);
      if (count < params.size() - 1)
        out.p(", ");
      count++;
    }
    return out.pln(");");
  }

  /**
   * Translates the constructor helper method into a
   * declaration for the C++ header struct and writes
   * it to the output stream.
   *
   * @param out The output stream.
   *
   * @return The output stream.
   */
  public Printer translateHelperDeclaration(Printer out) {
    out.indent().p("static ").p(cls.getName()).p(" $__");
    out.p(cls.getName());
    Set<String> params = parameters.keySet();
    for (String param : params) {
      out.p("$").p(parameters.get(param).getMangledType());
    }
    if (0 == params.size())
      out.p("$void");
    out.p("(");
    int count = 0;
    for (String param : params) {
      if (parameters.get(param).isArray())
        out.p("__rt::Ptr<");
      parameters.get(param).translate(out);
      if (parameters.get(param).isArray())
        out.p(" >");
      out.p(" ").p(param);
      if (count < params.size() - 1)
        out.p(", ");
      count++;
    }
    return out.pln(");");
  }

  /**
   * Translates the constructor into C++ and writes
   * it to the output stream.
   *
   * @param out The output stream.
   *
   * @return The output stream.
   */
  public Printer translate(Printer out) {
    // Create the mangled name based on the parameters
    Set<String> params = parameters.keySet();
    out.indent().p(name).p(" __").p(name).p("::$__").p(name);
    for (String param : params) {
      out.p("$").p(parameters.get(param).getMangledType());
    }
    if (0 == params.size())
      out.p("$void");
    out.p("(");
    int count = 0;
    for (String param : params) {
      if (parameters.get(param).isArray())
        out.p("__rt::Ptr<");
      parameters.get(param).translate(out);
      if (parameters.get(param).isArray())
        out.p(" >");
      out.p(" ").p(param);
      if (count < params.size() - 1)
        out.p(", ");
      count++;
    }
    out.pln(") {").incr();

    // Call the C++ constructor
    out.indent().p(name).p(" $con$ = ");
    if (null != thisCall)
      thisCall.translate(out);
    else
      out.p("new __").p(name).pln("();");

    if (null != superCall) {
      out.indent().p("$con$->__super = ");
      superCall.translate(out);
    } else if (null == thisCall) {
      out.indent().p("$con$->__super = ");
      JavaClass sup = cls.getParent();
      if (null == sup) {
        out.pln("__Object::$__Object$void();");
      } else {
        if (!sup.getFile().getPackage().getNamespace().equals(""))
          out.p(sup.getFile().getPackage().getNamespace()).p("::");
        out.p("__").p(sup.getName()).p("::$__").p(sup.getName()).pln("$void();");
      }
    }

    if (null == thisCall) {
      // Initialize any class instance variables
      for (JavaField f : cls.getFields()) {
        if (!f.isStatic()) {
          f.translateConstructor(out);
        }
      }
    }

    // Translate the body of the constructor
    body.translate(out);

    // Return the created instance
    out.indent().pln("return $con$;");
    return out.decr().indent().pln("}");
  }

}
