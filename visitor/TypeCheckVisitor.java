package visitor;

import syntaxtree.*;
import visitor.SymbolTable;

public class TypeCheckVisitor extends DepthFirstVisitor {

  static Class currClass;
  static Method currMethod;
  static SymbolTable symbolTable;

  public TypeCheckVisitor(SymbolTable s) {
    symbolTable = s;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.elementAt(i).accept(this);
    }
  }

  // IdentifierType i1;
  // Identifier i2;
  // Statement s;
  public void visit(MainClass n) {
    String i1 = n.i1.toString();
    currClass = symbolTable.getClass(i1);

    currMethod = currClass.getMethod("main");

    // n.i2.accept(this);

    /* for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */

    n.s.accept(this);
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    String id = n.i.toString();
    currClass = symbolTable.getClass(id);
    /* for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }
  }

  // IdentifierType i;
  // IdentifierType j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    String id = n.i.toString();
    currClass = symbolTable.getClass(id);
    // n.j.accept(this);
    /* for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }
  }

  // Type t;
  // Identifier i;
  /* public void visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
  } */

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    // n.t.accept(this);
    String id = n.i.toString();
    // currMethod = currClass.getMethodById(n.idRef);
    currMethod = currClass.getMethod(id);
    Type retType = currMethod.type();
    /* for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
    Type t1 = n.e.accept(new TypeCheckExpVisitor());
    if (!symbolTable.compareTypes(t1, retType)) {
      System.out.println("wrong return type for method " + id);
      System.exit(-1);
    }
  }

  // Type t;
  // Identifier i;
  /* public void visit(Formal n) {
      n.t.accept(this);
      n.i.accept(this);
  } */

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    Type t1 = n.e.accept(new TypeCheckExpVisitor());
    if (!(t1 instanceof BooleanType)) {
      System.out.println("the condition of if must be of type boolean");
      System.exit(-1);
    }
    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    Type t1 = n.e.accept(new TypeCheckExpVisitor());
    if (!(t1 instanceof BooleanType)) {
      System.out.println("the condition of while must be of type boolean");
      System.exit(-1);
    }
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    Type t1 = n.e.accept(new TypeCheckExpVisitor());
    if (!(t1 instanceof IntegerType)) {
      System.out.println("the argument of System.out.println must be of type int");
      System.exit(-1);
    }
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    // Type t1 = symbolTable.getVarType(currMethod, currClass, n.i.toString());
    Type t1 = symbolTable.getVar(currMethod, currClass, n.i.toString()).type;
    Type t2 = n.e.accept(new TypeCheckExpVisitor());
    if (!symbolTable.compareTypes(t2, t1)) {
      System.out.println("type error in assignment to " + n.i.toString());
      System.exit(-1);
    }
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    // Type typeI = symbolTable.getVarType(currMethod, currClass, n.i.toString());
    Type t0 = symbolTable.getVar(currMethod, currClass, n.i.toString()).type;
    if (!(t0 instanceof IntArrayType)) {
      System.out.println("the identifier in an array assignment must be of type int []");
      System.exit(-1);
    }
    Type t1 = n.e1.accept(new TypeCheckExpVisitor());
    if (!(t1 instanceof IntegerType)) {
      System.out.println("index of array must be of type int");
      System.exit(-1);
    }
    Type t2 = n.e2.accept(new TypeCheckExpVisitor());
    if (!(t2 instanceof IntegerType)) {
      System.out.println("rhs in an array assignment must be of type int");
      System.exit(-1);
    }
  }
}
