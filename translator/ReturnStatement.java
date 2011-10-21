/**
 * Expression?
 */

package translator;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ReturnStatement extends TranslationVisitor { 
    
    private Expression expression;
    
    public ReturnStatement(GNode n)
    {
        visit(n);
    }
    
    public void visitExpression(Gnode n)
    {
        expression = new Expression(n);
    }
    

}