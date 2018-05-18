package visitor;

import java.util.Vector;
import syntaxtree.*;

public class TypeCheckExpVisitor extends TypeDepthFirstVisitor {
  void p1(String op, String type) {
    System.out.println("lhs of " + op + " must be of type " + type);
  }

  void p2(String op, String type) {
    System.out.println("rhs of " + op + " must be of type " + type);
  }

  // Exp e1, e2;
  public Type visit(And n) {
    Type t1 = n.e1.accept(this);
    if (!(t1 instanceof BooleanType)) {
      p1("And", "boolean");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(t2 instanceof BooleanType)) {
      p2("And", "boolean");
      System.exit(-1);
    }
    return new BooleanType();
  }

  // Exp e1, e2;
  public Type visit(LessThan n) {
    Type t1 = n.e1.accept(this);
    if (!(t1 instanceof IntegerType)) {
      p1("LessThan", "int");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(t2 instanceof IntegerType)) {
      p2("LessThan", "int");
      System.exit(-1);
    }
    return new BooleanType();
  }

  // Exp e1, e2;
  public Type visit(Plus n) {
    Type t1 = n.e1.accept(this);
    if (!(t1 instanceof IntegerType)) {
      p1("Plus", "int");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(t2 instanceof IntegerType)) {
      p2("Plus", "int");
      System.exit(-1);
    }
    return new IntegerType();
  }

  // Exp e1, e2;
  public Type visit(Minus n) {
    Type t1 = n.e1.accept(this);
    if (!(n.e1.accept(this) instanceof IntegerType)) {
      p1("Minus", "int");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(n.e2.accept(this) instanceof IntegerType)) {
      p2("Minus", "int");
      System.exit(-1);
    }
    return new IntegerType();
  }

  // Exp e1,e2;
  public Type visit(Times n) {
    Type t1 = n.e1.accept(this);
    if (!(t1 instanceof IntegerType)) {
      p1("Times", "int");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(t2 instanceof IntegerType)) {
      p2("Times", "int");
      System.exit(-1);
    }
    return new IntegerType();
  }

  // Exp e1, e2;
  public Type visit(ArrayLookup n) {
    Type t1 = n.e1.accept(this);
    if (!(t1 instanceof IntArrayType)) {
      p1("ArrayLookup", "int []");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(this);
    if (!(t2 instanceof IntegerType)) {
      p2("ArrayLookup", "int");
      System.exit(-1);
    }
    return new IntegerType();
  }

  // Exp e;
  public Type visit(ArrayLength n) {
    Type t1 = n.e.accept(this);
    if (!(t1 instanceof IntArrayType)) {
      p1("ArrayLength", "int []");
      System.exit(-1);
    }
    return new IntegerType();
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    String mname = n.i.toString();
    Type t1 = n.e.accept(this);
    if (!(t1 instanceof InstanceType)) {
      System.out.println("method " + mname
          + " called  on something that is not a instance of Object");
      System.exit(-1);
    }

    Vector<Type> vt = new Vector<Type>();
    for (int i = 0; i < n.el.size(); i++) {
      Type t2 = n.el.elementAt(i).accept(this);
      vt.addElement(t2);
    }

    String cname = t1.toString();
    Class tmp = TypeCheckVisitor.symbolTable.getClass(cname);
    Method m = tmp.getMethod2(mname, vt);
    return m.type.instance();
  }

  // int i;
  public Type visit(IntegerLiteral n) {
    return new IntegerType();
  }

  public Type visit(True n) {
    return new BooleanType();
  }

  public Type visit(False n) {
    return new BooleanType();
  }

  // String s;
  public Type visit(IdentifierExp n) {
    // return TypeCheckVisitor.symbolTable.getVarType(TypeCheckVisitor.currMethod,
    // TypeCheckVisitor.currClass, n.s);
    return TypeCheckVisitor.symbolTable.getVar(TypeCheckVisitor.currMethod,
        TypeCheckVisitor.currClass, n.s).type.instance();
  }

  public Type visit(This n) {
    return new InstanceType(TypeCheckVisitor.currClass.type().toString());
  }

  // Exp e;
  public Type visit(NewArray n) {
    Type t1 = n.e.accept(this);
    if (!(t1 instanceof IntegerType)) {
      p1("new int []", "int");
      System.exit(-1);
    }
    return new IntArrayType();
  }

  // IdentifierType i;
  public Type visit(NewObject n) {
    return new InstanceType(n.i.s);
  }

  // Exp e;
  public Type visit(Not n) {
    Type t1 = n.e.accept(this);
    if (!(t1 instanceof BooleanType)) {
      p2("Not", "boolean");
      System.exit(-1);
    }
    return new BooleanType();
  }

} //TypeCheckVisitor
