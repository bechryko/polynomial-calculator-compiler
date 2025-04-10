package ast;

import utils.VariableHandler;

public class VariableDeclaration implements ExecutableNode {
   private final String type;
   private final String name;
   private final VariableHandler handler;

   public VariableDeclaration(String type, String name, VariableHandler handler) {
      this.type = type;
      this.name = name;
      this.handler = handler;
   }

   @Override
   public void execute() {
      handler.declareVariable(name, type);
   }

   @Override
   public String toString() {
      return String.format("VariableDeclaration(%s (%s))", name, type);
   }
}
