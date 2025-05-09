package ast;

import models.Polynom;
import utils.VariableHandler;

public class VariableAssignment implements Node {
   private final String name;
   private final Node value;
   private final VariableHandler handler;

   public VariableAssignment(String name, Node value, VariableHandler handler) {
      this.name = name;
      this.value = value;
      this.handler = handler;
   }

   @Override
   public void execute() {
      value.execute();
      handler.assignValue(name, value.getValue());
   }

   @Override
   public Polynom getValue() {
      return handler.accessVariableValue(name);
   }

   @Override
   public String toString() {
      return String.format("VariableAssignment(%s, %s)", name, value.toString());
   }
}
