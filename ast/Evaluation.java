package ast;

import models.Polynom;

public class Evaluation implements Node {
   private final Node node;
   private final Node evalPlaceNode;

   public Evaluation(Node node, Node evalPlaceNode) {
      this.node = node;
      this.evalPlaceNode = evalPlaceNode;
   }

   @Override
   public void execute() {
      node.execute();
   }

   @Override
   public Polynom getValue() {
      return Polynom.fromNumber(node.getValue().evaluateAt(getEvalPlace()));
   }

   @Override
   public String toString() {
      return String.format("Evaluation(%s at %s)", node.toString(), evalPlaceNode.toString());
   }

   private double getEvalPlace() {
      var value = evalPlaceNode.getValue();
      if (!value.isNumber()) {
         throw new RuntimeException(
               String.format("Evaluation place can only be a number. %s is not a number.", value.toString()));
      }

      return value.asNumber();
   }
}
