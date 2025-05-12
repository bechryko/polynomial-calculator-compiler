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
  : VARIABLE_TYPE { var varsToDeclare = new ArrayList<String>(); }
    VARIABLE_NAME { varsToDeclare.add($VARIABLE_NAME.text); }
    ( OP_SEPARATOR VARIABLE_NAME { varsToDeclare.add($VARIABLE_NAME.text); } )*
    { $node = new ast.VariableDeclaration($VARIABLE_TYPE.text, varsToDeclare, vh); }
  ;

expr returns [ast.Node node]
  : ADD=expr_add { $node = $ADD.node; }
  | a=assignment { $node = $a.node; }
  ;

expr_list returns [List<ast.Node> nodes]
  : e=expr { $nodes = new ArrayList<ast.Node>(); $nodes.add($e.node); }
    ( OP_SEPARATOR e=expr { $nodes.add($e.node); } )*
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
    mem=polynom_member { builder.addCoefficient($mem.value); }
    ( OP_ADD mem=polynom_member { builder.addCoefficient($mem.value, $OP_ADD.text); } )*
    { $node = new ast.LazyConstant(builder, vh); } POLYNOM_CLOSE
  | i=if { $node = $i.node; }
  | w=while { $node = $w.node; }
  | f=for { $node = $f.node; }
  | KEYWORD_SHOW t=term { $node = new ast.ShowNode($t.node); }
  | PAREN_OPENING E=expr PAREN_CLOSING { $node = $E.node; }
  | t=term OP_EVAL_OPENING evalPlace=expr OP_EVAL_CLOSING { $node = new ast.Evaluation($t.node, $evalPlace.node); }
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
  : KEYWORD_FOR PAREN_OPENING al=expr_list EOL e=expr EOL el=expr_list PAREN_CLOSING b=block
    { $node = new ast.LoopNode($al.nodes, $e.node, $el.nodes, $b.node); }
  | KEYWORD_FOR PAREN_OPENING e=expr KEYWORD_TIMES PAREN_CLOSING b=block
    { $node = new ast.LoopNode($e.node, $b.node, true); }
  ;

prefixed_term returns [ast.UnaryOperation node]
  : { var builder = new SignBuilder(); }
    ( OP_ADD { builder.multiplySign($OP_ADD.text); } )+ T=term
    { $node = new ast.UnaryOperation(builder.build(), $T.node); }
  ;

polynom_member returns [PolynomBuilder.PolynomBuildMember value]
  : 'x' OP_PWR pwr=number[true] { $value = new PolynomBuilder.PolynomBuildMember(1, (int) $pwr.value); }
  | 'x' { $value = new PolynomBuilder.PolynomBuildMember(1, 1); }
  | coe=polynom_coefficient 'x' OP_PWR pwr=number[true] {
      $value = new PolynomBuilder.PolynomBuildMember($coe.value, $coe.varName, (int) $pwr.value);
    }
  | coe=polynom_coefficient { $value = new PolynomBuilder.PolynomBuildMember($coe.value, $coe.varName, 0); }
  | coe=polynom_coefficient 'x' { $value = new PolynomBuilder.PolynomBuildMember($coe.value, $coe.varName, 1); }
  ;

polynom_coefficient returns [double value, String varName]
  : num=number[false] { $value = $num.value; }
  | VARIABLE_NAME { $varName = $VARIABLE_NAME.text; }
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
OP_SEPARATOR: ',';
OP_EVAL_OPENING: '[';
OP_EVAL_CLOSING: ']';
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
POLYNOM_START: '<';
POLYNOM_CLOSE: '>';
OP_PWR: '^';
KEYWORD_SHOW: 'show';
KEYWORD_IF: 'if';
KEYWORD_ELSE: 'else';
KEYWORD_WHILE: 'while';
KEYWORD_FOR: 'for';
KEYWORD_TIMES: 'times';
BLOCK_OPENING: '{';
BLOCK_CLOSING: '}';
COMMENT: '//'(.)*?[\n] -> skip;
VARIABLE_TYPE: ('Number'|'Polynom');
VARIABLE_NAME: [a-zA-Z_][a-zA-Z0-9_]*;
