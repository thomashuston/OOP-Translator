/**
 * null Block (CatchClause* Block)/(CatchClause+ null)
 */
package pcp.translator;

import java.util.List;
import java.util.ArrayList;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class TryCatchFinallyStatement extends Statement implements Translatable {
  
  private Block tryBlock;
  private Block finallyBlock;
  private List<CatchClause> catchClause;

  public TryCatchFinallyStatement(GNode n) {
    tryBlock = null;
    finallyBlock = null;
    catchClause = new ArrayList<CatchClause>();
    visit(n);
  }

  public void visitBlock(GNode n) {
    Block block = new Block(n);
    if (tryBlock == null) {
      tryBlock = block;
    } else {
      finallyBlock = block;
    }
  }

  public void visitCatchClause(GNode n) {
    catchClause.add(new CatchClause(n));
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    return "";
  }
  
}