/**
 * Expression Statement
 */
package translator;

import java.util.ArrayList;
import java.util.List;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class WhileStatement extends Statement implements Translatable {
  
  private Expression expression;
  private Statement statement;

  public WhileStatement(GNode n) {
    visit(n);
  }

  public void visitExpression(GNode n) {
    expression = new Expression(n);
  }

  public void visitEqualityExpression(GNode n) {
    expression = new EqualityExpression(n);
  }

  public void visitPostfixExpression(GNode n) {
    expression = new PostfixExpression(n);
  }

  public void visitRelationalExpression(GNode n) {
    expression = new RelationalExpression(n);
  }

  public void visitAssertStatement(GNode n) {
    statement = new AssertStatement(n);
  }

  public void visitBreakStatement(GNode n) {
    statement = new BreakStatement(n);
  }
  
  public void visitConditionalStatement(GNode n) {
    statement = new ConditionalStatement(n);
  }
  
  public void visitContinueStatement(GNode n) {
    statement = new ContinueStatement(n);
  }
  
  public void visitDoWhileStatement(GNode n) {
    statement = new DoWhileStatement(n);
  }
  
  public void visitExpressionStatement(GNode n) {
    statement = new ExpressionStatement(n);
  }
  
  public void visitForStatement(GNode n) {
    statement = new ForStatement(n);
  }

  public void visitPrimaryIdentifier(GNode n) {
    expression = new PrimaryIdentifier(n);
  }
  
  public void visitReturnStatement(GNode n) {
    statement = new ReturnStatement(n);
  }
  
  public void visitSwitchStatement(GNode n) {
    statement = new SwitchStatement(n);
  }
  
  public void visitSynchronizedStatement(GNode n) {
    statement = new SynchronizedStatement(n);    
  }

  public void visitThrowStatement(GNode n) {
    statement = new ThrowStatement(n);
  }
  
  public void visitTryCatchFinallyStatement(GNode n) {
    statement = new TryCatchFinallyStatement(n);
  }
  
  public void visitWhileStatement(GNode n) {
    statement = new WhileStatement(n);
  }
  
  public String getCC(int indent, String className, List<Variable> variables) {
    StringBuilder s = new StringBuilder();
    s.append(getIndent(indent) + "while (" + expression.getCC(indent, className, variables) + ") {\n");
    s.append(getIndent(indent) + statement.getCC(indent, className, variables) + getIndent(indent) + "}");
    s.append("\n");
    return s.toString();
  }
  
}
