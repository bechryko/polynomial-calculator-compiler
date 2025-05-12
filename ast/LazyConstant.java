package ast;

import models.Polynom;
import utils.PolynomBuilder;
import utils.VariableHandler;

public class LazyConstant implements Node {
   private final PolynomBuilder builder;
   private final VariableHandler handler;
   private Polynom value;

   public LazyConstant(PolynomBuilder builder, VariableHandler handler) {
      this.builder = builder;
      this.handler = handler;
   }

   @Override
   public void execute() {
      value = builder.build(handler);
   }

   @Override
   public Polynom getValue() {
      return value;
   }

   @Override
   public String toString() {
      return String.format("LazyConstant(%s)", valueToString());
   }

   private String valueToString() {
      if (value == null) {
         return "not executed yet";
      }
      return value.toString();
   }
}
