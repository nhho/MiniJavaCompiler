# Usage: make A=<test case #>
# e.g., to perform test on test0.java, issue command
#   make A=0

# Specify the command to run your code generator
# You may need to modify this to run your code generator
CODEGEN = java -classpath .. Main

# specify the command to run MARS
# "nc" means "Don't output the copyright message"
MARS = java -jar Mars4_5.jar nc 

# To compile a particular test case in order to obtain the expected output
%.class: %.java
	javac $<

# Run your Code generator to generate the MIPS code
# Run the generated MIPS program to get the output
# Run the MiniJava program to get the expected output
# Check if both outputs match
check: test$(A).class
	$(CODEGEN) < test$(A).java > test$(A).asm
	$(MARS) test$(A).asm > out_mips$(A).txt
	java test$(A) > out_java$(A).txt
	diff -B out_mips$(A).txt out_java$(A).txt



