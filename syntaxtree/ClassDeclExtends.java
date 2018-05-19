package syntaxtree;

import visitor.TypeVisitor;
import visitor.Visitor;

/*
   class i extends j {
     (Variable declarations)*
     (Method declaration)*
   }
*/
public class ClassDeclExtends extends ClassDecl {
  public IdentifierType i;
  public IdentifierType j;
  public VarDeclList vl;      // Sequence of variable declarations
  public MethodDeclList ml;   // Sequence of method declarations

  public ClassDeclExtends(IdentifierType ai, IdentifierType aj,
      VarDeclList avl, MethodDeclList aml) {
    i = ai;
    j = aj;
    vl = avl;
    ml = aml;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
