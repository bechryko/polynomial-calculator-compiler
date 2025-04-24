# Polynomial Calculator
Project for the MSc course Compilers in SZTE.

## Tasks

 - A polynomial can be given in the form `<a_n x^n + ... + a_1 x + a_0>`, where
    - `x` is a language element
    - `n` can be any integer
    - `a_n`, ... `a_0` are real coefficient values (literals)
    - the member with coefficient 0 can be omitted
    - the notation `x^1` can be written as `x`
    - and the notation `x^0` can be omitted (in which case only the coefficient `a_0` is used in the polynomial).
 - Polynomial variables can be given a value in the form `p = <expression>`, where
    - `p` is a variable declared as `polynom p;`,
    - `<expression>` is a polynomial expression.
 - The following operations can be performed on polynomials:
    - `+`, `-`, `*`, `/`, `%` (the last two are the quotient and remainder of a residual polynomial division, both polynomials),
    - using parentheses `(` and `)`, and
    - evaluation in `<expression>[X]`, where `X` can be any real number, and
    - the evaluation operation has a high priority.
 - The program's `show` statement prints the value of the specified expression (if it is a polynomial, then the polynomial).
 - The program can have real type variables (`number`) which can be used as coefficients in polynomials.

## Bonus tasks

 - The program can have conditions in the syntax `if (<condition>) { <lines of code> }`, which can be used as a standalone expression, or inline as a value. The value is the block's last line's value.
 - The program can have loops, which can be used as a standalone expression, or inline as a value. The value is the last line's value in the loop's last run.
    - It can be a "while" loop in the syntax `while (<condition>) { <lines of code> }`.
    - It can be a "for" loop in the syntax `for (<expression>; <condition>; <expression>) { <lines of code> }`.
