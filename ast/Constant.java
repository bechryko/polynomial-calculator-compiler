package ast;

import models.Polynom;

public class Constant implements EvaluatableNode {
   public final Polynom value;

   public Constant(Polynom polynom) {
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
