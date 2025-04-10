package models;

public class Variable {
   public final Type type;
   public Polynom value;

   public Variable(String type, Polynom initialValue) {
      this.type = Type.parseString(type);
      this.value = initialValue;
   }

   public boolean hasValue() {
      return value != null;
   }

   @Override
   public String toString() {
      return String.format("%s (%s)", getValueToString(), type.text);
   }

   private String getValueToString() {
      if (value == null) {
         return "undefined";
      }
      return value.toString();
   }

   public enum Type {
      POLYNOM("Polynom"),
      NUMBER("Number");

      private final String text;

      Type(String text) {
         this.text = text;
      }

      public static Type parseString(String str) {
         for (var types : values()) {
            if (types.text.equals(str)) {
               return types;
            }
         }
         return null;
      }
   }
}
