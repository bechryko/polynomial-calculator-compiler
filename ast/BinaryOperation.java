package ast;

import java.util.ArrayList;
import java.util.List;

public class BinaryOperation extends Node {
   public final String operator;
   public final List<Node> operands = new ArrayList<>();

   public static BinaryOperation appendOperand(String operator, Node previous, Node newChild) {
      if (previous instanceof BinaryOperation) {
         BinaryOperation parent = (BinaryOperation) previous;
         if (!operator.equals(parent.operator)) {
            throw new RuntimeException("Operation mismatch");
         }
         parent.addOperandNode(newChild);
         return parent;
      }

      var binParent = new BinaryOperation(operator, previous);
      binParent.addOperandNode(newChild);
      return binParent;
   }

   private BinaryOperation(String operator, Node firstNode) {
      super(Node.NodeType.BINARY_OP);
      this.operator = operator;
      addOperandNode(firstNode);
   }

   public final void addOperandNode(Node node) {
      operands.add(node);
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
