package ast;

import models.Polynom;

public class Constant implements Node {
   public final Polynom value;

   public Constant(double value) {
      this.value = Polynom.fromNumber(value);
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
