package ast;

public abstract class Node {
   public enum NodeType {
      INVALID,
      CONST,
      BINARY_OP,
      UNARY_OP,
      MEMORY_ACCESS,
      MEMORY_ACCESS_INTERPOLATED,
      MEMORY_ASSIGNMENT
   }

   public NodeType type = NodeType.INVALID;

   public Node(NodeType type) {
      this.type = type;
   }
}
