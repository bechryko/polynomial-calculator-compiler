grammar CalculatorLang;

options {
    language = Java;
}

start: line*;

line
  : expr EOL
  | VAR ASSIGN line EOL
  ;

expr: expr_add;

expr_add
  : expr_mul ( OP_ADD expr_mul )*
  ;

expr_mul
  : expr_pwr ( OP_MUL expr_pwr )*
  ;

expr_pwr
  : term ( OP_PWR term )*
  ;

term
  : NUMBER
  | VAR
  | PAREN_OPENING expr PAREN_CLOSING
  | prefixed_term
  ;

prefixed_term
  : ( OP_ADD )+ term
  ;

WS: [ \t\r\n]+ -> skip;
EOL: ';';
NUMBER: ([0-9]*[.])?[1-9][0-9]*;
OP_ADD: ('+'|'-');
OP_MUL: ('*'|'/');
OP_PWR: '^';
VAR: 'M';
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';

