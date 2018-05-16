package visitor;

import syntaxtree.*;
import java.io.PrintWriter;

public class CodeGenVisitor extends DepthFirstVisitor {

  static Class currClass;
  static Method currMethod;
  static SymbolTable symbolTable;
  PrintWriter out;
 
  public CodeGenVisitor(SymbolTable s, PrintWriter out) {
    symbolTable = s;
    this.out = out;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    // Data segment 
    out.println(
      ".data\n" +
      "newline: .asciiz \"\\n\"\n" +    // to be used by cgen for "System.out.println()"
      "msg_index_out_of_bound_exception: .asciiz \"Index out of bound exception\\n\"\n" +   
      "msg_null_pointer_exception: .asciiz \"Null pointer exception\\n\"\n" +  
      "\n" +
      ".text\n" 
    );

    n.m.accept(this);

    out.println(  // Code to terminate the program
      "# exit\n" +
      "li $v0, 10\n" +
      "syscall\n"
    );

    // Code for all methods
    for ( int i = 0; i < n.cl.size(); i++ ) {
        n.cl.elementAt(i).accept(this);
    }

    // Code for some utility functions 
    cgen_supporting_functions();
  }
  
  // Identifier i1,i2;
  // VarDeclList vl;
  // Statement s;
  public void visit(MainClass n) {
    String i1 = n.i1.toString();
    currClass = symbolTable.getClass(i1);
    currMethod = currClass.getMethod("main");   // This is a hack (treat main() as instance method.)
 
    // Can ignore the parameter of main()
  
    // Info about local variables are kept in "currMethod"

    // Generate code to reserve space for local variables in stack
    // Optionally, generate code to reserve space for temps

    n.s.accept(this);
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
  }
 
  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  // cgen: t i(fl) { vl sl return e; }
  public void visit(MethodDecl n) {
  }

  // Exp e;
  // Statement s1,s2;
  // cgen: if (e) s1 else s2
  public void visit(If n) {
  }

  // Exp e;
  // Statement s;
  // cgen: while (e) s;
  public void visit(While n) {
  }

  // Exp e;
  // cgen: System.out.println(e)
  public void visit(Print n) {
  }
  
  // Identifier i;
  // Exp e;
  // cgen: i = e
  public void visit(Assign n) {
  }

  // Identifier i;
  // Exp e1,e2;
  // cgen: i[e1] = e2
  public void visit(ArrayAssign n) {
  }

  // Exp e1,e2;
  // cgen: e1 && e2
  public void visit(And n) {
  }

  // Exp e1,e2;
  // cgen: e1 < e2
  public void visit(LessThan n) {
  }

  // Exp e1,e2;
  // cgen: e1 + e2
  public void visit(Plus n) {
  }

  // Exp e1,e2;
  // cgen: e1 - e2
  public void visit(Minus n) {
  }

  // Exp e1,e2;
  // cgen: e1 * e2
  public void visit(Times n) {
  }

  // Exp e1,e2;
  // cgen: e1[e2]
  public void visit(ArrayLookup n) {
  }

  // Exp e;
  // cgen: e.length
  public void visit(ArrayLength n) {
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  // cgen: e.i(el)
  public void visit(Call n) {
  }

  // Exp e;
  // cgen: new int [e]
  public void visit(NewArray n) {
  }

  // Identifier i;
  // cgen: new n
  public void visit(NewObject n) {
  }

  // Exp e;
  // cgen: !e
  public void visit(Not n) {
  }

  // cgen: this
  public void visit (This n) {
  }

  // int i;
  // cgen: Load immediate the value of n.i
  public void visit(IntegerLiteral n) {
  }

  // cgen: Load immeidate the value of "true"
  public void visit(True n) {
  }

  // cgen: Load immeidate the value of "false"
  public void visit(False n) {
  }

  // String s;
  // cgen: Load the value of the variable n.s (which can be a local variable, parameter, or field)
  public void visit(IdentifierExp n) {
  }

  void cgen_supporting_functions() {
    out.println(
     "_print_int: # System.out.println(int)\n" +
     "li $v0, 1\n" +
     "syscall\n" +
     "la $a0, newline\n" +
     "li $a1, 1\n" +
     "li $v0, 4   # print newline\n" +
     "syscall\n" +
     "jr $ra\n"
    );

    out.println(
      "_null_pointer_exception:\n" +
      "la $a0, msg_null_pointer_exception\n" + 
      "li $a1, 23\n" +
      "li $v0, 4\n" +
      "syscall\n" +
      "li $v0, 10\n" +
      "syscall\n" 
    );

    out.println(
      "_array_index_out_of_bound_exception:\n" +
      "la $a0, msg_index_out_of_bound_exception\n" + 
      "li $a1, 29\n" +
      "li $v0, 4\n" +
      "syscall\n" +
      "li $v0, 10\n" +
      "syscall\n" 
    );

    out.println(
      "_alloc_int_array: # new int [$a0]\n" +
      "addi $a2, $a0, 0  # Save length in $a2\n" +
      "addi $a0, $a0, 1  # One more word to store the length\n" +
      "sll $a0, $a0, 2   # multiple by 4 bytes\n" +
      "li $v0, 9         # allocate space\n" +
      "syscall\n" +
      "\n" + 
      "sw $a2, 0($v0)    # Store array length\n" +
      "addi $t1, $v0, 4  # begin address = ($v0 + 4); address of the first element\n" +
      "add $t2, $v0, $a0 # loop until ($v0 + 4*(length+1)), the address after the last element\n" +
      "\n" +
      "_alloc_int_array_loop:\n" +
      "beq $t1, $t2, _alloc_int_array_loop_end\n" +
      "sw $0, 0($t1)\n"+
      "addi $t1, $t1, 4\n" +
      "j _alloc_int_array_loop\n" +
      "_alloc_int_array_loop_end:\n" +
      "\n" +
      "jr $ra\n"
    );
  }
}

