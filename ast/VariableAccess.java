package ast;

import models.Polynom;
import utils.VariableHandler;

public class VariableAccess implements Node {
   private final String variableName;
   private final VariableHandler handler;

   public VariableAccess(String variableName, VariableHandler handler) {
      this.variableName = variableName;
      this.handler = handler;
   }

   @Override
   public Polynom getValue() {
      return handler.accessVariableValue(variableName);
   }

   @Override
   public String toString() {
      return String.format("VariableAccess(%s)", variableName);
   }
}
