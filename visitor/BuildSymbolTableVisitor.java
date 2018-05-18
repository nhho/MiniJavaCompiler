package visitor;

import java.util.Enumeration;
import java.util.Hashtable;
import syntaxtree.*;

public class BuildSymbolTableVisitor extends TypeDepthFirstVisitor {

  SymbolTable symbolTable;
  private int state;
  private int need;
  private int done;
  // 0: check class name conflicts
  // 1: check class extension
  // other: dfs

  public BuildSymbolTableVisitor() {
    symbolTable = new SymbolTable();
    need = 0;
    done = 0;
  }

  public SymbolTable getSymTab() {
    return symbolTable;
  }

  // In global scope => both currClass and currMethod are null
  //   Contains class declaration
  // Inside a class (but not in a method) => currMethod is null
  //   Contains field and method declarations
  // Inside a method
  //   Contains declaration of local variables
  // These two variables help keep track of the current scope.
  private Class currClass;
  private Method currMethod;

  // Note: Because in MiniJava there is no nested scopes and all local
  // variables can only be declared at the beginning of a method. This "hack"
  // uses two variables instead of a stack to track nested level.


  // MainClass m;
  // ClassDeclList cl;
  public Type visit(Program n) {
    for (state = 0; state < 3 || done < need; state++) {
      n.m.accept(this);  // Main class declaration

      // Declaration of remaining classes
      for (int i = 0; i < n.cl.size(); i++) {
        n.cl.elementAt(i).accept(this);
      }
    }
    return null;
  }

  // Identifier i1 (name of class),i2 (name of argument in main();
  // Statement s;
  public Type visit(MainClass n) {
    if (state == 0) {
      symbolTable.addClass(n.i1.toString(), null);
    } else if (state == 1) {
      need++;
    } else {
      currClass = symbolTable.getClass(n.i1.toString());
      if (!currClass.vis) {
        currClass.vis = true;
        // this is an ugly hack.. but its not worth having a Void and
        // String[] type just for one occourance
        currClass.addMethod("main", new IdentifierType("void"));
        currMethod = currClass.getMethod("main");
        // currMethod.addParam(n.i2.toString(), new IdentifierType("String []"));
        for (int i = 0; i < n.vl.size(); i++) {
          n.vl.elementAt(i).accept(this);
        }
        n.s.accept(this);
        currMethod = null;
        done++;
      }
    }
    return null;
  }

  // Identifier i;  (Class name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclSimple n) {
    if (state == 0) {
      if (!symbolTable.addClass(n.i.s, null)) {
        System.out.println("Class " +  n.i.s + "is already defined");
        System.exit(-1);
      }
    } else if (state == 1) {
      need++;
    } else {
      // Entering a new class scope (no need to explicitly leave a class scope)
      currClass =  symbolTable.getClass(n.i.s);
      if (!currClass.vis) {
        currClass.vis = true;
        // Process field declaration
        for (int i = 0; i < n.vl.size(); i++) {
          n.vl.elementAt(i).accept(this);
        }
        // Process method declaration
        for (int i = 0; i < n.ml.size(); i++) {
          n.ml.elementAt(i).accept(this);
        }
        done++;
      }
    }
    return null;
  }

  // Identifier i; (Class name)
  // Identifier j; (Superclass's name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclExtends n) {
    if (state == 0) {
      if (!symbolTable.addClass(n.i.s,  n.j.s)) {
        System.out.println("class " +  n.i.s + " is already defined");
        System.exit(-1);
      }
    } else if (state == 1) {
      Class tmp = symbolTable.getClass(n.i.s);
      if (!symbolTable.containsClass(tmp.parent)) {
        System.out.println("class " + n.i.s + " has unknown parent " + n.j.s);
        System.exit(-1);
      }
    } else {
      // Entering a new class scope (no need to explicitly leave a class scope)
      currClass = symbolTable.getClass(n.i.toString());
      Class tmp = symbolTable.getClass(currClass.parent);
      if (!tmp.vis) {
        return null;
      }
      if (!currClass.vis) {
        currClass.vis = true;
        n.j.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
          n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
          n.ml.elementAt(i).accept(this);
        }
        done++;
      }
    }
    return null;
  }

  // Type t;
  // Identifier i;
  //
  // Field delcaration or local variable declaration
  public Type visit(VarDecl n) {
    Type t = n.t.accept(this);
    String id =  n.i.toString();

    // Not inside a method => a field declaration
    if (currMethod == null) {
      // Add a field
      if (!currClass.addVar(id, t)) {
        System.out.println(id + " is already defined in " + currClass.id);
        System.exit(-1);
      }
    } else {
      // Add a local variable
      if (!currMethod.addVar(id, t)) {
        System.out.println(id + " is already defined in " + currClass.id + "." + currMethod.id);
        System.exit(-1);
      }
    }
    return t;
  }

  // Type t;  (Return type)
  // Identifier i; (Method name)
  // FormalList fl; (Formal parameters)
  // VarDeclList vl; (Local variables)
  // StatementList sl;
  // Exp e; (The expression that evaluates to the return value)
  //
  // Method delcaration
  public Type visit(MethodDecl n) {
    Type t = n.t.accept(this);
    String id = n.i.toString();

    // Entering a method scope

    if (!currClass.addMethod(id, t)) {
      System.out.println("method " + id + " is already defined in " + currClass.id);
      System.exit(-1);
    }

    currMethod = currClass.getMethod(id);

    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }

    // n.idRef = currMethod.idRef;

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }

    n.e.accept(this);

    // Leaving a method scope (return to class scope)
    currMethod = null;
    return null;
  }

  // Type t;
  // Identifier i;
  //
  // Register a formal parameter
  public Type visit(Formal n) {
    Type t = n.t.accept(this);
    String id = n.i.toString();

    if (!currMethod.addParam(id, t)) {
      System.out.println("formal " + id + " is already defined in " + currClass.id + "."
          + currMethod.id);
      System.exit(-1);
    }
    return t;
  }

  public Type visit(IntArrayType n) {
    return n;
  }

  public Type visit(BooleanType n) {
    return n;
  }

  public Type visit(IntegerType n) {
    return n;
  }

  // String s;
  public Type visit(IdentifierType n) {
    if (!symbolTable.containsClass(n.s)) {
      System.out.println(n.s + " is an unknown identifier");
      System.exit(-1);
    }
    return n;
  }

  // StatementList sl;
  // Optional for MiniJava (unless variable declaration is allowed inside
  // a block
  public Type visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
    return null;
  }

  // Exp e;
  // Statement s1,s2;
  public Type visit(If n) {
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
    return null;
  }

  // Exp e;
  // Statement s;
  public Type visit(While n) {
    n.e.accept(this);
    n.s.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(Print n) {
    n.e.accept(this);
    return null;
  }

  // Identifier i;
  // Exp e;
  public Type visit(Assign n) {
    Variable tmp = symbolTable.getVar(currMethod, currClass, n.i.s);
    if (tmp == null) {
      System.out.println(n.i.s + "is an unknown identifier");
      System.exit(-1);
    }
    n.e.accept(this);
    return null;
  }

  // Identifier i;
  // Exp e1, e2;
  public Type visit(ArrayAssign n) {
    Variable tmp = symbolTable.getVar(currMethod, currClass, n.i.s);
    if (tmp == null) {
      System.out.println(n.i.s + "is an unknown identifier");
      System.exit(-1);
    }
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(And n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(ArrayLength n) {
    n.e.accept(this);
    return null;
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    n.e.accept(this);
    // n.i.accept(this);
    for (int i = 0; i < n.el.size(); i++) {
      n.el.elementAt(i).accept(this);
    }
    return null;
  }

  // int i;
  public Type visit(IntegerLiteral n) {
    return null;
  }

  public Type visit(True n) {
    return null;
  }

  public Type visit(False n) {
    return null;
  }

  // String s;
  public Type visit(IdentifierExp n) {
    Variable tmp = symbolTable.getVar(currMethod, currClass, n.s);
    if (tmp == null) {
      System.out.println(n.s + "is an unknown identifier");
      System.exit(-1);
    }
    return tmp.type();
  }

  public Type visit(This n) {
    return null;
  }

  // Exp e;
  public Type visit(NewArray n) {
    n.e.accept(this);
    return null;
  }

  // IdentifierType i;
  public Type visit(NewObject n) {
    n.i.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(Not n) {
    n.e.accept(this);
    return null;
  }

  // String s;
  // no access
  public Type visit(Identifier n) {
    return null;
  }
}
