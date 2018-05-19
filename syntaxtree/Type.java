package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

// Data type
public abstract class Type {
  public abstract void accept(Visitor v);

  public abstract Type accept(TypeVisitor v);

  public abstract String toString();

  public abstract Type instance();
}
