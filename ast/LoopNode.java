package ast;

import java.util.ArrayList;
import java.util.List;

import models.Polynom;

public class LoopNode implements Node {
   private final List<Node> preOps;
   private final Node condition;
   private final List<Node> endOps;
   private final StartNode node;
   private Polynom lastValue;

   public LoopNode(Node condition, StartNode node) {
      this.condition = condition;
      this.node = node;
      this.preOps = new ArrayList<>();
      this.endOps = new ArrayList<>();
   }

   public LoopNode(List<Node> preOps, Node condition, List<Node> endOps, StartNode node) {
      this.condition = condition;
      this.node = node;
      this.endOps = endOps;
      this.preOps = preOps;
   }

   @Override
   public void execute() {
      for (executeAll(preOps); getConditionValue(); executeAll(endOps)) {
         node.execute();
         lastValue = node.getValue();
      }
   }

   @Override
   public Polynom getValue() {
      return lastValue;
   }

   @Override
   public String toString() {
      return String.format("Loop((%s) ?(%s) (%s), %s)", joinNodeStrings(preOps), condition, joinNodeStrings(endOps),
            node);
   }

   private boolean getConditionValue() {
      var conditionValue = condition.getValue();
      boolean conditionValueBool = !conditionValue.isNumber() || conditionValue.asNumber() != 0;
      return conditionValueBool;
   }

   private void executeAll(List<Node> nodes) {
      nodes.forEach(node -> {
         node.execute();
      });
   }

   private String joinNodeStrings(List<Node> nodes) {
      var sb = new StringBuilder();
      nodes.forEach(node -> {
         sb.append(node.toString()).append(", ");
      });
      sb.delete(sb.length() - 2, sb.length());
      return sb.toString();
   }
}
