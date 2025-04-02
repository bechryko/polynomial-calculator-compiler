package ast;

import java.util.ArrayList;
import java.util.List;
import models.BinaryOperator;
import models.Polynom;

public class BinaryOperation implements EvaluatableNode {
   public final BinaryOperator operator;
   public final List<EvaluatableNode> operands = new ArrayList<>();

   public static BinaryOperation appendOperand(String operator, EvaluatableNode previous, EvaluatableNode newChild) {
      if (previous instanceof BinaryOperation parent) {
         if (!operator.equals(parent.operator.value)) {
            throw new RuntimeException("Operation mismatch");
         }
         parent.addOperandNode(newChild);
         return parent;
      }

      var binParent = new BinaryOperation(BinaryOperator.parseString(operator), previous);
      binParent.addOperandNode(newChild);
      return binParent;
   }

   private BinaryOperation(BinaryOperator operator, EvaluatableNode firstNode) {
      this.operator = operator;
      addOperandNode(firstNode);
   }

   public final void addOperandNode(EvaluatableNode node) {
      operands.add(node);
   }

   @Override
   public Polynom getValue() {
      Polynom result = null;
      for (var operand : operands) {
         if (result == null) {
            result = operand.getValue();
            continue;
         }

         result = operator.action.execute(result, operand.getValue());
      }

      return result;
   }

   @Override
   public String toString() {
      var sb = new StringBuilder("BinaryOperation");
      sb.append("(");
      sb.append(operator);
      sb.append(", ");
      for (var node : operands) {
         sb.append(node == null ? "null" : node.toString());
         sb.append(", ");
      }
      sb.deleteCharAt(sb.toString().length() - 2);
      sb.append(")");
      return sb.toString();
   }
}
