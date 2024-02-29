# IEC-63313-ST-Simulator

IEC-63313 Structured Text simulator.

## VSCode

settings.json

```
"java.jdt.ls.java.home": "C:\\Program Files\\Java\\jdk-20",
"java.configuration.updateBuildConfiguration": "interactive",
"maven.executable.options": "-s C:/Users/U5353/.m2/settings.xml -D\"maven.wagon.http.ssl.insecure\"=\"true\" -D\"maven.wagon.http.ssl.allowall\"=\"true\" -D\"maven.wagon.http.ssl.ignore.validity.dates\"=\"true\" -D\"maven.resolver.transport\"=\"wagon\"",
```

## Grammar

The grammar was created using the rules outlined in the paper: 

```
A Syntactic Speciﬁcation for the Programming Languages of the IEC 61131-3 Standard
FLOR NARCISO§ 
ADDISON RIOS-BOLIVAR§ 
FRANCISCO HIDROBO‡
OLGA GONZALEZ§

§Universidad de Los Andes
Escuela de Ingenier´ıa de Sistemas
La Hechicera, M´erida 5101
VENEZUELA
fnarciso,ilich,ogonzalez@ula.ve

‡Universidad de Los Andes
Departamento de F´ısica La Hechicera, M´erida 5101
VENEZUELA
hidrobo@ula.ve
```

A function block starts with FUNCTION_BLOCK, has a section of variable declarations followed by a body.
The body contains actions and methods and terminates with the implementation of the function block
followed by END_FUNCTION_BLOCK.

A method has a variable section followed by the implementation code.
Because there are no explicit token that mark the start and the end of the implementation code 
for methods, the start is implicitly detected to be right after the closing token of the 
variable section. The end is implicitly deteced with the start of any other section such
as another method or action declaration.

Similar to methods, actions also have implementation code.
Actions have direct access to the variables of the object that calls the action!
They behave like mixins known from other languages.

Following custom extensions to the IEC grammar are introduced in order to be able 
to stick all objects into a single function block file.

1. Actions get a introductory token (similar to the METHOD token for methods)
This is done so that the implicit rules to detect the start and end of implementation code 
can be detected

2. The function block's implementation block gets two custom token (IMPLEMENTATION and END_IMPLEMENTATION)
which wrap the function block's implementation code, so the parser can detect it properly and
can also apply the implicit rules for demarkation of action and method implementation code.
