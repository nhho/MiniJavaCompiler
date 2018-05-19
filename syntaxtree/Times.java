package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

public class Times extends Exp {
  public Exp e1;
  public Exp e2;

  public Times(Exp ae1, Exp ae2) {
    e1 = ae1;
    e2 = ae2;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
