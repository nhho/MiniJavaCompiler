from os import walk
path = 'test'
cnt = 0
for i in walk(path):
    for j in i[2]:
        if j.endswith('.java') and not j.startswith('null'):
            cnt += 1
            name = j[:-5]
            file_path = i[0].replace('\\', '/') + '/' + name
            folder = i[0].replace('\\', '/')
            print '\tjavac $(JAVAC_OPTIONS) %s.java' % file_path
            print '\t$(CODEGEN) < %s.java > %s.asm' % (file_path, file_path)
            print '\t$(MARS) %s.asm > %s_mips.txt' % (file_path, file_path)
            print '\tjava -classpath %s %s > %s_java.txt' % (folder, name, file_path)
            print '\tdiff --strip-trailing-cr -B %s_mips.txt %s_java.txt' % (file_path, file_path)
