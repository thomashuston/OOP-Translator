/**
 * ("static":Word)? Block
 */
package pcp.translator;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class BlockDeclaration extends Declaration {

  private boolean isStatic;
  private Block block;
  
  public BlockDeclaration(GNode n) {
    isStatic = false;
    visit(n);
  }
  
  public void visitBlock(GNode n) {
    block = new Block(n);
  }
  
  public void visitWord(GNode n) {
    isStatic = true;
  }

}
