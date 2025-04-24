package ast;

import models.Polynom;

public class LoopNode implements Node {
   private final Node preOp;
   private final Node condition;
   private final Node endOp;
   private final StartNode node;
   private Polynom lastValue;

   public LoopNode(Node condition, StartNode node) {
      this.condition = condition;
      this.node = node;
      this.preOp = null;
      this.endOp = null;
   }

   public LoopNode(Node preOp, Node condition, Node endOp, StartNode node) {
      this.condition = condition;
      this.node = node;
      this.endOp = endOp;
      this.preOp = preOp;
   }

   @Override
   public void execute() {
      for (executeIfNotNull(preOp); getConditionValue(); executeIfNotNull(endOp)) {
         node.execute();
         lastValue = node.getValue();
      }
   }

   @Override
   public Polynom getValue() {
      return lastValue;
   }

   private boolean getConditionValue() {
      var conditionValue = condition.getValue();
      boolean conditionValueBool = !conditionValue.isNumber() || conditionValue.asNumber() != 0;
      return conditionValueBool;
   }

   private void executeIfNotNull(Node node) {
      if (node != null) {
         node.execute();
      }
   }

   @Override
   public String toString() {
      return String.format("Loop(%s -> %s, %s)", preOp != null ? preOp : "-", condition, node);
   }
}
