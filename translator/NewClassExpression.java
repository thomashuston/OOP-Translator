/**
 * null null (PrimitiveType/QualifiedIdentifier) Arguments ClassBody?
 */
package translator;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class NewClassExpression extends Expression implements Translatable {

  private Arguments arguments;
  private ClassBody classBody;
  private PrimitiveType primitive;
  private QualifiedIdentifier qualified;
  
  public NewClassExpression(GNode n) {
    classBody = null;
    primitive = null;
    qualified = null;
    visit(n);
  }
  
  public void visitArguments(GNode n) {
    arguments = new Arguments(n);
  }
  
  public void visitClassBody(GNode n) {
    classBody = new ClassBody(n);
  }
  
  public void visitPrimitiveType(GNode n) {
    primitive = new PrimitiveType(n);
  }
  
  public void visitQualifiedIdentifier(GNode n) {
    qualified = new QualifiedIdentifier(n);
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    return "";
  }

}