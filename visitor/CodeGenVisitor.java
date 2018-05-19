package visitor;

import java.io.PrintWriter;
import syntaxtree.*;

public class CodeGenVisitor extends DepthFirstVisitor {

  static Class currClass;
  static Method currMethod;
  static SymbolTable symbolTable;
  PrintWriter out;
  int loop;
  int branch;

  public CodeGenVisitor(SymbolTable s, PrintWriter out) {
    symbolTable = s;
    this.out = out;
    loop = 0;
    branch = 0;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    // Data segment
    out.println(".data");
    out.println("newline: .asciiz \"\\n\""); // to be used by cgen for "System.out.println()"
    out.println("_msg_index_out_of_bound_exception: .asciiz \"Index out of bound exception\\n\"");
    out.println("_msg_null_pointer_exception: .asciiz \"Null pointer exception\\n\"");
    out.println("_msg_negative_array_size_exception: .asciiz \"Negative array size exception\\n\"");
    out.println();
    out.println(".text");
    out.println();

    out.println("addi $sp, $sp, -4 # Follow the convention of the lecture");
    out.println("                  # for the stack machine where $sp points");
    out.println("                  # to the next location in the stack");
    out.println();

    n.m.accept(this);

    // Code to terminate the program
    out.println("# exit");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println();

    // Code for all classes
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.elementAt(i).accept(this);
    }

    // Code for some utility functions
    // cgen_supporting_functions();
  }

  // IdentifierType i1
  // Identifier i2;
  // VarDeclList vl;
  // Statement s;
  public void visit(MainClass n) {
    currClass = symbolTable.getClass(n.i1.s);

    minusStack(); // ra
    minusStack(); // fp
    minusStack(); // current instance
    out.println("addi $fp, $sp, 0");

    currMethod = currClass.getMethod("main"); // This is a hack (treat main() as instance method.)

    // Can ignore the parameter of main()

    // Info about local variables are kept in "currMethod"

    // Generate code to reserve space for local variables in stack
    // Optionally, generate code to reserve space for temps

    for (int i = 0; i < n.vl.size(); i++) {
      // n.vl.elementAt(i).accept(this);
      minusStack();
      out.println("sw $0, 4($sp)");
    }

    n.s.accept(this);

    currMethod = null;
    currClass = null;
  }

  // IdentifierType i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    currClass = symbolTable.getClass(n.i.s);
    /* for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }
    currClass = null;
  }

  // IdentifierType i;
  // IdentifierType j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    currClass = symbolTable.getClass(n.i.s);
    /* for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    } */
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }
    currClass = null;
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  // cgen: t i(fl) { vl sl return e; }
  public void visit(MethodDecl n) {
    currMethod = currClass.getMethod(n.i.s);
    out.println("_method_" + currClass.id + "." + currMethod.id +  ":");
    for (int i = 0; i < n.fl.size(); i++) {
      // n.fl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      // n.vl.elementAt(i).accept(this);
      minusStack();
      out.println("sw $0, 4($sp)");
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
    n.e.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      addStack();
    }
    out.println("jr $ra");
    currMethod = null;
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
  }

  public void visit(IntArrayType n) {
  }

  public void visit(BooleanType n) {
  }

  public void visit(IntegerType n) {
  }

  // String s;
  public void visit(IdentifierType n) {
  }

  // String s;
  public void visit(InstanceType n) {
  }

  // StatementList sl;
  public void visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  // cgen: if (e) s1 else s2
  public void visit(If n) {
    n.e.accept(this);
    branch++;
    int b = branch;
    out.println("beq $a0, $0, _branch_" + b + "_a");
    n.s1.accept(this);
    out.println("j _branch_" + b + "_b");
    out.println("_branch_" + b + "_a:");
    n.s2.accept(this);
    out.println("_branch_" + b + "_b:");
  }

  // Exp e;
  // Statement s;
  // cgen: while (e) s;
  public void visit(While n) {
    branch++;
    int b = branch;
    out.println("_branch_" + b + "_a:");
    n.e.accept(this);
    out.println("beq $a0, $0, _branch_" + b + "_b");
    n.s.accept(this);
    out.println("j _branch_" + b + "_a");
    out.println("_branch_" + b + "_b:");
  }

  // Exp e;
  // cgen: System.out.println(e)
  public void visit(Print n) {
    n.e.accept(this);
    out.println("li $v0, 1");
    out.println("syscall");
    out.println("la $a0, newline");
    // out.println("li $a1, 1");
    out.println("li $v0, 4         # print newline");
    out.println("syscall");
  }

  // Identifier i;
  // Exp e;
  // cgen: i = e
  public void visit(Assign n) {
    getIdentifierAddress(n.i.s);
    push();
    n.e.accept(this);
    pop("t0");
    out.println("sw $a0, 0($t0)");
  }

  // Identifier i;
  // Exp e1, e2;
  // cgen: i[e1] = e2
  public void visit(ArrayAssign n) {
    getIdentifierAddress(n.i.s);
    branch++;
    out.println("bne $a0, $0, _branch_" + branch);
    out.println("la $a0, _msg_null_pointer_exception");
    // out.println("li $a1, 23");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + ":");
    push();
    n.e1.accept(this);
    pop("t0");
    out.println("lw $t1, -4($t0)");
    branch++;
    out.println("bltz $a0, _branch_" + branch + "_a");
    out.println("blt $a0, $t1, _branch_" + branch + "_b");
    out.println("_branch_" + branch + "_a:");
    out.println("la $a0, _msg_index_out_of_bound_exception");
    // out.println("li $a1, 29");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + "_b:");
    out.println("sll $a0, $a0, 2   # multiple by 4 bytes");
    out.println("add $t0, $t0, $a0");
    push("t0");
    n.e2.accept(this);
    pop("t0");
    out.println("sw $a0, 0($t0)");
  }

  // Exp e1, e2;
  // cgen: e1 && e2
  public void visit(And n) {
    n.e1.accept(this);
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("and $a0, $t0, $a0");
  }

  // Exp e1, e2;
  // cgen: e1 < e2
  public void visit(LessThan n) {
    n.e1.accept(this);
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("slt $a0, $t0, $a0");
  }

  // Exp e1, e2;
  // cgen: e1 + e2
  public void visit(Plus n) {
    n.e1.accept(this);
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("add $a0, $t0, $a0");
  }

  // Exp e1, e2;
  // cgen: e1 - e2
  public void visit(Minus n) {
    n.e1.accept(this);
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("sub $a0, $t0, $a0");
  }

  // Exp e1, e2;
  // cgen: e1 * e2
  public void visit(Times n) {
    n.e1.accept(this);
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("mult $a0, $t0");
    out.println("mflo $a0");
  }

  // Exp e1, e2;
  // cgen: e1[e2]
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    branch++;
    out.println("bne $a0, $0, _branch_" + branch);
    out.println("la $a0, _msg_null_pointer_exception");
    // out.println("li $a1, 23");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + ":");
    push();
    n.e2.accept(this);
    pop("t0");
    out.println("lw $t1, -4($t0)");
    branch++;
    out.println("bltz $a0, _branch_" + branch + "_a");
    out.println("blt $a0, $t1, _branch_" + branch + "_b");
    out.println("_branch_" + branch + "_a:");
    out.println("la $a0, _msg_index_out_of_bound_exception");
    // out.println("li $a1, 29");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + "_b:");
    out.println("sll $a0, $a0, 2   # multiple by 4 bytes");
    out.println("add $t0, $t0, $a0");
    out.println("lw $a0, ($t0)");
  }

  // Exp e;
  // cgen: e.length
  public void visit(ArrayLength n) {
    n.e.accept(this);
    branch++;
    out.println("bne $a0, $0, _branch_" + branch);
    out.println("la $a0, _msg_null_pointer_exception");
    // out.println("li $a1, 23");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + ":");
    out.println("lw $a0, -4($a0)");
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  // cgen: e.i(el)
  public void visit(Call n) {
    push("ra");
    push("fp");
    n.e.accept(this);
    branch++;
    out.println("bne $a0, $0, _branch_" + branch);
    out.println("la $a0, _msg_null_pointer_exception");
    // out.println("li $a1, 23");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + ":");
    push();
    for (int i = 0; i < n.el.size(); i++) {
      n.el.elementAt(i).accept(this);
      push();
    }
    out.println("li $fp, " + n.el.size());
    out.println("sll $fp, $fp, 2");
    out.println("add $fp, $fp, $sp");
    Class targetClass = symbolTable.getClass(currClass.id);
    while (true) {
      if (targetClass.getMethod(n.i.s) != null) {
        out.println("jal _method_" + targetClass.id + "." + n.i.s);
        break;
      }
      if (targetClass.parent == null) {
        break;
      }
      targetClass = symbolTable.getClass(targetClass.parent);
    }
    for (int i = 0; i <= n.el.size(); i++) {
      addStack();
    }
    pop("fp");
    pop("ra");
  }

  // int i;
  // cgen: Load immediate the value of n.i
  public void visit(IntegerLiteral n) {
    out.println("li $a0, " + n.i);
  }

  // cgen: Load immeidate the value of "true"
  public void visit(True n) {
    out.println("li $a0, 1");
  }

  // cgen: Load immeidate the value of "false"
  public void visit(False n) {
    out.println("li $a0, 0");
  }

  // String s;
  // cgen: Load the value of the variable n.s (which can be a local variable, parameter, or field)
  public void visit(IdentifierExp n) {
    getIdentifierAddress(n.s);
    out.println("lw $a0, 0($a0)    # load variable " + n.s);
  }

  // cgen: this
  public void visit(This n) {
    out.println("lw $a0, 4($fp)");
  }

  // Exp e;
  // cgen: new int [e]
  public void visit(NewArray n) {
    n.e.accept(this);
    branch++;
    out.println("bgez $a0, _branch_" + branch);
    out.println("la $a0, _msg_negative_array_size_exception");
    // out.println("li $a1, 30");
    out.println("li $v0, 4");
    out.println("syscall");
    out.println("li $v0, 10");
    out.println("syscall");
    out.println("_branch_" + branch + ":");
    out.println("addi $t0, $a0, 0  # Save length in $t0");
    out.println("addi $a0, $a0, 1  # One more word to store the length");
    out.println("sll $a0, $a0, 2   # multiple by 4 bytes");
    out.println("li $v0, 9         # allocate space");
    out.println("syscall");
    out.println("sw $t0, 0($v0)    # Store array length");
    out.println("addi $t1, $v0, 4  # begin address = ($v0 + 4)");
    out.println("                  # address of the first element");
    out.println("add $t2, $v0, $a0 # loop until ($v0 + 4 * (length + 1))");
    out.println("addi $a0, $t1, 0");
    out.println("                  # address after the last element");
    loop++;
    out.println("_loop_" + loop + ":");
    out.println("beq $t1, $t2, _loop_" + loop + "_end");
    out.println("sw $0, 0($t1)");
    out.println("addi $t1, $t1, 4");
    out.println("j _loop_" + loop);
    out.println("_loop_" + loop + "_end:");
  }

  // IdentifierType i;
  // cgen: new n
  public void visit(NewObject n) {
    Class targetClass = symbolTable.getClass(n.i.s);
    int size = 0;
    while (true) {
      size += targetClass.fields.size();
      if (targetClass.parent == null) {
        break;
      }
      targetClass = symbolTable.getClass(targetClass.parent);
    }
    size *= 4;
    out.println("li $a0, " + size);
    out.println("li $v0, 9         # allocate space");
    out.println("syscall");
    out.println("addi $a0, $v0, 0  # store address");
  }

  // Exp e;
  // cgen: !e
  public void visit(Not n) {
    out.println("xor $a0, $a0, 1");
  }

  // String s;
  public void visit(Identifier n) {
  }

  void cgen_supporting_functions() {
  }

  void getIdentifierAddress(String a) {
    int cnt = checkMethodVar(a);
    if (cnt != -1) {
      out.println("addi $a0, $fp, " + cnt * -4);
      return;
    }
    out.println("lw $a0, 4($fp)    # look for instance variable " + a);
    cnt = checkClassVar(a);
    out.println("addi $a0, $a0, " + cnt * 4);
  }

  int checkClassVar(String a) {
    Class targetClass = symbolTable.getClass(currClass.id);
    int cnt = 0;
    while (true) {
      for (String key: targetClass.fields.keySet()) {
        if (a.equals(key)) {
          return cnt;
        }
        cnt++;
      }
      targetClass = symbolTable.getClass(targetClass.parent);
    }
  }

  int checkMethodVar(String a) {
    int cnt = 0;
    for (int i = 0; i < currMethod.params.size(); i++) {
      if (a.equals(((Variable)currMethod.params.elementAt(i)).id)) {
        return cnt;
      }
      cnt++;
    }
    for (String key: currMethod.vars.keySet()) {
      if (isParams(key)) {
        continue;
      }
      if (a.equals(key)) {
        return cnt;
      }
      cnt++;
    }
    return -1;
  }

  boolean isParams(String a) {
    for (int i = 0; i < currMethod.params.size(); i++) {
      if (a.equals(((Variable)currMethod.params.elementAt(i)).id)) {
        return true;
      }
    }
    return false;
  }

  void minusStack() {
    out.println("addi $sp, $sp, -4");
  }

  void minusStack(int a) {
    out.println("addi $sp, $sp, -" + 4 * a);
  }

  void addStack() {
    out.println("addi $sp, $sp, 4");
  }

  void addStack(int a) {
    out.println("addi $sp, $sp, " + 4 * a);
  }

  void push() {
    minusStack();
    out.println("sw $a0, 4($sp)");
  }

  void push(String a) {
    minusStack();
    out.println("sw $" + a + ", 4($sp)");
  }

  void pop() {
    out.println("lw $a0, 4($sp)");
    addStack();
  }

  void pop(String a) {
    out.println("lw $" + a + ", 4($sp)");
    addStack();
  }
}

