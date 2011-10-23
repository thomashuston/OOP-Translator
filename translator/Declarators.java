/**
 * Declarator Declarator*
 */
package translator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class Declarators extends TranslationVisitor implements Iterable<Declarator>, Translatable {

  private List<Declarator> declarators;
  
  public Declarators(GNode n) {
    declarators = new ArrayList<Declarator>();
    visit(n);
  }
  
  public Declarator get(int index) {
    return declarators.get(index);
  }
  
  public Iterator<Declarator> iterator() {
    Iterator<Declarator> it = declarators.iterator();
    return it;
  }
  
  public int size() {
    return declarators.size();
  }
  
  public void visitDeclarator(GNode n) {
    if (n != null)
      declarators.add(new Declarator(n));
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    StringBuilder s = new StringBuilder(declarators.get(0).getCC(indent, className, variables));
    return s.toString();
  }
}
