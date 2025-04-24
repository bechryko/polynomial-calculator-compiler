package ast;

import models.Polynom;
import models.UnaryOperator;

public class UnaryOperation implements Node {
   public final UnaryOperator operator;
   public final Node operand;

   public UnaryOperation(String operator, Node operand) {
      this.operator = UnaryOperator.parseString(operator);
      this.operand = operand;
   }

   @Override
   public void execute() {
      operand.execute();
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
