package ast;

import models.Polynom;
import utils.VariableHandler;

public class VariableDeclaration implements Node {
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
   public Polynom getValue() {
      execute();
      return null;
   }

   @Override
   public String toString() {
      return String.format("VariableDeclaration(%s (%s))", name, type);
   }
}
