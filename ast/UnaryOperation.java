package ast;

public class UnaryOperation extends Node {
   public final String operator;
   public final Node operand;

   public UnaryOperation(String operator, Node operand) {
      super(Node.NodeType.UNARY_OP);
      this.operator = operator;
      this.operand = operand;
   }

   @Override
   public String toString() {
      return String.format("UnaryOperation(%s %s)", operator, operand.toString());
   }
}
