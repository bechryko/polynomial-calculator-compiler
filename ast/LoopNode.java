package ast;

import java.util.ArrayList;
import java.util.List;

import models.Polynom;

public class LoopNode implements Node {
   private final List<Node> preOps;
   private final Node condition;
   private final Node times;
   private final List<Node> endOps;
   private final StartNode node;
   private Polynom lastValue;

   public LoopNode(Node condition, StartNode node) {
      this.condition = condition;
      this.times = null;
      this.node = node;
      this.preOps = new ArrayList<>();
      this.endOps = new ArrayList<>();
   }

   public LoopNode(Node times, StartNode node, boolean constructorSignatureParam) {
      this.condition = null;
      this.times = times;
      this.node = node;
      this.preOps = new ArrayList<>();
      this.endOps = new ArrayList<>();
   }

   public LoopNode(List<Node> preOps, Node condition, List<Node> endOps, StartNode node) {
      this.condition = condition;
      this.times = null;
      this.node = node;
      this.endOps = endOps;
      this.preOps = preOps;
   }

   @Override
   public void execute() {
      if (times == null) {
         executeBasicLoop();
      } else {
         var value = times.getValue();
         if (!value.isNumber()) {
            throw new RuntimeException(
                  String.format("A loop can repeat a fixed number of times. %s is not a number.", value.toString()));
         }
         executeTimesLoop((int) Math.floor(value.asNumber()));
      }
   }

   @Override
   public Polynom getValue() {
      return lastValue;
   }

   @Override
   public String toString() {
      if (times != null) {
         return String.format("Loop(%s times, %s)", times, node);
      }
      if (preOps.isEmpty() && endOps.isEmpty()) {
         return String.format("Loop(%s ? %s)", condition, node);
      }
      return String.format("Loop((%s) ?(%s) (%s), %s)", joinNodeStrings(preOps), condition, joinNodeStrings(endOps),
            node);
   }

   private void executeBasicLoop() {
      for (executeAll(preOps); getConditionValue(); executeAll(endOps)) {
         node.execute();
         lastValue = node.getValue();
      }
   }

   private void executeTimesLoop(int times) {
      for (int loopVar = 0; loopVar < times; loopVar++) {
         node.execute();
         lastValue = node.getValue();
      }
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
      if (nodes.isEmpty()) {
         return "";
      }

      var sb = new StringBuilder();
      nodes.forEach(node -> {
         sb.append(node.toString()).append(", ");
      });
      sb.delete(sb.length() - 2, sb.length());
      return sb.toString();
   }
}
