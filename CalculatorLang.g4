grammar CalculatorLang;

options {
    language = Java;
}

start returns [double value]
  : ( L=line { $value = $L.value; } )*;

line returns [double value]
  : E=expr { $value = $E.value; } EOL
  | V=var ASSIGN L=line { $value = CalculatorLang.setVarValue($V.key, $L.value); } EOL
  ;

expr returns [double value]
  : ADD=expr_add { $value = $ADD.value; }
  ;

expr_add returns [double value]
  : MUL=expr_mul { $value = $MUL.value; } ( OP_ADD MUL=expr_mul { $value += $MUL.value; } )*
  ;

expr_mul returns [double value]
  : PWR=expr_pwr { $value = $PWR.value; } ( OP_MUL PWR=expr_pwr { $value *= $PWR.value; } )*
  ;

expr_pwr returns [double value]
  : T=term { CalculatorLang.initStack($T.value); } ( OP_PWR T=term { CalculatorLang.appendToStack($T.value); } )* { $value = CalculatorLang.getStackValue(); }
  ;

term returns [double value]
  : NUMBER { $value = Double.parseDouble("0" + $NUMBER.text); }
  | V=var { $value = CalculatorLang.getVarValue($V.key); }
  | '(' E=expr ')' { $value = $E.value; System.out.println($value); }
  | PT=prefixed_term { $value = $PT.value; }
  ;

prefixed_term returns [double value]
  : { CalculatorLang.initSignValue(); } ( OP_ADD { CalculatorLang.newSignValue($OP_ADD.text); } )+ T=term { $value = CalculatorLang.getSignedValue($T.value); }
  ;

var returns [String key]
  : VAR { $key = $VAR.text; }
  | VAR_INTERPOLATE_PREFIX L=line VAR_INTERPOLATE_SUFFIX { $key = CalculatorLang.interpolateVarKey($VAR_INTERPOLATE_PREFIX.text, $VAR_INTERPOLATE_SUFFIX.text, $L.value); }
  ;

WS: [ \t\r\n]+ -> skip;
EOL: ';';
NUMBER: ([0-9]*[.])?[1-9][0-9]*;
OP_ADD: ('+'|'-');
OP_MUL: ('*'|'/');
OP_PWR: '^';
VAR_PREFIX: 'M';
VAR: VAR_PREFIX[0-9]*;
VAR_INTERPOLATE_PREFIX: VAR_PREFIX[0-9]*'{';
VAR_INTERPOLATE_SUFFIX: '}'[0-9]*;
ASSIGN: '=';
PAREN_OPENING: '(';
PAREN_CLOSING: ')';
