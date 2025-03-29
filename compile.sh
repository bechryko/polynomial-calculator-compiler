#!/bin/bash

grammar_name=""
antlr_package="antlr-4.11.1-complete.jar"
input_file=""

if ! [ -e $antlr_package ]
then
  sh scripts/install.sh $antlr_package
fi

if [ "$1" = "gui" ];
then
  java -cp .:$antlr_package org.antlr.v4.gui.Interpreter $grammar_name.g4 start -gui inputs/$input_file
else
  java -jar $antlr_package $grammar_name.g4
  javac -cp .:$antlr_package *.java
  java -cp .:$antlr_package $grammar_name inputs/$input_file
  sh scripts/clear.sh $grammar_name
fi
