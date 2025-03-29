grammar CalculatorLang;

options {
  language = Java;
}

start returns [ast.Node node]
  : ( L=line { $node = $L.node; } )*;

line returns [ast.Node node]
  : E=expr { $node = $E.node; } EOL
  ;

expr returns [ast.Node node]
  : ADD=expr_add { $node = $ADD.node; }
  ;

expr_add returns [ast.Node node]
  : MUL=expr_mul { $node = $MUL.node; }
    ( OP_ADD MUL=expr_mul { $node = ast.BinaryOperation.appendOperand($OP_ADD.text, $node, $MUL.node); } )*
  ;

expr_mul returns [ast.Node node]
  : PWR=expr_pwr { $node = $PWR.node; }
    ( OP_MUL PWR=expr_pwr { $node = ast.BinaryOperation.appendOperand($OP_MUL.text, $node, $PWR.node); } )*
  ;

expr_pwr returns [ast.Node node]
  : T=term { $node = $T.node; }
    ( OP_PWR T=term { $node = ast.BinaryOperation.appendOperand($OP_PWR.text, $node, $T.node); } )*
  ;

term returns [ast.Node node]
  : NUMBER { $node = new ast.Constant($NUMBER.text); }
  | '(' E=expr ')' { $node = $E.node; }
  | PT=prefixed_term { $node = $PT.node; }
  ;

prefixed_term returns [ast.UnaryOperation node]
  : { CalculatorLang.initSignValue(); }
    ( OP_ADD { CalculatorLang.newSignValue($OP_ADD.text); } )+ T=term
    { $node = new ast.UnaryOperation(CalculatorLang.getSign(), $T.node); }
  ;

WS: [ \t\r\n]+ -> skip;
EOL: ';';
NUMBER: ([0-9]*[.])?[1-9][0-9]*;
OP_ADD: ('+'|'-');
OP_MUL: ('*'|'/');
OP_PWR: '^';
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
