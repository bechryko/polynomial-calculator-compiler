package ast;

import models.Polynom;

public class ConditionNode implements Node {
   private final Node condition;
   private final StartNode trueCaseNode;
   private final StartNode falseCaseNode;

   public ConditionNode(Node condition, StartNode trueCaseNode, StartNode falseCaseNode) {
      this.condition = condition;
      this.trueCaseNode = trueCaseNode;
      this.falseCaseNode = falseCaseNode;
   }

   @Override
   public void execute() {
      condition.execute();
      getConditionalNode().execute();
   }

   @Override
   public Polynom getValue() {
      return getConditionalNode().getValue();
   }

   private boolean getConditionValue() {
      var conditionValue = condition.getValue();
      boolean conditionValueBool = !conditionValue.isNumber() || conditionValue.asNumber() != 0;
      return conditionValueBool;
   }

   private StartNode getConditionalNode() {
      return getConditionValue() ? trueCaseNode : falseCaseNode;
   }

   @Override
   public String toString() {
      return String.format("Condition(%s \n? %s \n: %s)", condition, trueCaseNode, falseCaseNode);
   }
}
