package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

public class MainClass {
  public IdentifierType i1;
  public Identifier i2;
  public VarDeclList vl;
  public Statement s;

  public MainClass(IdentifierType ai1, Identifier ai2, VarDeclList avl, Statement as) {
    i1 = ai1;
    i2 = ai2;
    s = as;
    vl = avl;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}

