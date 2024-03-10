grammar StructuredText;

// https://tomassetti.me/migrating-from-antlr2-to-antlr4/
// ANTLR4 does not build an AST for you! You have to use a visitor and build the AST yourself.
//options {
//    output = AST;
//    ASTLabelType = CommonTree;
//}

// https://stackoverflow.com/questions/23976617/parsing-single-line-comments
LineComment : 
	('--' | '//') ~[\r\n]* -> channel(HIDDEN)
	;
	
BlockComment :
	(('/*' .*? '*/')
	| ('(*' .*? '*)'))
	-> skip
    ;
    
compilation_unit :
	(library_element_declaration)*
	EOF
	;
	
// library_element_name ::= data_type_name | function_name | function_block_type_name | program_type_name | resource_type_name | configuration_name
library_element_name :
//	data_type_name 
//	| 
	function_name 
	| 
	function_block_type_name 
	| 
	program_type_name 
	| 
//	resource_type_name 
//	| 
//	configuration_name
	;
	
//library_element_declaration ::= data_type_declaration | function_declaration | function_block_declaration | program_declaration | configuration_declaration
library_element_declaration:
	data_type_declaration 
	| 
	function_declaration 
	| 
	function_block_declaration 
	| 
	program_declaration 
	| 
	configuration_declaration
	;

// function_declaration ::= ’FUNCTION’ derived_function_name ’:’
// (elementary_type_name | derived_type_name)
// { io_var_declarations | function_var_decls } function_body
// ’END_FUNCTION’
function_declaration:
    FUNCTION derived_function_name ':' 
    ( elementary_type_name | derived_type_name )? // return value
    ( io_var_declarations | function_var_decls )*
        function_body
    END_FUNCTION
    ;

// function_var_decls ::= ’VAR’ [’CONSTANT’]
//  var2_init_decl ’;’ {var2_init_decl ’;’} ’END_VAR’
function_var_decls:
    VAR CONSTANT?
    ( var2_init_decl ';' )*
    END_VAR
    ;
    
//var2_init_decl ::= var1_init_decl | array_var_init_decl |
//    structured_var_init_decl | string_var_declaration

var2_init_decl:
    var1_init_decl
    ;

//function_body ::= ladder_diagram | function_block_diagram |
//instruction_list | statement_list | <other languages>
function_body:
    statement_list
    ;
    
//INTERFACE I_Subject
interface_declaration:
	INTERFACE IDENTIFIER
	// interface method declarations have no body
	(interface_method_declaration)*
//	(method_declaration)*
	;
	
interface_method_declaration:
	METHOD PUBLIC? IDENTIFIER COLON elementary_type_name
	// optional variable declarations
	(
		other_var_declarations
		|
		io_var_declarations
	)*
	// no body required
	;
	
	
	
	
action_declaration:
	ACTION IDENTIFIER function_block_body END_ACTION
	;
	
	

method_declaration:
	interface_method_declaration function_block_body
	;
	

	
	
	
//function_block_type_name ::= standard_function_block_name | derived_function_block_name
function_block_type_name:
	//standard_function_block_name 
	//| 
	derived_function_block_name
	;

//standard_function_block_name ::= <as defined in clause 2.5.2.3 of the standard>
derived_function_block_name: 
	IDENTIFIER
	;

//function_block_declaration ::=
//'FUNCTION_BLOCK' derived_function_block_name
//{ io_var_declarations | other_var_declarations }
//function_block_body
//'END_FUNCTION_BLOCK'
function_block_declaration:
	FUNCTION_BLOCK PUBLIC? IDENTIFIER
	// mandatory variable declarations
	(
		io_var_declarations 
		|
		other_var_declarations
	)+
	// either methods or a single function block body
	(

// custom grammar change - the function_block_body is moved to it's own dedicated slot
		(
			method_declaration
			|
			action_declaration
            //|
            //initial_step
            //|
            //transition
            |
            sequential_function_chart
		)*		
// original grammar
//		(method_declaration*)
//		|
//		function_block_body
	)

// custom grammar change, add function block implementation wrapper token
	(
		IMPLEMENTATION function_block_body END_IMPLEMENTATION
	)?

// function_block_body
    (
        function_block_body
    )?
	END_FUNCTION_BLOCK
	;

//sequential_function_chart ::= sfc_network {sfc_network}
sequential_function_chart:
    sfc_network
    ;

//sfc_network ::= initial_step {step | transition | action}
sfc_network:
    initial_step
    (
        step 
        | 
        transition 
        | 
        action
    )+
    ;

//initial_step ::= 'INITIAL_STEP' step_name ':' {action_association ';'} 'END_STEP'
initial_step:
    'INITIAL_STEP'
    step_name
    COLON
    (
        action_association SEMICOLON
    )*
    'END_STEP'
    ;

//step ::= 'STEP' step_name ':' {action_association ';'} 'END_STEP'
step:
    'STEP' step_name COLON
    (
        action_association SEMICOLON
    )*
    'END_STEP'
    ;

//step_name ::= identifier
step_name:
    IDENTIFIER
    ;

//action_association ::= action_name '(' [action_qualifier] {',' indicator_name} ')'
action_association:
    action_name
    '(' 
    ( action_qualifier )?
    (
        COMMA indicator_name
    )*
    ')'
    ;

//action_name ::= identifier
action_name:
    IDENTIFIER
    ;

//action_qualifier ::= 'N' | 'R' | 'S' | 'P' | timed_qualifier ',' action_time
action_qualifier:
    'N'
    |
    'R'
    |
    'S'
    |
    'P'
    | 
    ( timed_qualifier COMMA action_time )
    ;

//timed_qualifier ::= 'L' | 'D' | 'SD' | 'DS' | 'SL'
timed_qualifier:
    'L' 
    | 
    'D' 
    | 
    'SD' 
    | 
    'DS' 
    | 
    'SL'
    ;

//action_time ::= duration | variable_name
action_time:
    duration
    |
    variable_name
    ;

//indicator_name ::= variable_name
indicator_name:
    variable_name
    ;

//transition ::= ‘TRANSITION’ [transition_name] ['(' 'PRIORITY' ':=' integer ')']
//'FROM' steps 'TO' steps
//transition_condition
//'END_TRANSITION'
transition:
    'TRANSITION'
    ( transition_name )?
    ( '(' 'PRIORITY' ':=' integer ')' )?
    'FROM' steps 'TO' steps
    transition_condition
    'END_TRANSITION'
    ;

//transition_name ::= identifier
transition_name:
    IDENTIFIER
    ;

//steps ::= step_name | '(' step_name ',' step_name {',' step_name} ')'
steps:
    step_name
    |
    '(' step_name ',' step_name (',' step_name)* ')'
    ;

//transition_condition ::= ':' simple_instruction_list | ':=' expression ';' | ':' (fbd_network | rung)
transition_condition:
    COLON statement_list
    |
    ':=' expression SEMICOLON
    //|
    //COLON ( fbd_network | rung )
    ;

//action ::= 'ACTION' action_name ':'
// function_block_body
// 'END_ACTION'
action: 
    'ACTION' action_name COLON
    function_block_body
    'END_ACTION'
    ;

//temp_var_decls ::=
//'VAR_TEMP'
//temp_var_decl ';'
//{temp_var_decl ';'}
//'END_VAR'

//non_retentive_var_decls ::=
//'VAR' 'NON_RETAIN'
//var_init_decl ';'
//{var_init_decl ';'}
//'END_VAR'

//function_block_body ::= ladder_diagram | function_block_diagram | instruction_list | statement_list | <other languages>

// function_name ::= standard_function_name | derived_function_name
function_name:
	standard_function_name
//	| 
//	derived_function_name
	;

// standard_function_name ::= <as defined in clause 2.5.1.5 of the standard>
standard_function_name:
	IDENTIFIER (DOT IDENTIFIER)*
	;

// derived_function_name ::= identifier
derived_function_name:
	IDENTIFIER
	;

//configuration_declaration ::=
//    ’CONFIGURATION’ configuration_name
//    [global_var_declarations]
//    (single_resource_declaration
//    | (resource_declaration {resource_declaration}))
//    [access_declarations]
//    [instance_specific_initializations]
//    ’END_CONFIGURATION’
configuration_declaration: 
    CONFIGURATION configuration_name
    (global_var_declarations)?
    (
        single_resource_declaration
        | 
        ( resource_declaration+ )
    )
//    [access_declarations]
//    [instance_specific_initializations]
    END_CONFIGURATION
    ;

//resource_declaration ::=
//’RESOURCE’ resource_name ’ON’ resource_type_name
//[global_var_declarations]
//single_resource_declaration
//’END_RESOURCE’
resource_declaration:
    'RESOURCE' resource_name 'ON' resource_type_name
    ( global_var_declarations )?
    single_resource_declaration
    'END_RESOURCE'
    ;

// resource_name ::= identifier
resource_name:
    IDENTIFIER
    ;

resource_type_name:
    IDENTIFIER
    ;

//single_resource_declaration ::=
//{task_configuration ’;’}
//program_configuration ’;’
//{program_configuration ’;’}
single_resource_declaration:
    ( task_configuration SEMICOLON )*
    ( program_configuration SEMICOLON )*
    ;

//program_configuration ::=
//’PROGRAM’ [RETAIN | NON_RETAIN]
//program_name [’WITH’ task_name] ’:’ program_type_name
//[’(’ prog_conf_elements ’)’]
program_configuration:
    PROGRAM ( RETAIN | NON_RETAIN )?
    program_name ( 'WITH' task_name )? COLON program_type_name
    ( '(' prog_conf_elements ')' )?
    ;

program_name:
    IDENTIFIER
    ;

program_type_name:
    IDENTIFIER
    ;

// prog_conf_elements ::= prog_conf_element {’,’ prog_conf_element}
prog_conf_elements:
    prog_conf_element ( COMMA prog_conf_element )*
    ;

// prog_conf_element ::= fb_task | prog_cnxn
prog_conf_element:
    fb_task | prog_cnxn
    ;

//fb_task ::= fb_name ’WITH’ task_name
fb_task:
    fb_name 'WITH' task_name
    ;

//prog_cnxn ::= symbolic_variable ’:=’ prog_data_source
//| symbolic_variable ’=>’ data_sink
prog_cnxn:
    symbolic_variable ':=' prog_data_source
    | 
    symbolic_variable '=>' data_sink
    ;

//prog_data_source ::= constant | enumerated_value | global_var_reference | direct_variable
prog_data_source:
    constant 
    | 
    enumerated_value 
    //| 
    //global_var_reference 
    //| 
    //direct_variable
    ;

//data_sink ::= global_var_reference | direct_variable
data_sink:
    //global_var_reference
    //|
    //direct_variable
    ;

//task_configuration ::= ’TASK’ task_name task_initialization
task_configuration:
    TASK task_name task_initialization
    ;

task_name:
    IDENTIFIER
    ;

//task_initialization ::=
//’(’ [’SINGLE’ ’:=’ data_source ’,’]
//[’INTERVAL’ ’:=’ data_source ’,’]
//’PRIORITY’ ’:=’ integer ’)’
task_initialization:
    '('
        ( 'SINGLE' ':=' data_source COMMA )?
        ( 'INTERVAL' ':=' data_source COMMA )?
        'PRIORITY' ':=' integer
    ')'
    ;

//data_source ::= constant | global_var_reference
//| program_output_reference | direct_variable
data_source:
    constant 
    //| 
    //global_var_reference
    //| 
    //program_output_reference 
    //| 
    //direct_variable
    ;

//global_var_declarations ::= ’VAR_GLOBAL’ [’CONSTANT’ | ’RETAIN’]
//global_var_decl ’;’ {global_var_decl ’;’} ’END_VAR’
global_var_declarations:
    VAR_GLOBAL
    ( CONSTANT | RETAIN )?
    ( global_var_decl SEMICOLON )+
    END_VAR
    ;

global_var_decl:
    var_init_decl
    ;

//configuration_name ::= identifier
configuration_name:
    IDENTIFIER
    ;
	
// several VAR-END_VAR are allowed in this grammar!
program_declaration:
	PROGRAM program_type_name
	(
        io_var_declarations 
        |
        other_var_declarations 
    //	| located_var_declarations 
    //	| program_access_decls
	)+
	function_block_body
	END_PROGRAM
	;
	
other_var_declarations:
	external_var_declarations 
    |
	var_declarations 
	//| retentive_var_declarations 
	//| non_retentive_var_declarations
    //| temp_var_decls 
    //| incompl_located_var_declarations
    ;

//external_var_declarations ::= ’VAR_EXTERNAL’ [’CONSTANT’]
//  external_declaration ’;’ {external_declaration ’;’} ’END_VAR’
external_var_declarations:
    VAR_EXTERNAL ( 'CONSTANT' )?
        ( external_declaration SEMICOLON )+
    END_VAR
    ;

//external_declaration ::= global_var_name ’:’ (simple_specification |
//subrange_specification | enumerated_specification | array_specification |
//structure_type_name | function_block_type_name)
external_declaration :
    global_var_name COLON
    (
        simple_specification 
        //|
        //subrange_specification 
        //| 
        //enumerated_specification 
        | 
        array_specification 
        |
        structure_type_name 
        | 
        function_block_type_name
    )
    ;

global_var_name:
    IDENTIFIER
    ;
    
//io_var_declarations ::= input_declarations | output_declarations | input_output_declarations 
io_var_declarations:
	input_declarations
	|
	output_declarations
	;
	
// input_declarations ::= 'VAR_INPUT' ['RETAIN' | 'NON_RETAIN'] input_declaration ';' {input_declaration ';'} 'END_VAR'
input_declarations:
	VAR_INPUT (RETAIN | NON_RETAIN)?
	(input_declaration SEMICOLON)*
	END_VAR
	;
	
//input_declaration ::= var_init_decl | edge_declaration
input_declaration:
	var_init_decl 
	//| 
	//edge_declaration
	;
	
// output_declarations ::= 'VAR_OUTPUT' ['RETAIN' | 'NON_RETAIN'] var_init_decl ';' {var_init_decl ';'} 'END_VAR'
output_declarations:
	VAR_OUTPUT (RETAIN | NON_RETAIN)?
	(output_declaration SEMICOLON)*
	END_VAR
	;
	
output_declaration:
	var_init_decl 
	//| 
	//edge_declaration
	;

var_declarations: 
	VAR 
	( (CONSTANT | PERSISTENT)* (var_init_decl SEMICOLON)+ )?
	END_VAR
	;
	
var_init_decl:
	var1_init_decl 
	| 
	array_var_init_decl 
	//| 
	//structured_var_init_decl 
	// | 
	//fb_name_decl 
	// | 
	//string_var_declaration
	;
	
var1_init_decl: 
	var1_list COLON 
	(
		simple_spec_init 
		//| 
		//subrange_spec_init 
		//|
		//enumerated_spec_init
	)
	;
	
var1_list: 
	variable_name (COMMA variable_name)*
	;
	
simple_spec_init: 
	simple_specification (':=' constant)?
	;
	
simple_specification: 
	elementary_type_name 
	| 
	simple_type_name
	;
	
simple_type_name:
	IDENTIFIER (DOT IDENTIFIER)*
	;
	
// elementary_type_name ::= numeric_type_name | date_type_name | bit_string_type_name | 'STRING' | 'WSTRING' | 'TIME'
elementary_type_name:
	numeric_type_name 
	| 
	//date_type_name 
	//| 
	bit_string_type_name 
	| 
	'STRING' ('(' integer ')')? 
	| 
	'WSTRING' ('(' integer ')')? 
	| 
	'TIME'
	;
	
bit_string_type_name: 
	'BOOL' 
	| 
	'BYTE' 
	| 
	'WORD' 
	| 
	'DWORD' 
	| 
	'LWORD'
	;

// numeric_type_name ::= integer_type_name | real_type_name
numeric_type_name:
	integer_type_name
	//|
	//real_type_name
	;

constant: 
	numeric_literal 
	| 
	character_string 
	| 
	time_literal 
	| 
	bit_string_literal 
	|
	boolean_literal
	;
	
character_string :
	STRING_LITERAL_SINGLE_QUOTE
	;
	
// time_literal ::= duration | time_of_day
time_literal :
	duration 
//	| 
//	time_of_day
	;

// duration ::= (’T’ | ’TIME’) ’#’ [’-’] interval
duration :
	('T' | 'TIME') '#' '-'? interval
	;

// interval ::= days | hours | minutes | seconds | milliseconds
interval : 
	days 
	| 
	hours 
	| 
	minutes 
	| 
	seconds 
	| 
	milliseconds
	;

// days ::= fixed_point (’d’) | integer (’d’) [’_’] hours
days: 
	fixed_point ('d'|'D')
	| 
	integer ('d'|'D') '_'? hours
	;

// fixed_point ::= integer [ ’.’ integer]
fixed_point :
	integer ( '.' integer )?
	;

// hours ::= fixed_point (’h’) | integer (’h’)[’_’] minutes
hours :
	fixed_point ('h'|'H') 
	| 
	integer ('h'|'H') '_'? minutes
	;

// minutes ::= fixed_point (’m’) | integer (’m’) [’_’] seconds
minutes :
	fixed_point ('m'|'M') 
	| 
	integer ('m'|'M') '_'? seconds
	;

// seconds ::= fixed_point (’s’) | integer (’s’) [’_’]milliseconds
seconds :
	fixed_point ('s'|'S') 
	| 
	integer ('s'|'S') '_'? milliseconds
	;

// milliseconds ::= fixed_point (’ms’)
milliseconds :
	 fixed_point ('ms'|'MS')
	 ;
	 
// bit_string_literal ::= [ (’BYTE’ | ’WORD’ | ’DWORD’ | ’LWORD’) ’#’ ]( unsigned_integer | binary_integer | octal_integer | hex_integer)
bit_string_literal :
 	(('BYTE' | 'WORD' | 'DWORD' | 'LWORD') '#')?
 	( 
 		//unsigned_integer
 		integer
 		| 
 		binary_integer 
 		//BINARY_INTEGER
 		//| 
 		//octal_integer 
 		//| 
 		//hex_integer
 	)
	;
	
// binary_integer ::= ’2#’ bit {[’_’] bit}
//binary_integer:
//	'2#' bit ( ('_')? bit )+
//	'2#' DIGIT ( ('_')? DIGIT )+
//'2#' (ONE | ZERO)*
//'2#'
//    '2#' ('1' | '0') ('1' | '0') ('1' | '0')
//	;

binary_integer :
	BINARY_INTEGER
	;
	
// bit ::= ’1’ | ’0’
//bit:
//	'1'
//	|
//	'0'
//	;

boolean_literal :
	 BOOLEAN_EXPLICIT_LITERAL
	 | 
	 'TRUE' 
	 | 
	 'FALSE'
	 ;
	 
numeric_literal :
	integer_literal 
	//| 
	//real_literal
	;
	
// integer_literal ::= [ integer_type_name ’#’ ]( signed_integer | binary_integer | octal_integer | hex_integer)
integer_literal : 
	( integer_type_name '#' )?
	( 
		signed_integer 
		| 
		binary_integer 
		//BINARY_INTEGER
		//|
		//octal_integer 
		//| 
		//hex_integer
	)
	;
	
integer :
	INTEGER
	;	
	
signed_integer : 
//	(PLUS | MINUS)? integer
    (PLUS | MINUS) integer
	;
	
//unsigned_integer:
//	INTEGER
//	;

integer_type_name : 
	signed_integer_type_name 
	| 
	unsigned_integer_type_name
	;
	
signed_integer_type_name : 
	'SINT' 
	| 
	'INT' 
	| 
	'DINT' 
	| 
	'LINT'
	;
	
unsigned_integer_type_name : 
	'USINT' 
	| 
	'UINT' 
	| 
	'UDINT' 
	| 
	'ULINT'
	;

//data_type_declaration ::= 'TYPE' type_declaration ';' {type_declaration ';'} 'END_TYPE'
data_type_declaration :
	TYPE 
	(type_declaration SEMICOLON)+
	END_TYPE
	;

// type_declaration ::= single_element_type_declaration | array_type_declaration | structure_type_declaration | string_type_declaration
type_declaration :
	//single_element_type_declaration 
	//| 
	array_type_declaration 
	| 
	structure_type_declaration 
	//| 
	//string_type_declaration
	;

// array_var_init_decl ::= var1_list ’:’ array_spec_init
array_var_init_decl :
	var1_list COLON array_spec_init
	;

// array_type_declaration ::= array_type_name ’:’ array_spec_init
array_type_declaration :
	array_type_name COLON array_spec_init
	;
	
// array_type_name ::= identifier
array_type_name :
	IDENTIFIER
	;
	
// array_spec_init ::= array_specification [’:=’ array_initialization]
array_spec_init :
	array_specification (':=' array_initialization)?
	;
	
// array_specification ::= array_type_name | ’ARRAY’ ’[’ subrange {’,’ subrange} ’]’ ’OF’ non_generic_type_name
array_specification :
	array_type_name
	|
	ARRAY '[' subrange (COMMA subrange)* ']' OF non_generic_type_name
	;
	
// non_generic_type_name ::= elementary_type_name | derived_type_name
non_generic_type_name :
	elementary_type_name
	|
	derived_type_name
	;
	
// derived_type_name ::= single_element_type_name | array_type_name | structure_type_name | string_type_name
derived_type_name :
	single_element_type_name 
	| 
	array_type_name 
	| 
	structure_type_name 
	| 
	string_type_name
	;
	
// single_element_type_name ::= simple_type_name | subrange_type_name | enumerated_type_name
single_element_type_name :
	simple_type_name
	|
	subrange_type_name
	|
	enumerated_type_name
	;
	
//simple_type_name:
//	IDENTIFIER
//	;
	
subrange_type_name :
	IDENTIFIER
	;
	
//enumerated_type_name:
//	IDENTIFIER
//	;
	
//array_type_name:
//	IDENTIFIER
//	;
	
//structure_type_name:
//	IDENTIFIER
//	;
	
// array_initialization ::= ’[’ array_initial_elements {’,’ array_initial_elements} ’]’
array_initialization :
	'[' array_initial_elements (COMMA array_initial_elements)* ']'
	;
	
// array_initial_elements ::= array_initial_element | integer ’(’[array_initial_element] ’)’
array_initial_elements :
	array_initial_element 
	| 
	integer '(' (array_initial_element)? ')'
//	INTEGER '(' (array_initial_element)? ')'
	;
	
// array_initial_element ::= constant | enumerated_value | structure_initialization | array_initialization
array_initial_element :
	constant
	|
	enumerated_value 
	| 
	structure_initialization 
	| 
	array_initialization
	;

structure_type_declaration :
	structure_type_name COLON structure_specification
	;
	
structure_specification :
	 structure_declaration 
//	 | 
//	 initialized_structure
	 ;
	 
structure_declaration : 
	STRUCT 
	(structure_element_declaration SEMICOLON)+
	END_STRUCT
	;
	
structure_type_name :
	IDENTIFIER
	;
	
// structure_element_declaration ::= structure_element_name ':'(simple_spec_init |subrange_spec_init | enumerated_spec_init | array_spec_init | initialized_structure)
structure_element_declaration : 
	structure_element_name COLON 
	(
		simple_spec_init 
		| 
//		subrange_spec_init 
//		| 
//		enumerated_spec_init 
//		| 
		array_spec_init 
//		| 
//		initialized_structure
	)
	;
	
structure_element_name :
	IDENTIFIER
	;

structure_initialization :
	 '(' structure_element_initialization (COMMA structure_element_initialization)? ')'
	 ;

structure_element_initialization :
	structure_element_name ':=' 
//	(constant | enumerated_value | array_initialization | structure_initialization)
	;

string_type_name : 
	IDENTIFIER
	;

string_type_declaration : 
	string_type_name COLON ('STRING' | 'WSTRING') 
	('[' integer ']')? 
	(':=' STRING_LITERAL)?
	;
	
// function_block_body ::= ladder_diagram | function_block_diagram | instruction_list | statement_list | <other languages>
function_block_body :
	statement_list
	;
	
// statement_list aka. structured_text
// statement_list ::= statement ';' {statement ';'} 
statement_list :
	( statement )+
	;
	
// statement ::= NIL | assignment_statement | subprogram_control_statement | selection_statement | iteration_statement
statement :
	//NIL |
	assignment_statement SEMICOLON
	|
	subprogram_control_statement SEMICOLON
	|
	selection_statement SEMICOLON?
	|
	iteration_statement SEMICOLON
	;

// subprogram_control_statement ::= fb_invocation | ’RETURN’
subprogram_control_statement :
	fb_invocation
	|
	RETURN
	;

// fb_invocation ::= fb_name ’(’ [param_assignment {’,’ param_assignment}]’)’
fb_invocation :
	fb_name ( DOT fb_name )* '(' ( param_assignment (COMMA param_assignment)* )? ')'
	;	
	
// param_assignment ::= ([variable_name ’:=’] expression) | ([’NOT’] variable_name ’=>’ variable)	
param_assignment :
	variable_name ':=' ( expression )?
	|
	expression
	|
	'NOT'? variable_name '=>' variable?
	;

fb_name :
	IDENTIFIER
	;
	
assignment_statement :
	variable ':=' expression
	//variable ':=' numeric_literal
	;
	
// variable ::= direct_variable | symbolic_variable
variable :
	//direct_variable
	//|
	symbolic_variable (DOT variable)*
	;
	
symbolic_variable :
	variable_name 
	| 
	multi_element_variable
	;
	
variable_name :
//	IDENTIFIER (DOT IDENTIFIER)*
	IDENTIFIER
    // the next two options have been added since they look like valid IDENTIFIERS and the compiler will parse them before returning the IDENTIFIER token!
    |
    action_qualifier
    |
    timed_qualifier
	;
	
// multi_element_variable ::= array_variable | structured_variable
multi_element_variable :
	array_variable
//	|
//	structured_variable
	;
	
// array_variable ::= subscripted_variable subscript_list
array_variable :
	//subscripted_variable subscript_list
	IDENTIFIER subscript_list
	;
	
// subscripted_variable ::= symbolic_variable
subscripted_variable :
	symbolic_variable
	;
	
// subscript_list ::= ’[’ subscript {’,’ subscript} ’]’
subscript_list :
	'[' subscript (COMMA subscript)* ']'
	;
	
subscript : 
	expression
	;
	
// expression ::= xor_expression {'OR' xor_expression}
expression :
	xor_expression ( 'OR' xor_expression )?
	;
	
// xor_expression ::= and_expression {'XOR' and_expression}
xor_expression :
	and_expression ( 'OR' and_expression )?
	;
	
// and_expression ::= comparison {('&' | 'AND') comparison}
and_expression :
	comparison (('&' | 'AND') comparison)?
    |
    'AND' '(' comparison ',' comparison ')'
	;
	
// comparison ::= equ_expression { ('=' | '<>') equ_expression}
comparison :
	equ_expression ((EQUALS | '<>') equ_expression)?
	;
	
// equ_expression ::= add_expression {comparison_operator add_expression}
equ_expression :
	add_expression (comparison_operator add_expression)?
	;
	
// comparison_operator ::= '<' | '>' | '<=' | '>='
comparison_operator :
	'<' | '>' | '<=' | '>='
	;

// add_expression ::= term {add_operator term}
add_expression :
	//term (add_operator term)*
    term
    |
    //term add_operator add_expression
    add_expression add_operator term
    //add_expression add_operator add_expression
	;

//add_operator ::= '+' | '-'
add_operator :
	'+' | '-'
	;

// term ::= power_expression {multiply_operator power_expression}	
term :
	power_expression (multiply_operator power_expression)?
	;
	
// multiply_operator ::= '*' | '/' | 'MOD'
multiply_operator :
	'*' | '/' | 'MOD'
	;
	
// power_expression ::= unary_expression {'**' unary_expression}
power_expression :
	unary_expression ('*''*' unary_expression)?
	;
	
//unary_expression ::= [unary_operator] primary_expression
unary_expression :
	unary_operator? primary_expression
	;

// unary_operator ::= '-' | 'NOT'	
unary_operator :
	'-' | 'NOT'
	;
	
//primary_expression ::= constant | enumerated_value | variable | '(' expression ')' | function_name '(' param_assignment {',' param_assignment} ')'
primary_expression :
	constant
	//| 
	//enumerated_value 
	| 
	variable 
	| 
	'(' expression ')' 
	| 
	// param_assignment also allows the use of a simple variable instead of only assignments. The name is misleading.
	function_name '(' param_assignment (',' param_assignment)* ')'
	;
	
// iteration_statement ::= for_statement | while_statement | repeat_statement | exit_statement
iteration_statement :
	for_statement 
	//|
	//while_statement 
    | 
	repeat_statement 
    //| 
	//exit_statement
	;

//repeat_statement ::=
//’REPEAT’ statement_list ’UNTIL’ expression ’END_REPEAT’
repeat_statement :
    'REPEAT' statement_list 'UNTIL' expression 'END_REPEAT'
    ;
	
// for_statement ::= 'FOR' control_variable ':=' for_list 'DO' statement_list 'END_FOR'
for_statement :
	'FOR' control_variable ':=' for_list 'DO' statement_list 'END_FOR'
	;
	
// control_variable ::= identifier
control_variable :
	IDENTIFIER
	;

// for_list ::= expression 'TO' expression ['BY' expression]	
for_list :
	expression 'TO' expression ('BY' expression)?
	;
	
// selection_statement ::= if_statement | case_statement
selection_statement :
	if_statement
	|
	case_statement
	;

//if_statement ::=
//'IF' expression 'THEN' statement_list
//{'ELSIF' expression 'THEN' statement_list}
//['ELSE' statement_list]
//'END_IF'
if_statement :
	'IF' expression 'THEN' statement_list
	(
		'ELSIF' expression 'THEN' statement_list
	)*
	(
		'ELSE' statement_list
	)?
	'END_IF'
	;

//case_statement ::=
//'CASE' expression 'OF'
//case_element
//{case_element}
//['ELSE' statement_list]
//'END_CASE'
case_statement :
	CASE expression OF 
	(case_element)+
	('ELSE' statement_list)?
	'END_CASE'
	;

//case_element ::= case_list ':' statement_list
case_element :
	case_list COLON statement_list
	;

//case_list ::= case_list_element {',' case_list_element}
case_list :
	case_list_element (',' case_list_element)?
	;

//case_list_element ::= subrange | signed_integer | enumerated_value
case_list_element :
	subrange 
	|
	integer
	|
	signed_integer
	|
	binary_integer
	| 
	enumerated_value
	;

// subrange ::= signed_integer ’..’ signed_integer
subrange :
	(signed_integer | integer | IDENTIFIER) '..' (signed_integer | integer | IDENTIFIER)
	;

// enumerated_value ::= [enumerated_type_name ’#’] identifier
enumerated_value :
	(enumerated_type_name ('#' | '.')) IDENTIFIER
	;
	
// enumerated_type_name ::= identifier
enumerated_type_name :
	IDENTIFIER
	;
	
// A
ARRAY : 'ARRAY';
ACTION : 'ACTION';

// C
CASE : 'CASE';
COLON : ':';
COMMA : ',';
CONFIGURATION : 'CONFIGURATION';
CONSTANT : 'CONSTANT';

// D
DOT : '.';
DOUBLE_QUOTES : '"';

// E
END_ACTION : 'END_ACTION';
END_CONFIGURATION : 'END_CONFIGURATION';
END_FUNCTION : 'END_FUNCTION';
END_FUNCTION_BLOCK : 'END_FUNCTION_BLOCK';
END_IMPLEMENTATION : 'END_IMPLEMENTATION'; // custom token!
END_PROGRAM : 'END_PROGRAM';
END_VAR : 'END_VAR';
END_TYPE : 'END_TYPE';
END_STRUCT : 'END_STRUCT';
EQUALS : '=';

// F
FUNCTION : 'FUNCTION';
FUNCTION_BLOCK : 'FUNCTION_BLOCK';

// I
INTERFACE : 'INTERFACE';
IMPLEMENTATION : 'IMPLEMENTATION'; // custom token!

// M
MINUS : '-';
METHOD : 'METHOD';

// N
NON_RETAIN : 'NON_RETAIN';

// O
OF : 'OF';
//ONE : '1';

// P
PERSISTENT : 'PERSISTENT';
PLUS : '+';
PROGRAM : 'PROGRAM';
PUBLIC : 'PUBLIC';

// R
RETAIN : 'RETAIN';
RETURN : 'RETURN';

// S
SEMICOLON : ';';
STRUCT : 'STRUCT';

// T
TICK : '\'';
TYPE : 'TYPE';
TASK : 'TASK';

// U
UNDERSCORE : '_';

// V
VAR : 'VAR';
VAR_EXTERNAL : 'VAR_EXTERNAL';
VAR_INPUT : 'VAR_INPUT';
VAR_OUTPUT : 'VAR_OUTPUT';
VAR_GLOBAL : 'VAR_GLOBAL';

// ZERO
//ZERO: '0';


BINARY_INTEGER:
//	'2#''11'
	'2#' ('1' | '0')+
	;
	
BOOLEAN_EXPLICIT_LITERAL:
	'BOOL#' ('1' | '0')
	;


// Fragments
// fragment lexer rules can only be used by other lexer rules: 
// these will never become a token on their own. 
// Therefore, you cannot use fragment rules in parser rules.

// letter ::= 'A' | 'B' | <...> | 'Z' | 'a' | 'b' | <...> | 'z'
fragment LETTER: 
	[a-zA-Z]
	;

// digit ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
fragment DIGIT:
	[0-9]
	;

// octal_digit ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7'
//OCTAL_DIGIT

// hex_digit ::= digit | 'A' | 'B' | 'C' | 'D' | 'E' | 'F'

INTEGER: 
	DIGIT ('_' DIGIT)+
	|
	DIGIT+
	;

// identifier ::= (letter | ('_' (letter | digit))) {['_'] (letter | digit)}
IDENTIFIER:
	(LETTER | (UNDERSCORE (LETTER | DIGIT))) (UNDERSCORE | LETTER | DIGIT)*
	;
	
STRING_LITERAL_SINGLE_QUOTE: 
	UNTERMINATED_STRING_LITERAL_SINGLE_QUOTE TICK
  	;

UNTERMINATED_STRING_LITERAL_SINGLE_QUOTE: 
	TICK (~['\\\r\n] | '\\' (. | EOF))*
  	;
	
STRING_LITERAL: 
	UNTERMINATED_STRING_LITERAL DOUBLE_QUOTES
  	;

UNTERMINATED_STRING_LITERAL: 
	DOUBLE_QUOTES (~["\\\r\n] | '\\' (. | EOF))*
  	;

NEWLINE: 
	[\r\n]+ -> skip
	;

WS:   
	[ \t\r\n]+ -> skip
    ;