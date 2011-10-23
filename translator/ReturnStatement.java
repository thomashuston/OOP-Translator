/**
 * Expression?
 */
package translator;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class ReturnStatement extends Statement implements Translatable {   

  private Expression expression;
  
  public ReturnStatement(GNode n) {
    expression = null;
    visit(n);
  }
  
  public void visitExpression(GNode n) {
    expression = new Expression(n);
  }
  
  public void visitPrimaryIdentifier(GNode n) {
    expression = new PrimaryIdentifier(n);
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    if (expression != null)
      return getIndent(indent) + "return " + expression.getCC(indent, className, variables) + ";\n";
    else
      return getIndent(indent) + "return;\n";
  }
  
}