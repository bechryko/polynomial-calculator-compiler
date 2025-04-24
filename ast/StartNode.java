package ast;

import java.util.ArrayList;
import java.util.List;

import models.Polynom;

public class StartNode implements Node {
   private final List<Node> lines = new ArrayList<>();

   public final void addChild(Node child) {
      lines.add(child);
   }

   @Override
   public Polynom getValue() {
      var last = lines.getLast();
      if (last instanceof EvaluatableNode evaluatableNode) {
         return evaluatableNode.getValue();
      }
      return null;
   }

   @Override
   public void execute() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String toString() {
      var sb = new StringBuilder("### START ###");
      sb.append("\n");
      for (var line : lines) {
         sb.append(line.toString()).append("\n");
      }
      sb.append("### END ###");
      return sb.toString();
   }
}
