#!/bin/bash

rm -rf $1.class
rm -rf $1.tokens
rm -rf $1.interp
rm -rf $1BaseListener.*
rm -rf $1Lexer.*
rm -rf $1Listener.*
rm -rf $1Parser.*
rm -rf $1Parser\$*
rm -rf ast/*.class
rm -rf models/*.class
rm -rf utils/*.class
