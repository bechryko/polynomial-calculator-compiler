package ast;

import models.Polynom;
import utils.VariableHandler;

public class VariableAssignment implements EvaluatableNode, ExecutableNode {
   private final String name;
   private final EvaluatableNode value;
   private final VariableHandler handler;

   public VariableAssignment(String name, EvaluatableNode value, VariableHandler handler) {
      this.name = name;
      this.value = value;
      this.handler = handler;
   }

   @Override
   public Polynom getValue() {
      return value.getValue();
   }

   @Override
   public void execute() {
      handler.assignValue(name, getValue());
   }

   @Override
   public String toString() {
      return String.format("VariableAssignment(%s, %s)", name, value.toString());
   }
}
