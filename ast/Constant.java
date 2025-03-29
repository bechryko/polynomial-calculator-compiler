package ast;

public class Constant extends Node {
   public final double value;

   public Constant(String text) {
      super(Node.NodeType.CONST);
      this.value = Double.parseDouble("0" + text);
   }

   @Override
   public String toString() {
      return String.format("Constant(%s)", value);
   }
}
