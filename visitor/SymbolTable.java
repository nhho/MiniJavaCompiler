package visitor;
import syntaxtree.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

// The global Symbol Table that maps class name to Class
class SymbolTable {
  private Hashtable<String, Class> hashtable;
   
  public SymbolTable() {
    hashtable = new Hashtable<String, Class>();
  }

  // Register the class name and map it to a new class (with its supperclass)
  // Return false if there is a name conflicts. Otherwise return true.
  public boolean addClass(String id, String parent) {
    if(containsClass(id)) 
      return false;	    
    else 
      hashtable.put(id, new Class(id, parent));
    return true;	    
  }

  // Return the Class that previously mapped to the specified name.
  // Return null if the specified is not found.
  public Class getClass(String id) {
    if(containsClass(id)) 
      return (Class)hashtable.get(id);	    
    else 
      return null;
  }

  public boolean containsClass(String id) {
    return hashtable.containsKey(id);
  }

  // Given a variable "id" that is used in method "m" inside class "c", 
  // return the type of the variable. It returns null if the variable
  // is not yet defined.
  // If "m" is null, check only fields in class "c" or in its ancestors.
  // If "c" is null, check only the variables declared in "m".
  public Type getVarType(Method m, Class c, String id) {
 
    if (m != null) {
      // Check if the variable is one of the local variables
      if (m.getVar(id) != null) {
        return m.getVar(id).type();
      }

      // Check if the variable is one of the formal parameters
      if (m.getParam(id) != null){
        return m.getParam(id).type();
      }
    }
     
    // Try to resolve the name against fields in class
    while(c != null) {
      // Check if the variables is one of the fields in the current class
      if (c.getVar(id) != null) {
        return c.getVar(id).type();  // Found!
      }
      else // Try its superclass (and their superclasses)
      if (c.parent() == null) {
         c = null;
      }
      else {
        c = getClass(c.parent());
      }
    }
      
    System.out.println("Variable " + id 
		        + " not defined in current scope");
    System.exit(0);	// Panic!
    return null;
  }

  // Return the declared method defined in the class named "cName" 
  // (or in one of its ancestors)
  public Method getMethod(String id, String cName) {
    Class c = getClass(cName);

    if (c == null) {
      System.out.println("Class " + cName + " not defined");  
      System.exit(0); // Panic!
    }

    // Try to find the declared method along the class hierarchy
    while (c != null) {
      if (c.getMethod(id) != null) {
        return c.getMethod(id);	 // Found!
      }
      else 
      if(c.parent() == null) {
        c = null;
      }
      else {
        c = getClass(c.parent());
      }
    }
	
    System.out.println("Method " + id + " not defined in class " + cName);
	
    System.exit(0);
    return null;
  }

  // Get the return type of a method declared in a class named "classCope"
  public Type getMethodType(String id, String cName) {
    Method m = getMethod(id, cName);
    if (m != null)
      return m.type();

    return null;
  }

  // Utility method to check if t1 is compatible with t2
  // or if t1 is a subclass of t2
  // Note: This method can be placed in another class
  public boolean compareTypes(Type t1, Type t2) {
    if (t1 == null || t2 == null) return false;
	
    if (t1 instanceof IntegerType && t2 instanceof  IntegerType)
      return true;
    if (t1 instanceof BooleanType && t2 instanceof  BooleanType)
      return true;
    if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
      return true;

    // If both are classes
    if (t1 instanceof IdentifierType && t2 instanceof IdentifierType) {
      IdentifierType i1 = (IdentifierType)t1;
      IdentifierType i2 = (IdentifierType)t2;
	    
      Class c = getClass(i2.s);
      while(c != null) {
        // If two classes has the same name
        if (i1.s.equals(c.getId())) 
          return true;
        else { // Check the next class along the class heirachy

          if (c.parent() == null)
            return false;

          c = getClass(c.parent());
        }
      }
    }
    return false;	
  }

} // SymbolTable

// Store all properties that describe a class
class Class {

  String id;      // Class name
  Hashtable<String, Method> methods;
  Hashtable<String, Variable> fields;
  String parent;  // Superclass's name  (null if there is no superclass)
  Type type;      // An instance of Type that represents this class

  // Model a class named "id" that extend a class name "p"
  // "p" is null if class "id" does has extend any class
  public Class(String id, String p) {
    this.id = id;
    parent = p;
    type = new IdentifierType(id);
    methods = new Hashtable<String, Method>();
    fields = new Hashtable<String, Variable>();
  }
    
  public Class() {}
    
  public String getId(){ return id; }
    
  public Type type(){ return type; }

  // Add a method defined in the current class by registering
  // its name along with its return type.
  // The other properties (parameters, local variables) of the method
  // will be added later
  // 
  // Return false if there is a name conflict (among all method names only)
  public boolean addMethod(String id, Type type) {
    if (containsMethod(id)) 
      return false;       
    else {
      methods.put(id, new Method(id, type));
      return true;
    }
  }

  // Enumeration of method names
  public Enumeration getMethods(){
    return methods.keys();
  }

  // Return the method representation for the specified method 
  public Method getMethod(String id) {
    if (containsMethod(id)) 
      return (Method)methods.get(id);
    else 
      return null;
  }

  // Add a field
  // Return false if there is a name conflict (among all fields only)
  public boolean addVar(String id, Type type) {
    if (fields.containsKey(id)) 
      return false;
    else{
      fields.put(id, new Variable(id, type));
      return true;
    }
  }

  // Return a field with the specified name 
  public Variable getVar(String id) {
    if (containsVar(id)) 
      return (Variable)fields.get(id);
    else 
      return null;
  }
    
  public boolean containsVar(String id) {
    return fields.containsKey(id);
  }
    
  public boolean containsMethod(String id) {
    return methods.containsKey(id);
  }
    
  public String parent() {
    return parent;
  }	    
} // Class

// Store all properties that describe a variable
class Variable {
	
  String id;
  Type type;
    
  public Variable(String id, Type type) {
    this.id = id;
    this.type = type;
  }
    
  public String id() { return id; }
    
  public Type type() { return type; }
	
} // Variable

// Store all properties that describe a variable
class Method {
	
  String id;  // Method name
  Type type;  // Return type
  Vector<Variable> params;          // Formal parameters
  Hashtable<String, Variable> vars; // Local variables
    
  public Method(String id, Type type) {
    this.id = id;
    this.type = type;
    params = new Vector<Variable>();
    vars = new Hashtable<String, Variable>();
  }
    
  public String getId() { return id; }
    
  public Type type() { return type; }
    
  // Add a formal parameter
  // Return false if there is a name conflict
  public boolean addParam(String id, Type type) {
    if (containsParam(id)) 
      return false;       
    else {
      params.addElement(new Variable(id, type));
      return true;
    }
  }
    
  public Enumeration getParams(){
    return params.elements();
  }

  // Return a formal parameter by position (i=0 means 1st parameter)
  public Variable getParamAt(int i){
    if (i < params.size())
      return (Variable)params.elementAt(i);
    else
      return null;
  }

  // Add a local variable
  // Return false if there is a name conflict
  public boolean addVar(String id, Type type) {
    if (vars.containsKey(id)) 
      return false;
    else {
      vars.put(id, new Variable(id, type));
      return true;
    }
  }
    
  public boolean containsVar(String id) {
    return vars.containsKey(id);
  }
    
  public boolean containsParam(String id) {
    for (int i = 0; i< params.size(); i++)
      if (((Variable)params.elementAt(i)).id.equals(id))
        return true;
    return false;
  }
    
  public Variable getVar(String id) {
    if (containsVar(id)) 
      return (Variable)vars.get(id);
    else 
      return null;
  }

  // Return a formal parameter by name
  public Variable getParam(String id) {
    for (int i = 0; i< params.size(); i++)
      if (((Variable)params.elementAt(i)).id.equals(id))
        return (Variable)(params.elementAt(i));
	
      return null;
  }
    
} // Method


