package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

// Data type
public abstract class Type {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
