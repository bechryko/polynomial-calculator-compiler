grammar CalculatorLang;

options {
  language = Java;
}

@header {
  import utils.*;
}

@members {
  private final VariableHandler vh = new VariableHandler();
}

start returns [ast.StartNode node]
  : { $node = new ast.StartNode(); }
    ( L=line { $node.addChild($L.node); } )*
  ;

block returns [ast.StartNode node]
  : { $node = new ast.StartNode(); }
    BLOCK_OPENING ( L=line { $node.addChild($L.node); } )* BLOCK_CLOSING
  ;

line returns [ast.Node node]
  : E=expr { $node = $E.node; } EOL
  | D=declaration { $node = $D.node; } EOL
  ;

declaration returns [ast.Node node]
  : VARIABLE_TYPE VARIABLE_NAME { $node = new ast.VariableDeclaration($VARIABLE_TYPE.text, $VARIABLE_NAME.text, vh); }
  ;

expr returns [ast.Node node]
  : ADD=expr_add { $node = $ADD.node; }
  | a=assignment { $node = $a.node; }
  ;

expr_add returns [ast.Node node]
  : MUL=expr_mul { $node = $MUL.node; }
    ( OP_ADD MUL=expr_mul { $node = ast.BinaryOperation.appendOperand($OP_ADD.text, $node, $MUL.node); } )*
  ;

expr_mul returns [ast.Node node]
  : T=term { $node = $T.node; }
    ( OP_MUL T=term { $node = ast.BinaryOperation.appendOperand($OP_MUL.text, $node, $T.node); } )*
  ;

term returns [ast.Node node]
  : NUM=number[false] { $node = new ast.Constant($NUM.value); }
  | POLYNOM_START { var builder = new PolynomBuilder(); }
    MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient); }
    ( OP_ADD MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient, $OP_ADD.text); } )*
    { $node = new ast.Constant(builder.build()); } POLYNOM_CLOSE
  | i=if { $node = $i.node; }
  | w=while { $node = $w.node; }
  | f=for { $node = $f.node; }
  | PAREN_OPENING E=expr PAREN_CLOSING { $node = $E.node; }
  | PT=prefixed_term { $node = $PT.node; }
  | VARIABLE_NAME { $node = new ast.VariableAccess($VARIABLE_NAME.text, vh); }
  ;

assignment returns [ast.Node node]
  : VARIABLE_NAME ASSIGN E=expr { $node = new ast.VariableAssignment($VARIABLE_NAME.text, $E.node, vh); }
  ;

if returns [ast.ConditionNode node]
  : KEYWORD_IF PAREN_OPENING e=expr PAREN_CLOSING trueCase=block
    ( KEYWORD_ELSE falseCase=block )? { $node = new ast.ConditionNode($e.node, $trueCase.node, $falseCase.node); }
  ;

while returns [ast.LoopNode node]
  : KEYWORD_WHILE PAREN_OPENING e=expr PAREN_CLOSING b=block { $node = new ast.LoopNode($e.node, $b.node); }
  ;

for returns [ast.LoopNode node]
  : KEYWORD_FOR PAREN_OPENING a=assignment EOL e=expr EOL l=line PAREN_CLOSING b=block
    { $node = new ast.LoopNode($a.node, $e.node, $l.node, $b.node); }
  ;

prefixed_term returns [ast.UnaryOperation node]
  : { var builder = new SignBuilder(); }
    ( OP_ADD { builder.multiplySign($OP_ADD.text); } )+ T=term
    { $node = new ast.UnaryOperation(builder.build(), $T.node); }
  ;

polynom_member returns [double coefficient, int power]
  : 'x' OP_PWR pwr=number[true] { $coefficient = 1; $power = (int)$pwr.value; }
  | 'x' { $coefficient = 1; $power = 1; }
  | num=number[false] 'x' OP_PWR pwr=number[true] { $coefficient = $num.value; $power = (int)$pwr.value; }
  | num=number[false] { $coefficient = $num.value; $power = 0; }
  | num=number[false] 'x' { $coefficient = $num.value; $power = 1; }
  ;

number [boolean isInteger] returns [double value]
  : NUMBER {
    $value = Double.parseDouble("0" + $NUMBER.text);

    if(isInteger && $value % 1 != 0) {
      throw new RuntimeException( $value + " is not an integer value!");
    }
  }
  ;

WS: [ \t\r\n]+ -> skip;
EOL: ';';
NUMBER: ([0-9]*[.])?[0-9]+;
OP_ADD: ('+'|'-');
OP_MUL: ('*'|'/'|'%');
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
POLYNOM_START: '<';
POLYNOM_CLOSE: '>';
OP_PWR: '^';
KEYWORD_IF: 'if';
KEYWORD_ELSE: 'else';
KEYWORD_WHILE: 'while';
KEYWORD_FOR: 'for';
BLOCK_OPENING: '{';
BLOCK_CLOSING: '}';
VARIABLE_TYPE: ('Number'|'Polynom');
VARIABLE_NAME: [a-zA-Z][a-zA-Z0-9]*;
