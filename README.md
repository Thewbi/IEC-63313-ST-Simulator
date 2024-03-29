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


## Execution Environment

### Generating Structured Text examples and strategy for execution

To create structured text files, the OpenPLC editor can be used (https://autonomylogic.com/download/). 
It is an extended version of the Beremiz editor. It has been extended to interface with the OpenPLC runtime.

The OpenPLC editor allows you to graphically design solutions using the 
FunctionBlockDiagrams (FBD) language and the SequenctialFunctionChart (SFC) language and also the
rest of the IEC 61131-3 languages.

The OpenPLC editor has compilers that can transform the graphical representation into structured text (ST).
The editor can save your solution to a .st file and fill that file with structured text that implements 
your logic.

The OpenPLC editor will first generate a .st file from your diagrams. It will then use the iec2c.exe compiler
to generate C-Code from IEC 61131-3 languages. The iec2c.exe tends to fail to compile the IEC 61131-3.
The issue is that whenever the iec2c.exe compiler fails, the OpenPLC editor will not open a dialog that lets
you download the generated IEC 61131-3 code! If that happens but you are only interested in the IEC 61131-3 code, 
it is still possible to use the IEC 61131-3 code that the OpenPLC editor has created. You can open a
intermediate file that are placed in this directory: 

```
C:\Users\<UserName>\Documents\OpenPLC\<ProjectName>\build
```

There are two files: generated_plc.st and plc.st. plc.st is the file that is fed to the iec2c.exe compiler
and plc.st contains a logger and I am not sure if it contains any other additional objects that are added 
by the OpenPLC editor.

generated_plc.st contains only the objects that the user has created using the OpenPLC editor. 
generated_plc.st is the file that can be processed by this runtime.

The software in this solution will directly consume ST code and execute it without any intermediate steps.
For example it would probably easier from the perspective of executing the ST code to first transpile it
to IL. IL is a more assembler like syntax and a execution environment for IL could be build more easily since
the individual instructions are broken down to very easy instructions that can be emulated easily on a PC.
Since I have no transpiler from ST to IL, this solution is not possible currently and ST is executed
directly.

### Environments

In order to implement the concept of scope and variables in scope, the idea is to have a stack of
environments. A environments is a data container that holds references to variables.

Initially, when the program starts, the IEC 61131-3 configuration object is searched inside the .st file.

```
CONFIGURATION Config0

  RESOURCE Res0 ON PLC
    VAR_GLOBAL
      global_status : S_FA_STATUS;
    END_VAR
    TASK task0(INTERVAL := T#20ms, PRIORITY := 0);
    PROGRAM instance0 WITH task0 : program0_POU;
  END_RESOURCE
  
END_CONFIGURATION
```

The configuration object defines global variables that are inserted into the bottom most initial environment.

Every call to a function or function block (fb) will place a new environment on the top of the stack.
When the execution of the function or function block is done, the environment is erased from the stack.

To find a variable, it is allowed to search for the function name in the entire stack from top to bottom
until an environment is found that contains that variable. With local variables of a function or fb on top
of the stack and the global variables at the bottom of the stack, global variables are always available
and local variables will shadow variables in lower levels of the stack, creating the concept of local scope
nicely.

TODO: What about retained variables?




## Assumptions about the execution model:

* Global variables (defined in the configuration) are retained (they keep their values) over task iterations.
* External variables have reference semantics (not copy semantics)
* Local variable are not retained unless they have the 'retained' specifier.

From: https://epub.jku.at/obvulihs/download/pdf/4817795?originalFilename=true

> A IEC 61131-3 function block can execute exactly one algorithm and multiple programs can be executed on a processing facility,
> which is called resource. Every program is executed by its own thread on the resource,
> which is depicted as task in Figure 1. Communication between programs is only possible, with global variables or communication function blocks. > Using global variables as
> communication between programs is a bad practice. It will produce large monolithic
> applications, that are hard to maintain. Scaling is also challenging with this system
> model. If parts of a program are to be executed on another resource, it must be split
> into several parts. This is a complicated task, because the system needs to behave exactly the same after splitting

## Program Execution

An instance of the configuration is created.
All the global variables in the configuration are created.
Global variables in the configuration are retained (they keep their values) over task iterations.

The configuration contains the program to execute and the task that executes the program.

The configuration's program instance is created.
Recursively, if the program contains function blocks, those function blocks are instantiated
and the program keeps a reference to all it's function blocks. (If function blocks have external
variables, references to those external variables are created)
If the program has external variables defined, a reference from the program to the global
variables are created. External variables have reference semantics (not copy semantics).
Local variables are reserved in the instance.

The configuration's task instance is created.
The task instance is started. 
The task has access to the global variables so it can pass them to the program.

Each iteration the task will execute the program.
Each iteration, local variables that have no retain flag are reset to their data type's default value
or, if there is an initial value specified, they are reset to that initial value.
The local variables of contained function blocks are also treated according to the above rules.
(--> Have a cycleStart() interface method that is recursively executed from the program over all it children recursively.
cycleStart() has to reset the variables according to the rules defined above.)
Retained variables are not reset.
External variables are not reset.
The list of statements is executed one after the other in the order they are listed.

If a statement executes a function block (SubprogramControlStatement), 
1. for all input variables (not output or local variables!) the parameters are passed in 
using copy semantics and they are copied into local variables 
overriding deault and initial values.
2. all the statements of the function block are executed in order.

If a statement is an assignment (AssignmentStatement) that assignment expression is executed/evaluated
and the evaluated value is stored into the assigned variable.

If a statement is a RepeatStatement, the repeatStatement is executed.

If a SubprogramControlStatement is executed on an SR or RS FlipFlop function block, 
the flip flop function block has to compute it's new state and retain that state.

If a SubprogramControlStatement is executed on a TON function block, the TON function block
has to check if the retained input from the last cycle and the current input form a rising edge.
If they form a rising edge, the timer is started.
If they form a falling edge, the timer is stopped and reset to 0.
The TON function block has to now retain the current input for the next cycle.
The timer is read and the input time configuration is read. If the time on the timer is greater 
then the configured time of the TON function block, then the output goes high.


## SequentialFunctionChart execution

The SequentialFunctionChart that is drawn in the OpenPLC editor is converted to a textual 
representation in StructuredText by the OpenPLC editor and saved to the plc.st file.

The SequentialFunctionChart is formulated as a function block. The function block is
executed in the program, which means it is executed on every cycle.

The SequentialFunctionChart function block has
* variables
* a list of global actions
* a list of currently active steps

A step consists of
* a list of actions

When the SequentialFunctionChart is instantiated

1. the variables are initialized
2. the initial steps are added to the list of current steps.

When the SequentialFunctionChart function block is executed for the current cycle:

1. All actions of the entire SequentialFunctionChart are set to isExecuted(false);
2. the global actions of the SequentialFunctionChart function block are executed.
After execution, isExecuted(true) is called.
3. all steps in the current step list are executed.
4. if the current step executes an action using the S qualifier, the action is added to the list
of global actions of the SequentialFunctionChart function block unless the action is added already.
5. the global actions of the SequentialFunctionChart function block are executed unless they have already
been executed: isExecuted(true).
6. the local actions of all current steps are executed.
7. All transitions are evaluated. For each transition that is activated
* the old state is removed from the current step list
* the new state is added to the current step list


# Next Steps

1. Move code out of main into TONVariableInstance as much as possible.
2. In OpenPLC editor, add Laufzeitüberwachung to P1
3. In OpenPLC editor, combine both laufzeitüberwachungsfehler into a system error status and display it on the GUI
4. Add failure quittierung
5. Add checking for collisions
6. Add workflows
7. Make the ampel work
8. Add parsing of functions (!= function blocks)