package ast;

import models.Polynom;

public class ShowNode implements Node {
   private final Node node;

   public ShowNode(Node node) {
      this.node = node;
   }

   @Override
   public void execute() {
      node.execute();
      System.out.println("[show] " + node.getValue());
   }

   @Override
   public Polynom getValue() {
      return node.getValue();
   }

   @Override
   public String toString() {
      return String.format("Show(%s)", node.toString());
   }
}
