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

line returns [ast.Node node]
  : E=expr { $node = $E.node; } EOL
  | D=declaration { $node = $D.node; } EOL
  ;

declaration returns [ast.Node node]
  : VARIABLE_TYPE VARIABLE_NAME { $node = new ast.VariableDeclaration($VARIABLE_TYPE.text, $VARIABLE_NAME.text, vh); }
  ;

expr returns [ast.Node node]
  : ADD=expr_add { $node = $ADD.node; }
  | VARIABLE_NAME ASSIGN E=expr
    {
      var node = new ast.VariableAssignment($VARIABLE_NAME.text, $E.node, vh);
      node.execute();
      $node = node;
    }
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
  | '<' { var builder = new PolynomBuilder(); }
    MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient); }
    ( OP_ADD MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient, $OP_ADD.text); } )*
    { $node = new ast.Constant(builder.build()); } '>'
  | '(' E=expr ')' { $node = $E.node; }
  | PT=prefixed_term { $node = $PT.node; }
  | VARIABLE_NAME { $node = new ast.VariableAccess($VARIABLE_NAME.text, vh); }
  ;

prefixed_term returns [ast.UnaryOperation node]
  : { var builder = new SignBuilder(); }
    ( OP_ADD { builder.multiplySign($OP_ADD.text); } )+ T=term
    { $node = new ast.UnaryOperation(builder.build(), $T.node); }
  ;

polynom_member returns [double coefficient, int power]
  : 'x' OP_PWR pwr=number[true] { $coefficient = 1; $power = (int)$pwr.value; }
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
OP_MUL: ('*'|'/');
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
OP_PWR: '^';
VARIABLE_TYPE: ('Number'|'Polynom');
VARIABLE_NAME: [a-zA-Z][a-zA-Z0-9]*;
