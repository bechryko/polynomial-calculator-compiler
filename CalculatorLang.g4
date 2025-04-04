grammar CalculatorLang;

options {
  language = Java;
}

@header {
  import utils.*;
}

start returns [ast.EvaluatableNode node]
  : ( L=line { $node = $L.node; } )*
  ;

line returns [ast.EvaluatableNode node]
  : E=expr { $node = $E.node; } EOL
  ;

expr returns [ast.EvaluatableNode node]
  : ADD=expr_add { $node = $ADD.node; }
  ;

expr_add returns [ast.EvaluatableNode node]
  : MUL=expr_mul { $node = $MUL.node; }
    ( OP_ADD MUL=expr_mul { $node = ast.BinaryOperation.appendOperand($OP_ADD.text, $node, $MUL.node); } )*
  ;

expr_mul returns [ast.EvaluatableNode node]
  : T=term { $node = $T.node; }
    ( OP_MUL T=term { $node = ast.BinaryOperation.appendOperand($OP_MUL.text, $node, $T.node); } )*
  ;

term returns [ast.EvaluatableNode node]
  : NUM=number[false] { $node = new ast.Constant($NUM.value); }
  | '<' { var builder = new PolynomBuilder(); }
    MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient); }
    ( '+' MEM=polynom_member { builder.addCoefficient($MEM.power, $MEM.coefficient); } )*
    { $node = new ast.Constant(builder.build()); } '>'
  | '(' E=expr ')' { $node = $E.node; }
  | PT=prefixed_term { $node = $PT.node; }
  ;

prefixed_term returns [ast.UnaryOperation node]
  : { var builder = new SignBuilder(); }
    ( OP_ADD { builder.multiplySign($OP_ADD.text); } )+ T=term
    { $node = new ast.UnaryOperation(builder.build(), $T.node); }
  ;

polynom_member returns [double coefficient, int power]
  : NUM=number { $coefficient = $NUM.value; $power = 0; }
  | NUM=number 'x' { $coefficient = $NUM.value; $power = 1; }
  | 'x' OP_PWR INT=integer { $coefficient = 1; $power = $INT.value; }
  | NUM=number 'x' OP_PWR INT=integer { $coefficient = $NUM.value; $power = $INT.value; }
  ;

number [boolean isInteger] returns [double value]
  : NUMBER {
    $value = Double.parseDouble("0" + $NUMBER.text);

    if(isInteger && $value % 1 !== 0) {
      throw new Exception( $value + " is not an integer value!");
    }
  }
  ;


WS: [ \t\r\n]+ -> skip;
EOL: ';';
NUMBER: ([0-9]*[.])?[1-9][0-9]*;
OP_ADD: ('+'|'-');
OP_MUL: ('*'|'/');
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
OP_PWR: '^';
