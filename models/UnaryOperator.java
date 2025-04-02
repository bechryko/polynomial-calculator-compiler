package models;

public enum UnaryOperator {
   POSITIVE("+", p -> p),
   NEGATIVE("-", p -> p.negate());

   public final String value;
   public final UnaryOperatorAction action;

   UnaryOperator(String value, UnaryOperatorAction action) {
      this.value = value;
      this.action = action;
   }

   public static UnaryOperator parseString(String str) {
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

   public interface UnaryOperatorAction {
      public Polynom execute(Polynom operand);
   }
}
