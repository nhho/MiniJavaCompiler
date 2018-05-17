package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class InstanceType extends Type {
  public String s;

  public InstanceType(String as) { 
    s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String toString() {
    return s;
  }

  public InstanceType instance() {
    return new InstanceType(s);
  }
}
