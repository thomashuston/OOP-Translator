/**
 * Expression Block
 */
package pcp.translator;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class SynchronizedStatement extends Statement implements Translatable {

  private Expression expression;
  private Block block;

  public SynchronizedStatement(GNode n) {
    visit(n);
  }

  public void visitExpression(GNode n) {
    expression = new Expression(n);
  }

  public void visitBlock(GNode n) {
    block = new Block(n);
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    return "";
  }
  
}
