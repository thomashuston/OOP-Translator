/**
 * (RelationalExpression Type)/
 * yyValue:RelationalExpression
 */
package translator;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class InstanceOfExpression extends Expression implements Translatable {
  
  private RelationalExpression relationalExpression;
  private Type type;

  public InstanceOfExpression(GNode n) {
    visit(n);
  }

  public void visitRelationalExpression(GNode n) {
    relationalExpression = new RelationalExpression(n);
  }

  public void visitType(GNode n) {
    type = new Type(n);
  }
  
  public String getCC(String className, int indent) {
    return "";
  }
  
}
