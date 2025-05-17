# Polynomial Calculator

Project for the MSc course Compilers in SZTE.

Made by Kozma Kristóf (UQ13LD)

## Tasks

 - A polynomial can be given in the form `<a_n x^n + ... + a_1 x + a_0>`, where
    - `x` is a language element
    - `n` can be any integer
    - `a_n`, ... `a_0` are real coefficient values (literals)
    - the member with coefficient 0 can be omitted
    - the notation `x^1` can be written as `x`
    - and the notation `x^0` can be omitted (in which case only the coefficient `a_0` is used in the polynomial).
 - Polynomial variables can be given a value in the form `pi = <expression>`, where
    - `pi` is a variable declared as `polynom p1, p2, `...` pi, `...` pn;`,
    - `<expression>` is a polynomial expression.
 - The following operations can be performed on polynomials:
    - `+`, `-`, `*`, `/`, `%` (the last two are the quotient and remainder of a residual polynomial division, both polynomials),
    - using parentheses `(` and `)`, and
    - evaluation in `<expression>[X]`, where `X` can be any real number, and
    - the evaluation operation has a high priority.
 - The program's `show` statement prints the value of the specified expression (if it is a polynomial, then the polynomial).
 - The program can have real type variables (`number`) which can be used as coefficients in polynomials.

### Task modifications

 - The keywords for declaring variables are capitalized (`Polynom` and `Number`).
 - The `show` statement doesn't require parens around its operand.

### Bonus tasks

 - Polynomials can be prefixed with `+` and `-` to leave unchanged or negate a polynomial respectively.
 - Assignments have value, which is the value of their right-hand side. They can be used as a value as well.
 - Numbers are also polynomials, and polynomials whose only member's power is 0 are numbers.
    - All polynomial operations are defined for numbers as well.
    - The type-checking (where only numbers can be accepted) is done during runtime.
 - The program can have conditions in the syntax `if (<condition>) { <lines of code> }`, which can be used as a standalone expression, or inline as a value. The value is the block's last line's value.
 - The program can have loops, which can be used as a standalone expression, or inline as a value. The value is the last line's value in the loop's last run.
    - It can be a "while" loop in the syntax `while (<condition>) { <lines of code> }`.
    - It can be a "for" loop in the syntax `for (<expressions>; <condition>; <expressions>) { <lines of code> }`.
    - It can be a "for-times" loop in the syntax `for (<expression> times) { <lines of code> }`, where
       - `<expression>` has to evaluate to a number,
       - the loop will repeat `<expression>` times.
 - The program can have comments, from a `//` to the end of the line.
 - Running the program with the `--print-string-tree` flag will log the ANTLR parse tree to the console, for easier debugging.
 - Running the program with the `--print-nodes` flag will log the nodes and blocks to the console, for easier debugging.

## Inputs

The `inputs` folder contains example inputs for the implemented features. In the `example-inputs` folder there are the 2 inputs which can be found in the [okt.inf](https://okt.inf.szte.hu/fordprog/gyakorlat/projekt/#pr04-polinom-szamologep).
