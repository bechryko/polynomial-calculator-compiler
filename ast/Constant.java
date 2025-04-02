package ast;

import models.Polynom;

public class Constant extends Node {
   public final Polynom value;

   public Constant(Polynom polynom) {
      super(Node.NodeType.CONST);
      value = polynom;
   }

   public Constant(double value) {
      this(Polynom.fromNumber(value));
   }

   @Override
   public Polynom getValue() {
      return value;
   }

   @Override
   public String toString() {
      return String.format("Constant(%s)", value.toString());
   }
}
