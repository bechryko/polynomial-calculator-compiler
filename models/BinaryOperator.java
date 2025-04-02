package models;

public enum BinaryOperator {
   ADDITION("+", (p1, p2) -> p1.add(p2)),
   SUBTRACTION("-", (p1, p2) -> p1.subtract(p2)),
   MULTIPLICATION("*", (p1, p2) -> p1.multiply(p2)),
   DIVISION("/", (p1, p2) -> p1.divide(p2, false)),
   REMAINDER("%", (p1, p2) -> p1.divide(p2, true));

   public final String value;
   public final BinaryOperatorAction action;

   BinaryOperator(String value, BinaryOperatorAction action) {
      this.value = value;
      this.action = action;
   }

   public static BinaryOperator parseString(String str) {
      for (var op : values()) {
         if (op.value.equals(str)) {
            return op;
         }
      }
      return null;
   }

   @Override
   public String toString() {
      return value;
   }

   public interface BinaryOperatorAction {
      public Polynom execute(Polynom lhs, Polynom rhs);
   }
}
