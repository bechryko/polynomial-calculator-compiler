package utils;

public class SignBuilder {
   private String sign = "+";

   public SignBuilder() {
   }

   public SignBuilder multiplySign(String mulSign) {
      if ("-".equals(mulSign)) {
         swapSign();
      }
      return this;
   }

   public String build() {
      return sign;
   }

   private void swapSign() {
      sign = "+".equals(sign) ? "-" : "+";
   }
}
