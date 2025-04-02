package ast;

import models.Polynom;
import models.UnaryOperator;

public class UnaryOperation implements EvaluatableNode {
   public final UnaryOperator operator;
   public final EvaluatableNode operand;

   public UnaryOperation(String operator, EvaluatableNode operand) {
      this.operator = UnaryOperator.parseString(operator);
      this.operand = operand;
   }

   @Override
   public Polynom getValue() {
      return operator.action.execute(operand.getValue());
   }

   @Override
   public String toString() {
      return String.format("UnaryOperation(%s %s)", operator, operand.toString());
   }
}
