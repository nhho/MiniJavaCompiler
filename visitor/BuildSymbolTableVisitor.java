package visitor;
import syntaxtree.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class BuildSymbolTableVisitor extends TypeDepthFirstVisitor {

  SymbolTable symbolTable;

  public BuildSymbolTableVisitor() {
    symbolTable = new SymbolTable();
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
    n.m.accept(this);  // Main class declaration

    // Declaration of remaining classes
    for ( int i = 0; i < n.cl.size(); i++ ) {
        n.cl.elementAt(i).accept(this);
    }
    return null;
  }
  
  // Identifier i1 (name of class),i2 (name of argument in main();
  // Statement s;
  public Type visit(MainClass n) {
     symbolTable.addClass( n.i1.toString(), null); 
     currClass = symbolTable.getClass(n.i1.toString());
      
    //this is an ugly hack.. but its not worth having a Void and
    //String[] type just for one occourance
    currClass.addMethod("main", new IdentifierType("void"));
    currMethod = currClass.getMethod("main");
    currMethod.addParam(n.i2.toString(), new IdentifierType("String []"));

    //currMethod = new Method ("main", new IdentifierType("void"));
    //currMethod.addVar(n.i2.toString(),
		      //new IdentifierType("String[]"));

    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }

    n.s.accept(this);

    currMethod = null;
          
    return null;
  }
  
  // Identifier i;  (Class name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclSimple n) {
    if (!symbolTable.addClass( n.i.toString(), null)){

	System.out.println("Class " +  n.i.toString()
			   + "is already defined" ); 
	System.exit(-1);
    }

    // Entering a new class scope (no need to explicitly leave a class scope)
    currClass =  symbolTable.getClass(n.i.toString());

    // Process field declaration
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }

    // Process method declaration
    for ( int i = 0; i < n.ml.size(); i++ ) {
      n.ml.elementAt(i).accept(this);
    }
    return null;
  }
 
  // Identifier i; (Class name)
  // Identifier j; (Superclass's name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclExtends n) {
    if (!symbolTable.addClass( n.i.toString(),  n.j.toString())) {
      System.out.println("Class " +  n.i.toString()
			   + "is already defined" ); 
      System.exit(-1);
    }

    // Entering a new class scope (no need to explicitly leave a class scope)
    currClass = symbolTable.getClass(n.i.toString());
  
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
      n.ml.elementAt(i).accept(this);
    }
    return null;
  }

  // Type t;
  // Identifier i;
  //
  // Field delcaration or local variable declaration
  public Type visit(VarDecl n) {
    
    Type t =  n.t.accept(this);
    String id =  n.i.toString();

    // Not inside a method => a field declaration
    if (currMethod == null) {

      // Add a field
      if (!currClass.addVar(id,t)) {
	 System.out.println(id + "is already defined in " 
			    + currClass.getId()); 
	 System.exit(-1);
      }
    } else {
      // Add a local variable
      if (!currMethod.addVar(id,t)){
        System.out.println(id + "is already defined in " 
		           + currClass.getId() + "." + currMethod.getId());
	System.exit(-1);
      }
    }
    return null;
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
    
    if (!currClass.addMethod(id,t)){
	System.out.println("Method " + id 
			   + "is already defined in " 
			   + currClass.getId()); 
	System.exit(-1);
    }

    // Entering a method scope 
    currMethod = currClass.getMethod(id);

    for ( int i = 0; i < n.fl.size(); i++ ) {
      n.fl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.sl.size(); i++ ) {
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
    
    if (!currMethod.addParam(id,t)){
	System.out.println("Formal" + id + "is already defined in " 
			   + currClass.getId() + "." +
			   currMethod.getId());
	System.exit(-1);
    }
    return null;
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
    return n;
  }

  // StatementList sl;
  // Optional for MiniJava (unless variable declaration is allowed inside
  // a block
  public Type visit(Block n) {
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
    return null;
  }
}
