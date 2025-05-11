package ast;

import java.util.List;

import models.Polynom;
import utils.VariableHandler;

public class VariableDeclaration implements Node {
   private final String type;
   private final List<String> names;
   private final VariableHandler handler;

   public VariableDeclaration(String type, List<String> names, VariableHandler handler) {
      this.type = type;
      this.names = names;
      this.handler = handler;
   }

   @Override
   public void execute() {
      names.forEach(name -> {
         handler.declareVariable(name, type);
      });
   }

   @Override
   public Polynom getValue() {
      execute();
      return null;
   }

   @Override
   public String toString() {
      return String.format("VariableDeclaration(%s (%s))", joinNames(), type);
   }

   private String joinNames() {
      var sb = new StringBuilder();
      for (var name : names) {
         sb.append(name).append(", ");
      }
      sb.delete(sb.length() - 2, sb.length());
      return sb.toString();
   }
}
