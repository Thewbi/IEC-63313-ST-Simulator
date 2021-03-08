grammar StructuredText;

// https://tomassetti.me/migrating-from-antlr2-to-antlr4/
// ANTLR4 does not build an AST for you! You have to use a visitor and build the AST yourself.
//options {
//    output = AST;
//    ASTLabelType = CommonTree;
//}

// https://stackoverflow.com/questions/23976617/parsing-single-line-comments
LineComment: 
	('--' | '//') ~[\r\n]* -> channel(HIDDEN)
	;
	
BlockComment:
	(('/*' .*? '*/')
	| ('(*' .*? '*)'))
	-> skip
    ;
    
//INTERFACE I_Subject
interface_declaration:
	INTERFACE IDENTIFIER
//	(interface_method_declaration)*
	(method_declaration)*
	;
	
//interface_method_declaration:
method_declaration:
	METHOD PUBLIC? IDENTIFIER COLON elementary_type_name
	(
		other_var_declarations
		|
		io_var_declarations
	)?
	;
	

	
	
	
//function_block_type_name ::= standard_function_block_name | derived_function_block_name
function_block_type_name :
	//standard_function_block_name | 
	derived_function_block_name
	;

//standard_function_block_name ::= <as defined in clause 2.5.2.3 of the standard>

derived_function_block_name : 
	IDENTIFIER
	;

//function_block_declaration ::=
//’FUNCTION_BLOCK’ derived_function_block_name
//{ io_var_declarations | other_var_declarations }
//function_block_body
//’END_FUNCTION_BLOCK’
function_block_declaration:
	FUNCTION_BLOCK PUBLIC IDENTIFIER
	(
	//io_var_declarations |
	other_var_declarations
	)
	method_declaration+
	function_block_body
	END_FUNCTION_BLOCK
	;



//temp_var_decls ::=
//’VAR_TEMP’
//temp_var_decl ’;’
//{temp_var_decl ’;’}
//’END_VAR’

//non_retentive_var_decls ::=
//’VAR’ ’NON_RETAIN’
//var_init_decl ’;’
//{var_init_decl ’;’}
//’END_VAR’

//function_block_body ::= ladder_diagram | function_block_diagram | instruction_list | statement_list | <other languages>
	
	

	
	
program_delcaration:
	PROGRAM program_type_name
	(
	io_var_declarations 
	|
	other_var_declarations 
//	| located_var_declarations 
//	| program_access_decls
	)?
	function_block_body
	END_PROGRAM
	;
	
program_type_name:
	IDENTIFIER
	;
	
other_var_declarations :
	//external_var_declarations |
	var_declarations 
	//| retentive_var_declarations 
	//| non_retentive_var_declarations
    //| temp_var_decls 
    //| incompl_located_var_declarations
    ;
    
//io_var_declarations ::= input_declarations | output_declarations | input_output_declarations 
io_var_declarations :
	input_declarations
	;
	
// input_declarations ::= ’VAR_INPUT’ [’RETAIN’ | ’NON_RETAIN’] input_declaration ’;’ {input_declaration ’;’} ’END_VAR’
input_declarations:
	VAR_INPUT (RETAIN | NON_RETAIN)?
	(input_declaration SEMICOLON)+
	END_VAR
	;
	
//input_declaration ::= var_init_decl | edge_declaration
input_declaration :
	var_init_decl 
	//| edge_declaration
	;

var_declarations : 
	VAR 
	( CONSTANT? (var_init_decl SEMICOLON)+ )?
	END_VAR
	;
	
var_init_decl :
	var1_init_decl 
	//| 
	//array_var_init_decl | 
	//structured_var_init_decl | 
	//fb_name_decl | 
	//string_var_declaration
	;
	
var1_init_decl : 
	var1_list COLON 
	(
		simple_spec_init 
		//| 
		//subrange_spec_init |
		//enumerated_spec_init
	)
	;
	
var1_list : 
	variable_name (COMMA variable_name)*
	;
	
simple_spec_init : 
	simple_specification (':=' constant)?
	;
	
simple_specification : 
	elementary_type_name 
	| 
	simple_type_name
	;
	
simple_type_name :
	IDENTIFIER
	;
	
// elementary_type_name ::= numeric_type_name | date_type_name | bit_string_type_name | �STRING� | �WSTRING� | �TIME�
elementary_type_name:
	numeric_type_name | 
	//date_type_name | 
	bit_string_type_name | 
	'STRING' | 
	'WSTRING' | 
	'TIME'
	;
	
bit_string_type_name: 
	'BOOL' | 
	'BYTE' | 
	'WORD' | 
	'DWORD' | 
	'LWORD'
	;

// numeric_type_name ::= integer_type_name | real_type_name
numeric_type_name:
	integer_type_name
	//|
	//real_type_name
	;
	
//constant :
//	'TRUE' | 'FALSE'
//	;

constant : 
	numeric_literal 
	| 
	//character_string | 
	//time_literal | 
	//bit_string_literal |
	boolean_literal
	;
	
boolean_literal :
	 //( 'BOOL#'? ('1' | '0' ) ) | 
	 'TRUE' 
	 | 'FALSE'
	 ;
	 
numeric_literal:
	integer_literal 
	//| 
	//real_literal
	;
	
integer_literal : 
	( integer_type_name '#' )?
	( 
		signed_integer 
		//| 
		//binary_integer |
		//octal_integer | 
		//hex_integer
	)
	;
	
signed_integer : 
	(PLUS | MINUS)? INTEGER
	;

integer_type_name : 
	signed_integer_type_name | 
	unsigned_integer_type_name
	;
	
signed_integer_type_name : 
	'SINT' | 
	'INT' | 
	'DINT' | 
	'LINT'
	;
	
unsigned_integer_type_name : 
	'USINT' | 
	'UINT' | 
	'UDINT' | 
	'ULINT'
	;
	
	



//data_type_declaration ::= ’TYPE’ type_declaration ’;’ {type_declaration ’;’} ’END_TYPE’
data_type_declaration:
	TYPE 
	(type_declaration SEMICOLON)+
	END_TYPE
	;

// type_declaration ::= single_element_type_declaration | array_type_declaration | structure_type_declaration | string_type_declaration
type_declaration:
	//single_element_type_declaration 
	//| 
	//array_type_declaration 
	//| 
	structure_type_declaration 
	//| 
	//string_type_declaration
	;

structure_type_declaration:
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
	


structure_type_name:
	IDENTIFIER
	;
	
// structure_element_declaration ::= structure_element_name ’:’(simple_spec_init |subrange_spec_init | enumerated_spec_init | array_spec_init | initialized_structure)
structure_element_declaration : 
	structure_element_name COLON 
	(
		simple_spec_init 
//		| 
//		subrange_spec_init 
//		| 
//		enumerated_spec_init 
//		| 
//		array_spec_init 
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
	('[' INTEGER ']')? 
	(':=' STRING_LITERAL)?
	;
	
// function_block_body ::= ladder_diagram | function_block_diagram | instruction_list | statement_list | <other languages>
function_block_body:
	statement_list
	;
	
// statement_list aka. structured_text
// statement_list ::= statement �;� {statement �;�} 
statement_list:
	(statement SEMICOLON)+
	;
	
// statement ::= NIL | assignment_statement | subprogram_control_statement | selection_statement | iteration_statement
statement:
	//NIL |
	assignment_statement
	//|
	//subprogram_control_statement 
	|
	selection_statement 
	|
	iteration_statement
	;
	
assignment_statement:
	variable ':=' expression
	//variable ':=' numeric_literal
	;
	
// variable ::= direct_variable | symbolic_variable
variable:
	//direct_variable
	//|
	symbolic_variable
	;
	
symbolic_variable:
	variable_name 
	//| 
	//multi_element_variable
	;
	
variable_name:
	IDENTIFIER
	;
	
// expression ::= xor_expression {�OR� xor_expression}
expression:
	xor_expression ('OR' xor_expression)?
	;
	
// xor_expression ::= and_expression {�XOR� and_expression}
xor_expression:
	and_expression ('OR' and_expression)?
	;
	
// and_expression ::= comparison {(�&� | �AND�) comparison}
and_expression:
	comparison (('&' | 'AND') comparison)?
	;
	
// comparison ::= equ_expression { (�=� | �<>�) equ_expression}
comparison:
	equ_expression (('=' | '<>') equ_expression)?
	;
	
//equ_expression ::= add_expression {comparison_operator add_expression}
equ_expression:
	add_expression (comparison_operator add_expression)?
	;
	
// comparison_operator ::= �<� | �>� | �<=� | �>=�
comparison_operator:
	'<' | '>' | '<=' | '>='
	;

// add_expression ::= term {add_operator term}
add_expression:
	term (add_operator term)?
	;

//add_operator ::= �+� | �-�
add_operator:
	'+' | '-'
	;

// term ::= power_expression {multiply_operator power_expression}	
term:
	power_expression (multiply_operator power_expression)?
	;
	
// multiply_operator ::= �*� | �/� | �MOD�
multiply_operator:
	'*' | '/' | 'MOD'
	;
	
// power_expression ::= unary_expression {�**� unary_expression}
power_expression:
	unary_expression ('*''*' unary_expression)?
	;
	
//unary_expression ::= [unary_operator] primary_expression
unary_expression:
	unary_operator? primary_expression
	;

// unary_operator ::= �-� | �NOT�	
unary_operator:
	'-' | 'NOT'
	;
	
//primary_expression ::= constant | enumerated_value | variable | �(� expression �)� | function_name �(� param_assignment {�,� param_assignment} �)�
primary_expression:
	constant
	//| 
	//enumerated_value 
	| 
	variable 
	// | �(� expression �)� 
	// | function_name �(� param_assignment {�,� param_assignment} �)�
	;
	
	
	
	
// iteration_statement ::= for_statement | while_statement | repeat_statement | exit_statement
iteration_statement:
	for_statement 
	//|
	//while_statement | 
	//repeat_statement | 
	//exit_statement
	;
	
// for_statement ::= �FOR� control_variable �:=� for_list �DO� statement_list �END_FOR�
for_statement:
	'FOR' control_variable ':=' for_list 'DO' statement_list 'END_FOR'
	;
	
// control_variable ::= identifier
control_variable:
	IDENTIFIER
	;

// for_list ::= expression �TO� expression [�BY� expression]	
for_list:
	expression 'TO' expression ('BY' expression)?
	;
	
	
	
	
	
// selection_statement ::= if_statement | case_statement
selection_statement:
	if_statement
	|
	case_statement
	;

//if_statement ::=
//�IF� expression �THEN� statement_list
//{�ELSIF� expression �THEN� statement_list}
//[�ELSE� statement_list]
//�END_IF�
if_statement:
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
//�CASE� expression �OF�
//case_element
//{case_element}
//[�ELSE� statement_list]
//�END_CASE�
case_statement:
	'CASE' expression 'OF' 
	(case_element)+
	('ELSE' statement_list)?
	'END_CASE'
	;

//case_element ::= case_list �:� statement_list
case_element:
	case_list COLON statement_list
	;

//case_list ::= case_list_element {�,� case_list_element}
case_list:
	case_list_element (',' case_list_element)?
	;

//case_list_element ::= subrange | signed_integer | enumerated_value
case_list_element:
	//subrange 
	//| 
	signed_integer 
	//| 
	//enumerated_value
	;





// C
COLON: ':';
COMMA: ',';
CONSTANT: 'CONSTANT';

// D
DOUBLE_QUOTES: '"';

// E
END_FUNCTION_BLOCK: 'END_FUNCTION_BLOCK';
END_PROGRAM : 'END_PROGRAM';
END_VAR : 'END_VAR';
END_TYPE : 'END_TYPE';
END_STRUCT: 'END_STRUCT';

// F
FUNCTION_BLOCK : 'FUNCTION_BLOCK';

// I
INTERFACE: 'INTERFACE';

// M
MINUS: '-';
METHOD: 'METHOD';

// N
NON_RETAIN: 'NON_RETAIN';

// P
PLUS: '+';
PROGRAM : 'PROGRAM';
PUBLIC: 'PUBLIC';

// R
RETAIN: 'RETAIN';

// S
SEMICOLON : ';';
STRUCT : 'STRUCT';

// T
TICK : '\'';
TYPE : 'TYPE';

// U
UNDERSCORE: '_';

// V
VAR: 'VAR';
VAR_INPUT: 'VAR_INPUT';





// Fragments
// fragment lexer rules can only be used by other lexer rules: 
// these will never become a token on their own. Therefor, you cannot use fragment rules in parser rules.

// letter ::= �A� | �B� | <...> | �Z� | �a� | �b� | <...> | �z�
fragment LETTER : [a-zA-Z];

// digit ::= �0� | �1� | �2� | �3� | �4� | �5� | �6� | �7� | �8� |�9�
fragment DIGIT : [0-9];

// octal_digit ::= �0� | �1� | �2� | �3� | �4� | �5� | �6� | �7�
//OCTAL_DIGIT

// hex_digit ::= digit | �A� | �B� | �C� | �D� | �E� | �F�



INTEGER: 
	//DIGIT ('_' DIGIT)*
	//'1'
	DIGIT+
	;
	

// identifier ::= (letter | (�_� (letter | digit))) {[�_�] (letter | digit)}
IDENTIFIER:
	(LETTER | (UNDERSCORE (LETTER | DIGIT))) (UNDERSCORE | LETTER | DIGIT)*
	;
	
STRING_LITERAL : 
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