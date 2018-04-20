/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Camilo Sanchez (Camiloasc1)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/

/*******************************************************************************
 * C++14 Grammar for ANTLR v4
 *
 * Based on n4140 draft paper
 * https://github.com/cplusplus/draft/blob/master/papers/n4140.pdf
 * and
 * http://www.nongnu.org/hcb/
 *
 * Possible Issues:
 *
 * Input must avoid conditional compilation blocks (this grammar ignores any preprocessor directive)
 * GCC extensions not yet supported (do not try to parse the preprocessor output)
 * Right angle bracket (C++11) - Solution '>>' and '>>=' are not tokens, only Greater
 * Lexer issue with pure-specifier rule ('0' token) - Solution in embedded code
 *   Change it to match the target language you want in line 1097 or verify inside your listeners/visitors
 *   Java:
if($val.text.compareTo("0")!=0) throw new InputMismatchException(this);
 *   JavaScript:

 *   Python2:

 *   Python3:

 *   C#:

 ******************************************************************************/
grammar CPP14;

/*Basic concepts*/
translationunit
:
	declarationseq? EOF
;

/*Expressions*/
primaryexpression
:
	literal
	| This
	| LeftParen expression RightParen
	| idexpression
	| lambdaexpression
;

idexpression
:
	unqualifiedid
	| qualifiedid
	| Nweak idexpression
;

unqualifiedid
:
	Identifier
	| operatorfunctionid
	| conversionfunctionid
	| literaloperatorid
	| Tilde classname
	| Tilde decltypespecifier
	| templateid
;

qualifiedid
:
	nestednamespecifier Template? unqualifiedid
;

nestednamespecifier
:
	Doublecolon
	| thetypename Doublecolon
	| namespacename Doublecolon
	| decltypespecifier Doublecolon
	| nestednamespecifier Identifier Doublecolon
	| nestednamespecifier Template? simpletemplateid Doublecolon
;

lambdaexpression
:
	lambdaintroducer lambdadeclarator? compoundstatement
;

lambdaintroducer
:
	LeftBracket lambdacapture? RightBracket
;

lambdacapture
:
	capturedefault
	| capturelist
	| capturedefault Comma capturelist
;

capturedefault
:
	And
	| Assign
;

capturelist
:
	capture Ellipsis?
	| capturelist Comma capture Ellipsis?
;

capture
:
	simplecapture
	| initcapture
;

simplecapture
:
	Identifier
	| And Identifier
	| This
;

initcapture
:
	Identifier initializer
	| And Identifier initializer
;

lambdadeclarator
:
	LeftParen parameterdeclarationclause RightParen Mutable? exceptionspecification?
	attributespecifierseq? trailingreturntype?
;

postfixexpression
:
	primaryexpression
	| postfixexpression LeftBracket expression RightBracket
	| postfixexpression LeftBracket bracedinitlist RightBracket
	| postfixexpression LeftParen expressionlist? RightParen
	| simpletypespecifier LeftParen expressionlist? RightParen
	| typenamespecifier LeftParen expressionlist? RightParen
	| simpletypespecifier bracedinitlist
	| typenamespecifier bracedinitlist
	| postfixexpression Dot Template? idexpression
	| postfixexpression Arrow Template? idexpression
	| postfixexpression Dot pseudodestructorname
	| postfixexpression Arrow pseudodestructorname
	| postfixexpression PlusPlus
	| postfixexpression MinusMinus
	| Dynamic_cast Less thetypeid Greater LeftParen expression RightParen
	| Static_cast Less thetypeid Greater LeftParen expression RightParen
	| Reinterpret_cast Less thetypeid Greater LeftParen expression RightParen
	| Const_cast Less thetypeid Greater LeftParen expression RightParen
	| Typeid LeftParen expression RightParen
	| Typeid LeftParen thetypeid RightParen
;

expressionlist
:
	initializerlist
;

pseudodestructorname
:
	nestednamespecifier? thetypename Doublecolon Tilde thetypename
	| nestednamespecifier Template simpletemplateid Doublecolon Tilde thetypename
	| nestednamespecifier? Tilde thetypename
	| Tilde decltypespecifier
;

unaryexpression
:
	postfixexpression
	| PlusPlus castexpression
	| MinusMinus castexpression
	| unaryoperator castexpression
	| Sizeof unaryexpression
	| Sizeof LeftParen thetypeid RightParen
	| Sizeof Ellipsis LeftParen Identifier RightParen
	| Alignof LeftParen thetypeid RightParen
	| noexceptexpression
	| newexpression
	| deleteexpression
;

unaryoperator
:
	Or
	| Star
	| And
	| Plus
	| Not
	| Tilde
	| Minus
;

newexpression
:
	Doublecolon? New newplacement? newtypeid newinitializer?
	| Doublecolon? New newplacement? LeftParen thetypeid RightParen newinitializer?
;

newplacement
:
	LeftParen expressionlist RightParen
;

newtypeid
:
	typespecifierseq newdeclarator?
;

newdeclarator
:
	ptroperator newdeclarator?
	| noptrnewdeclarator
;

noptrnewdeclarator
:
	LeftBracket expression RightBracket attributespecifierseq?
	| noptrnewdeclarator LeftBracket constantexpression RightBracket attributespecifierseq?
;

newinitializer
:
	LeftParen expressionlist? RightParen
	| bracedinitlist
;

deleteexpression
:
	Doublecolon? Delete castexpression
	| Doublecolon? Delete LeftBracket RightBracket castexpression
;

noexceptexpression
:
	Noexcept LeftParen expression RightParen
;

castexpression
:
	unaryexpression
	| LeftParen thetypeid RightParen castexpression
;

pmexpression
:
	castexpression
	| pmexpression DotStar castexpression
	| pmexpression ArrowStar castexpression
;

multiplicativeexpression
:
	pmexpression
	| multiplicativeexpression Star pmexpression
	| multiplicativeexpression Div pmexpression
	| multiplicativeexpression Mod pmexpression
;

additiveexpression
:
	multiplicativeexpression
	| additiveexpression Plus multiplicativeexpression
	| additiveexpression Minus multiplicativeexpression
;

shiftexpression
:
	additiveexpression
	| shiftexpression LeftShift additiveexpression
	| shiftexpression rightShift additiveexpression
;

relationalexpression
:
	shiftexpression
	| relationalexpression Less shiftexpression
	| relationalexpression Greater shiftexpression
	| relationalexpression LessEqual shiftexpression
	| relationalexpression GreaterEqual shiftexpression
;

equalityexpression
:
	relationalexpression
	| equalityexpression Equal relationalexpression
	| equalityexpression NotEqual relationalexpression
;

andexpression
:
	equalityexpression
	| andexpression And equalityexpression
;

exclusiveorexpression
:
	andexpression
	| exclusiveorexpression Caret andexpression
;

inclusiveorexpression
:
	exclusiveorexpression
	| inclusiveorexpression Or exclusiveorexpression
;

logicalandexpression
:
	inclusiveorexpression
	| logicalandexpression AndAnd inclusiveorexpression
;

logicalorexpression
:
	logicalandexpression
	| logicalorexpression OrOr logicalandexpression
;

conditionalexpression
:
	logicalorexpression
	| logicalorexpression Question expression Colon assignmentexpression
;

assignmentexpression
:
	conditionalexpression
	| logicalorexpression assignmentoperator initializerclause
	| throwexpression
;

assignmentoperator
:
	Assign
	| StarAssign
	| DivAssign
	| ModAssign
	| PlusAssign
	| MinusAssign
	| rightShiftAssign
	| LeftShiftAssign
	| AndAssign
	| XorAssign
	| OrAssign
;

expression
:
	assignmentexpression
	| expression Comma assignmentexpression
;

constantexpression
:
	conditionalexpression
;
/*Statements*/
statement
:
	labeledstatement
	| attributespecifierseq? expressionstatement
	| attributespecifierseq? compoundstatement
	| attributespecifierseq? selectionstatement
	| attributespecifierseq? iterationstatement
	| attributespecifierseq? jumpstatement
	| declarationstatement
	| attributespecifierseq? tryblock
;

labeledstatement
:
	attributespecifierseq? Identifier Colon statement
	| attributespecifierseq? Case constantexpression Colon statement
	| attributespecifierseq? Default Colon statement
;

expressionstatement
:
	expression? Semi
;

compoundstatement
:
	LeftBrace statementseq? RightBrace
;

statementseq
:
	statement
	| statementseq statement
;

selectionstatement
:
	If LeftParen condition RightParen statement
	| If LeftParen condition RightParen statement Else statement
	| Switch LeftParen condition RightParen statement
;

condition
:
	expression
	| attributespecifierseq? declspecifierseq declarator Assign initializerclause
	| attributespecifierseq? declspecifierseq declarator bracedinitlist
;

iterationstatement
:
	While LeftParen condition RightParen statement
	| Do statement While LeftParen expression RightParen Semi
	| For LeftParen forinitstatement condition? Semi expression? RightParen statement
	| For LeftParen forrangedeclaration Colon forrangeinitializer RightParen statement
;

forinitstatement
:
	expressionstatement
	| simpledeclaration
;

forrangedeclaration
:
	attributespecifierseq? declspecifierseq declarator
;

forrangeinitializer
:
	expression
	| bracedinitlist
;

jumpstatement
:
	Break Semi
	| Continue Semi
	| Return expression? Semi
	| Return bracedinitlist Semi
	| Goto Identifier Semi
;

declarationstatement
:
	blockdeclaration
;

/*Declarations*/
declarationseq
:
	declaration
	| declarationseq declaration
;

declaration
:
	blockdeclaration
	| functiondefinition
	| templatedeclaration
	| explicitinstantiation
	| explicitspecialization
	| linkagespecification
	| namespacedefinition
	| emptydeclaration
	| attributedeclaration
;

blockdeclaration
:
	simpledeclaration
	| asmdefinition
	| namespacealiasdefinition
	| usingdeclaration
	| usingdirective
	| static_assertdeclaration
	| aliasdeclaration
	| opaqueenumdeclaration
;

aliasdeclaration
:
	Using Identifier attributespecifierseq? Assign thetypeid Semi
;

simpledeclaration
:
	declspecifierseq? initdeclaratorlist? Semi
	| attributespecifierseq declspecifierseq? initdeclaratorlist Semi
;

static_assertdeclaration
:
	Static_assert LeftParen constantexpression Comma Stringliteral RightParen Semi
;

emptydeclaration
:
	Semi
;

attributedeclaration
:
	attributespecifierseq Semi
;

declspecifier
:
	storageclassspecifier
	| typespecifier
	| functionspecifier
	| Friend
	| Typedef
	| Constexpr
;

declspecifierseq
:
	declspecifier attributespecifierseq?
	| declspecifier declspecifierseq
;

storageclassspecifier
:
	Register
	| Static
	| Thread_local
	| Extern
	| Mutable
;

functionspecifier
:
	Inline
	| Virtual
	| Explicit
;

typedefname
:
	Identifier
;

typespecifier
:
	trailingtypespecifier
	| classspecifier
	| enumspecifier
;

trailingtypespecifier
:
	simpletypespecifier
	| elaboratedtypespecifier
	| typenamespecifier
	| cvqualifier
;

typespecifierseq
:
	typespecifier attributespecifierseq?
	| typespecifier typespecifierseq
;

trailingtypespecifierseq
:
	trailingtypespecifier attributespecifierseq?
	| trailingtypespecifier trailingtypespecifierseq
;

simpletypespecifier
:
	nestednamespecifier? thetypename
	| nestednamespecifier Template simpletemplateid
	| Char
	| Char16
	| Char32
	| Wchar
	| Bool
	| Short
	| Int
	| Long
	| Signed
	| Unsigned
	| Float
	| Double
	| Void
	| Auto
	| decltypespecifier
;

thetypename
:
	classname
	| enumname
	| typedefname
	| simpletemplateid
;

decltypespecifier
:
	Decltype LeftParen expression RightParen
	| Decltype LeftParen Auto RightParen
;

elaboratedtypespecifier
:
	classkey attributespecifierseq? nestednamespecifier? Identifier
	| classkey simpletemplateid
	| classkey nestednamespecifier Template? simpletemplateid
	| Enum nestednamespecifier? Identifier
;

enumname
:
	Identifier
;

enumspecifier
:
	enumhead LeftBrace enumeratorlist? RightBrace
	| enumhead LeftBrace enumeratorlist Comma RightBrace
;

enumhead
:
	enumkey attributespecifierseq? Identifier? enumbase?
	| enumkey attributespecifierseq? nestednamespecifier Identifier enumbase?
;

opaqueenumdeclaration
:
	enumkey attributespecifierseq? Identifier enumbase? Semi
;

enumkey
:
	Enum
	| Enum Class
	| Enum Struct
;

enumbase
:
	Colon typespecifierseq
;

enumeratorlist
:
	enumeratordefinition
	| enumeratorlist Comma enumeratordefinition
;

enumeratordefinition
:
	enumerator
	| enumerator Assign constantexpression
;

enumerator
:
	Identifier
;

namespacename
:
	originalnamespacename
	| namespacealias
;

originalnamespacename
:
	Identifier
;

namespacedefinition
:
	namednamespacedefinition
	| unnamednamespacedefinition
;

namednamespacedefinition
:
	originalnamespacedefinition
	| extensionnamespacedefinition
;

originalnamespacedefinition
:
	Inline? Namespace Identifier LeftBrace namespacebody RightBrace
;

extensionnamespacedefinition
:
	Inline? Namespace originalnamespacename LeftBrace namespacebody RightBrace
;

unnamednamespacedefinition
:
	Inline? Namespace LeftBrace namespacebody RightBrace
;

namespacebody
:
	declarationseq?
;

namespacealias
:
	Identifier
;

namespacealiasdefinition
:
	Namespace Identifier Assign qualifiednamespacespecifier Semi
;

qualifiednamespacespecifier
:
	nestednamespecifier? namespacename
;

usingdeclaration
:
	Using Typename? nestednamespecifier unqualifiedid Semi
	| Using Doublecolon unqualifiedid Semi
;

usingdirective
:
	attributespecifierseq? Using Namespace nestednamespecifier? namespacename Semi
;

asmdefinition
:
	Asm LeftParen Stringliteral RightParen Semi
;

linkagespecification
:
	Extern Stringliteral LeftBrace declarationseq? RightBrace
	| Extern Stringliteral declaration
;

attributespecifierseq
:
	attributespecifier
	| attributespecifierseq attributespecifier
;

attributespecifier
:
	LeftBracket LeftBracket attributelist RightBracket RightBracket
	| alignmentspecifier
;

alignmentspecifier
:
	Alignas LeftParen thetypeid Ellipsis? RightParen
	| Alignas LeftParen constantexpression Ellipsis? RightParen
;

attributelist
:
	attribute?
	| attributelist Comma attribute?
	| attribute Ellipsis
	| attributelist Comma attribute Ellipsis
;

attribute
:
	attributetoken attributeargumentclause?
;

attributetoken
:
	Identifier
	| attributescopedtoken
;

attributescopedtoken
:
	attributenamespace Doublecolon Identifier
;

attributenamespace
:
	Identifier
;

attributeargumentclause
:
	LeftParen balancedtokenseq RightParen
;

balancedtokenseq
:
	balancedtoken?
	| balancedtokenseq balancedtoken
;

balancedtoken
:
	LeftParen balancedtokenseq RightParen
	| LeftBracket balancedtokenseq RightBracket
	| LeftBrace balancedtokenseq RightBrace
	/*any token other than a parenthesis , a bracket , or a brace*/
;

/*Declarators*/
initdeclaratorlist
:
	initdeclarator
	| initdeclaratorlist Comma initdeclarator
;

initdeclarator
:
	declarator initializer?
;

declarator
:
	ptrdeclarator
	| noptrdeclarator parametersandqualifiers trailingreturntype
;

ptrdeclarator
:
	noptrdeclarator
	| ptroperator ptrdeclarator
;

noptrdeclarator
:
	declaratorid attributespecifierseq?
	| noptrdeclarator parametersandqualifiers
	| noptrdeclarator LeftBracket constantexpression? RightBracket attributespecifierseq?
	| LeftParen ptrdeclarator RightParen
;

parametersandqualifiers
:
	LeftParen parameterdeclarationclause RightParen cvqualifierseq? refqualifier?
	exceptionspecification? attributespecifierseq?
;

trailingreturntype
:
	Arrow trailingtypespecifierseq abstractdeclarator?
;

ptroperator
:
	Star attributespecifierseq? cvqualifierseq?
	| And attributespecifierseq?
	| AndAnd attributespecifierseq?
	| nestednamespecifier Star attributespecifierseq? cvqualifierseq?
;

cvqualifierseq
:
	cvqualifier cvqualifierseq?
;

cvqualifier
:
	Const
	| Volatile
;

refqualifier
:
	And
	| AndAnd
;

declaratorid
:
	Ellipsis? idexpression
;

thetypeid
:
	typespecifierseq abstractdeclarator?
;

abstractdeclarator
:
	ptrabstractdeclarator
	| noptrabstractdeclarator? parametersandqualifiers trailingreturntype
	| abstractpackdeclarator
;

ptrabstractdeclarator
:
	noptrabstractdeclarator
	| ptroperator ptrabstractdeclarator?
;

noptrabstractdeclarator
:
	noptrabstractdeclarator parametersandqualifiers
	| parametersandqualifiers
	| noptrabstractdeclarator LeftBracket constantexpression? RightBracket attributespecifierseq?
	| LeftBracket constantexpression? RightBracket attributespecifierseq?
	| LeftParen ptrabstractdeclarator RightParen
;

abstractpackdeclarator
:
	noptrabstractpackdeclarator
	| ptroperator abstractpackdeclarator
;

noptrabstractpackdeclarator
:
	noptrabstractpackdeclarator parametersandqualifiers
	| noptrabstractpackdeclarator LeftBracket constantexpression? RightBracket
	attributespecifierseq?
	| Ellipsis
;

parameterdeclarationclause
:
	parameterdeclarationlist? Ellipsis?
	| parameterdeclarationlist Comma Ellipsis
;

parameterdeclarationlist
:
	parameterdeclaration
	| parameterdeclarationlist Comma parameterdeclaration
;

parameterdeclaration
:
	attributespecifierseq? declspecifierseq declarator
	| attributespecifierseq? declspecifierseq declarator Assign initializerclause
	| attributespecifierseq? declspecifierseq abstractdeclarator?
	| attributespecifierseq? declspecifierseq abstractdeclarator? Assign
	initializerclause
;

functiondefinition
:
	attributespecifierseq? declspecifierseq? declarator virtspecifierseq?
	functionbody
;

functionbody
:
	ctorinitializer? compoundstatement
	| functiontryblock
	| Assign Default Semi
	| Assign Delete Semi
;

initializer
:
	braceorequalinitializer
	| LeftParen expressionlist RightParen
;

braceorequalinitializer
:
	Assign initializerclause
	| bracedinitlist
;

initializerclause
:
	assignmentexpression
	| bracedinitlist
;

initializerlist
:
	initializerclause Ellipsis?
	| initializerlist Comma initializerclause Ellipsis?
;

bracedinitlist
:
	LeftBrace initializerlist Comma? RightBrace
	| LeftBrace RightBrace
;

/*Classes*/
classname
:
	Identifier
	| simpletemplateid
;

classspecifier
:
	classhead LeftBrace memberspecification? RightBrace
;

classhead
:
	classkey attributespecifierseq? classheadname classvirtspecifier? baseclause?
	| classkey attributespecifierseq? baseclause?
;

classheadname
:
	nestednamespecifier? classname
;

classvirtspecifier
:
	Final
;

classkey
:
	Class
	| Struct
	| Union
;

memberspecification
:
	memberdeclaration memberspecification?
	| accessspecifier Colon memberspecification?
;

memberdeclaration
:
	attributespecifierseq? declspecifierseq? memberdeclaratorlist? Semi
	| functiondefinition
	| usingdeclaration
	| static_assertdeclaration
	| templatedeclaration
	| aliasdeclaration
	| emptydeclaration
;

memberdeclaratorlist
:
	memberdeclarator
	| memberdeclaratorlist Comma memberdeclarator
;

memberdeclarator
:
	declarator virtspecifierseq? purespecifier?
	| declarator braceorequalinitializer?
	| Identifier? attributespecifierseq? Colon constantexpression
;

virtspecifierseq
:
	virtspecifier
	| virtspecifierseq virtspecifier
;

virtspecifier
:
	Override
	| Final
;

/*
purespecifier:
	Assign '0'//Conflicts with the lexer
 ;
 */
purespecifier
:
	Assign val = Octalliteral
	{if($val.text.compareTo("0")!=0) throw new InputMismatchException(this);}

;

/*Derived classes*/
baseclause
:
	Colon basespecifierlist
;

basespecifierlist
:
	basespecifier Ellipsis?
	| basespecifierlist Comma basespecifier Ellipsis?
;

basespecifier
:
	attributespecifierseq? basetypespecifier
	| attributespecifierseq? Virtual accessspecifier? basetypespecifier
	| attributespecifierseq? accessspecifier Virtual? basetypespecifier
;


classordecltype
:
	nestednamespecifier? classname
	| decltypespecifier
;

basetypespecifier
:
	classordecltype
;

accessspecifier
:
	Private
	| Protected
	| Public
;

/*Special member functions*/
conversionfunctionid
:
	Operator conversiontypeid
;

conversiontypeid
:
	typespecifierseq conversiondeclarator?
;

conversiondeclarator
:
	ptroperator conversiondeclarator?
;

ctorinitializer
:
	Colon meminitializerlist
;

meminitializerlist
:
	meminitializer Ellipsis?
	| meminitializer Ellipsis? Comma meminitializerlist
;

meminitializer
:
	meminitializerid LeftParen expressionlist? RightParen
	| meminitializerid bracedinitlist
;

meminitializerid
:
	classordecltype
	| Identifier
;

/*Overloading*/
operatorfunctionid
:
	Operator theoperator
;

literaloperatorid
:
	Operator Stringliteral Identifier
	| Operator Userdefinedstringliteral
;

/*Templates*/
templatedeclaration
:
	Template Less templateparameterlist Greater declaration
;

templateparameterlist
:
	templateparameter
	| templateparameterlist Comma templateparameter
;

templateparameter
:
	typeparameter
	| parameterdeclaration
;

typeparameter
:
	Class Ellipsis? Identifier?
	| Class Identifier? Assign thetypeid
	| Typename Ellipsis? Identifier?
	| Typename Identifier? Assign thetypeid
	| Template Less templateparameterlist Greater Class Ellipsis? Identifier?
	| Template Less templateparameterlist Greater Class Identifier? Assign idexpression
;

simpletemplateid
:
	templatename Less templateargumentlist? Greater
;

templateid
:
	simpletemplateid
	| operatorfunctionid Less templateargumentlist? Greater
	| literaloperatorid Less templateargumentlist? Greater
;

templatename
:
	Identifier
;

templateargumentlist
:
	templateargument Ellipsis?
	| templateargumentlist Comma templateargument Ellipsis?
;

templateargument
:
	thetypeid
	| constantexpression
	| idexpression
;

typenamespecifier
:
	Typename nestednamespecifier Identifier
	| Typename nestednamespecifier Template? simpletemplateid
;

explicitinstantiation
:
	Extern? Template declaration
;

explicitspecialization
:
	Template Less Greater declaration
;

/*Exception handling*/
tryblock
:
	Try compoundstatement handlerseq
;

functiontryblock
:
	Try ctorinitializer? compoundstatement handlerseq
;

handlerseq
:
	handler handlerseq?
;

handler
:
	Catch LeftParen exceptiondeclaration RightParen compoundstatement
;

exceptiondeclaration
:
	attributespecifierseq? typespecifierseq declarator
	| attributespecifierseq? typespecifierseq abstractdeclarator?
	| Ellipsis
;

throwexpression
:
	Throw assignmentexpression?
;

exceptionspecification
:
	dynamicexceptionspecification
	| noexceptspecification
;

dynamicexceptionspecification
:
	Throw LeftParen typeidlist? RightParen
;

typeidlist
:
	thetypeid Ellipsis?
	| typeidlist Comma thetypeid Ellipsis?
;

noexceptspecification
:
	Noexcept LeftParen constantexpression RightParen
	| Noexcept
;

/*Preprocessing directives*/

MultiLineMacro
:
    '#' (~[\n]*? '\\' '\r'? '\n')+ ~[\n]+ -> channel(HIDDEN)
;

Directive
:
    '#' ~[\n]* -> channel(HIDDEN)
;

/*Lexer*/

/*Keywords*/
Alignas
:
	'alignas'
;

Alignof
:
	'alignof'
;

Asm
:
	'asm'
;

Auto
:
	'auto'
;

Bool
:
	'bool'
;

Break
:
	'break'
;

Case
:
	'case'
;

Catch
:
	'catch'
;

Char
:
	'char'
;

Char16
:
	'char16_t'
;

Char32
:
	'char32_t'
;

Class
:
	'class'
;

Const
:
	'const'
;

Constexpr
:
	'constexpr'
;

Const_cast
:
	'const_cast'
;

Continue
:
	'continue'
;

Decltype
:
	'decltype'
;

Default
:
	'default'
;

Delete
:
	'delete'
;

Do
:
	'do'
;

Double
:
	'double'
;

Dynamic_cast
:
	'dynamic_cast'
;

Else
:
	'else'
;

Enum
:
	'enum'
;

Explicit
:
	'explicit'
;

Export
:
	'export'
;

Extern
:
	'extern'
;

False
:
	'false'
;

Final
:
	'final'
;

Float
:
	'float'
;

For
:
	'for'
;

Friend
:
	'friend'
;

Goto
:
	'goto'
;

If
:
	'if'
;

Inline
:
	'inline'
;

Int
:
	'int'
;

Long
:
	'long'
;

Mutable
:
	'mutable'
;

Namespace
:
	'namespace'
;

New
:
	'new'
;

Noexcept
:
	'noexcept'
;

Nullptr
:
	'nullptr'
;

Nweak
:
	'nweak'
;

Operator
:
	'operator'
;

Override
:
	'override'
;

Private
:
	'private'
;

Protected
:
	'protected'
;

Public
:
	'public'
;

Register
:
	'register'
;

Reinterpret_cast
:
	'reinterpret_cast'
;

Return
:
	'return'
;

Short
:
	'short'
;

Signed
:
	'signed'
;

Sizeof
:
	'sizeof'
;

Static
:
	'static'
;

Static_assert
:
	'static_assert'
;

Static_cast
:
	'static_cast'
;

Struct
:
	'struct'
;

Switch
:
	'switch'
;

Template
:
	'template'
;

This
:
	'this'
;

Thread_local
:
	'thread_local'
;

Throw
:
	'throw'
;

True
:
	'true'
;

Try
:
	'try'
;

Typedef
:
	'typedef'
;

Typeid
:
	'typeid'
;

Typename
:
	'typename'
;

Union
:
	'union'
;

Unsigned
:
	'unsigned'
;

Using
:
	'using'
;

Virtual
:
	'virtual'
;

Void
:
	'void'
;

Volatile
:
	'volatile'
;

Wchar
:
	'wchar_t'
;

While
:
	'while'
;

/*Operators*/
LeftParen
:
	'('
;

RightParen
:
	')'
;

LeftBracket
:
	'['
;

RightBracket
:
	']'
;

LeftBrace
:
	'{'
;

RightBrace
:
	'}'
;

Plus
:
	'+'
;

Minus
:
	'-'
;

Star
:
	'*'
;

Div
:
	'/'
;

Mod
:
	'%'
;

Caret
:
	'^'
;

And
:
	'&'
;

Or
:
	'|'
;

Tilde
:
	'~'
;

Not
:
	'!'
;

Assign
:
	'='
;

Less
:
	'<'
;

Greater
:
	'>'
;

PlusAssign
:
	'+='
;

MinusAssign
:
	'-='
;

StarAssign
:
	'*='
;

DivAssign
:
	'/='
;

ModAssign
:
	'%='
;

XorAssign
:
	'^='
;

AndAssign
:
	'&='
;

OrAssign
:
	'|='
;

LeftShift
:
	'<<'
;

rightShift
:
//'>>'
	'>>'
;

LeftShiftAssign
:
	'<<='
;

rightShiftAssign
:
//'>>='
	'>>='
;

Equal
:
	'=='
;

NotEqual
:
	'!='
;

LessEqual
:
	'<='
;

GreaterEqual
:
	'>='
;

AndAnd
:
	'&&'
;

OrOr
:
	'||'
;

PlusPlus
:
	'++'
;

MinusMinus
:
	'--'
;

Comma
:
	','
;

ArrowStar
:
	'->*'
;

Arrow
:
	'->'
;

Question
:
	'?'
;

Colon
:
	':'
;

Doublecolon
:
	'::'
;

Semi
:
	';'
;

Dot
:
	'.'
;

DotStar
:
	'.*'
;

Ellipsis
:
	'...'
;

theoperator
:
	New
	| Delete
	| New LeftBracket RightBracket
	| Delete LeftBracket RightBracket
	| Plus
	| Minus
	| Star
	| Div
	| Mod
	| Caret
	| And
	| Or
	| Tilde
	| Not
	| Assign
	| Less
	| Greater
	| PlusAssign
	| MinusAssign
	| StarAssign
	| DivAssign
	| ModAssign
	| XorAssign
	| AndAssign
	| OrAssign
	| LeftShift
	| rightShift
	| rightShiftAssign
	| LeftShiftAssign
	| Equal
	| NotEqual
	| LessEqual
	| GreaterEqual
	| AndAnd
	| OrOr
	| PlusPlus
	| MinusMinus
	| Comma
	| ArrowStar
	| Arrow
	| LeftParen RightParen
	| LeftBracket RightBracket
;

/*Lexer*/
fragment
Hexquad
:
	HEXADECIMALDIGIT HEXADECIMALDIGIT HEXADECIMALDIGIT HEXADECIMALDIGIT
;

fragment
Universalcharactername
:
	'\\u' Hexquad
	| '\\U' Hexquad Hexquad
;

Identifier
:
/*
	Identifiernondigit
	| Identifier Identifiernondigit
	| Identifier DIGIT
	*/
	Identifiernondigit
	(
		Identifiernondigit
		| DIGIT
	)*
;

fragment
Identifiernondigit
:
	NONDIGIT
	| Universalcharactername
	/* other implementation defined characters*/
;

fragment
NONDIGIT
:
	[a-zA-Z_]
;

fragment
DIGIT
:
	[0-9]
;

literal
:
	Integerliteral
	| Characterliteral
	| Floatingliteral
	| Stringliteral
	| booleanliteral
	| pointerliteral
	| userdefinedliteral
;

Integerliteral
:
	Decimalliteral Integersuffix?
	| Octalliteral Integersuffix?
	| Hexadecimalliteral Integersuffix?
	| Binaryliteral Integersuffix?
;

Decimalliteral
:
	NONZERODIGIT
	(
		'\''? DIGIT
	)*
;

Octalliteral
:
	'0'
	(
		'\''? OCTALDIGIT
	)*
;

Hexadecimalliteral
:
	(
		'0x'
		| '0X'
	) HEXADECIMALDIGIT
	(
		'\''? HEXADECIMALDIGIT
	)*
;

Binaryliteral
:
	(
		'0b'
		| '0B'
	) BINARYDIGIT
	(
		'\''? BINARYDIGIT
	)*
;

fragment
NONZERODIGIT
:
	[1-9]
;

fragment
OCTALDIGIT
:
	[0-7]
;

fragment
HEXADECIMALDIGIT
:
	[0-9a-fA-F]
;

fragment
BINARYDIGIT
:
	[01]
;

Integersuffix
:
	Unsignedsuffix Longsuffix?
	| Unsignedsuffix Longlongsuffix?
	| Longsuffix Unsignedsuffix?
	| Longlongsuffix Unsignedsuffix?
;

fragment
Unsignedsuffix
:
	[uU]
;

fragment
Longsuffix
:
	[lL]
;

fragment
Longlongsuffix
:
	'll'
	| 'LL'
;

Characterliteral
:
	'\'' Cchar+ '\''
	| 'u' '\'' Cchar+ '\''
	| 'U' '\'' Cchar+ '\''
	| 'L' '\'' Cchar+ '\''
;

fragment
Cchar
:
	~['\\\r\n]
	| Escapesequence
	| Universalcharactername
;

fragment
Escapesequence
:
	Simpleescapesequence
	| Octalescapesequence
	| Hexadecimalescapesequence
;

fragment
Simpleescapesequence
:
	'\\\''
	| '\\"'
	| '\\?'
	| '\\\\'
	| '\\a'
	| '\\b'
	| '\\f'
	| '\\n'
	| '\\r'
	| '\\t'
	| '\\v'
;

fragment
Octalescapesequence
:
	'\\' OCTALDIGIT
	| '\\' OCTALDIGIT OCTALDIGIT
	| '\\' OCTALDIGIT OCTALDIGIT OCTALDIGIT
;

fragment
Hexadecimalescapesequence
:
	'\\x' HEXADECIMALDIGIT+
;

Floatingliteral
:
	Fractionalconstant Exponentpart? Floatingsuffix?
	| Digitsequence Exponentpart Floatingsuffix?
;

fragment
Fractionalconstant
:
	Digitsequence? Dot Digitsequence
	| Digitsequence Dot
;

fragment
Exponentpart
:
	'e' SIGN? Digitsequence
	| 'E' SIGN? Digitsequence
;

fragment
SIGN
:
	[+-]
;

fragment
Digitsequence
:
	DIGIT
	(
		'\''? DIGIT
	)*
;

fragment
Floatingsuffix
:
	[flFL]
;

Stringliteral
:
	Encodingprefix? '"' Schar* '"'
	| Encodingprefix? 'R' Rawstring
;

fragment
Encodingprefix
:
	'u8'
	| 'u'
	| 'U'
	| 'L'
;

fragment
Schar
:
	~["\\\r\n]
	| Escapesequence
	| Universalcharactername
;

fragment
Rawstring /* '"' dcharsequence? '(' rcharsequence? RightParen dcharsequence? '"' */
:
	'"' .*? LeftParen .*? RightParen .*? '"'
;

booleanliteral
:
	False
	| True
;

pointerliteral
:
	Nullptr
;

userdefinedliteral
:
	Userdefinedintegerliteral
	| Userdefinedfloatingliteral
	| Userdefinedstringliteral
	| Userdefinedcharacterliteral
;

Userdefinedintegerliteral
:
	Decimalliteral Udsuffix
	| Octalliteral Udsuffix
	| Hexadecimalliteral Udsuffix
	| Binaryliteral Udsuffix
;

Userdefinedfloatingliteral
:
	Fractionalconstant Exponentpart? Udsuffix
	| Digitsequence Exponentpart Udsuffix
;

Userdefinedstringliteral
:
	Stringliteral Udsuffix
;

Userdefinedcharacterliteral
:
	Characterliteral Udsuffix
;

fragment
Udsuffix
:
	Identifier
;

Whitespace
:
	[ \t]+ -> channel(HIDDEN)
;

Newline
:
	(
		'\r' '\n'?
		| '\n'
	) -> channel(HIDDEN)
;

BlockComment
:
	'/*' .*? '*/' -> channel(HIDDEN)
;

LineComment
:
	'//' ~[\r\n]* -> channel(HIDDEN)
;
