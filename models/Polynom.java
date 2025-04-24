package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.PolynomBuilder;

public class Polynom implements Cloneable {
   private final Map<Integer, Double> coefficientMap;

   public static Polynom fromNumber(double number) {
      return new PolynomBuilder().addCoefficient(0, number).build();
   }

   public Polynom(Map<Integer, Double> coefficientMap) {
      this.coefficientMap = coefficientMap;
      deleteUnusedKeys();
   }

   public Polynom add(Polynom rhs) {
      Set<Integer> commonKeys = new HashSet<>();
      commonKeys.addAll(coefficientMap.keySet());
      commonKeys.addAll(rhs.coefficientMap.keySet());

      var builder = new PolynomBuilder();

      for (var key : commonKeys) {
         builder.addCoefficient(key, getCoefficientAt(key) + rhs.getCoefficientAt(key));
      }

      return builder.build();
   }

   public Polynom subtract(Polynom rhs) {
      return add(rhs.negate());
   }

   public Polynom multiply(Polynom rhs) {
      var builder = new PolynomBuilder();

      for (var rhsEntry : rhs.coefficientMap.entrySet()) {
         for (var lhsEntry : coefficientMap.entrySet()) {
            builder.incrementCoefficient(rhsEntry.getKey() + lhsEntry.getKey(),
                  rhsEntry.getValue() * lhsEntry.getValue());
         }
      }

      return builder.build();
   }

   public Polynom divide(Polynom rhs, boolean returnRemainder) {
      if (rhs.isNumber() && rhs.asNumber() == 0) {
         throw new ArithmeticException("Division by 0");
      }

      Polynom remainder = copy();
      var quotientBuilder = new PolynomBuilder();

      while (remainder.deg() >= rhs.deg()) {
         Integer partialQuotientPower = remainder.deg() - rhs.deg();
         Double partialQuotientCoefficient = remainder.getMainCoefficient() / rhs.getMainCoefficient();

         quotientBuilder.addCoefficient(partialQuotientPower, partialQuotientCoefficient);
         var partialQuotient = new PolynomBuilder()
               .addCoefficient(partialQuotientPower, partialQuotientCoefficient)
               .build();
         remainder = remainder.subtract(partialQuotient.multiply(rhs));
      }

      return returnRemainder ? remainder : quotientBuilder.build();
   }

   public double evaluateAt(double x) {
      double value = 0;

      for (var entry : coefficientMap.entrySet()) {
         value += entry.getValue() * Math.pow(x, entry.getKey());
      }

      return value;
   }

   public Polynom negate() {
      var builder = new PolynomBuilder();

      for (var entry : coefficientMap.entrySet()) {
         builder.addCoefficient(entry.getKey(), -entry.getValue());
      }

      return builder.build();
   }

   public int deg() {
      int maxKey = 0;
      for (var key : coefficientMap.keySet()) {
         maxKey = Math.max(maxKey, key);
      }
      return maxKey;
   }

   public boolean isNumber() {
      var keys = coefficientMap.keySet();
      return keys.isEmpty() || (keys.size() == 1 && keys.contains(0));
   }

   public double asNumber() {
      if (coefficientMap.keySet().isEmpty()) {
         return 0;
      }
      return coefficientMap.get(0);
   }

   public Polynom copy() {
      return new Polynom(coefficientMap);
   }

   @Override
   public String toString() {
      if (isNumber()) {
         return Double.toString(asNumber());
      }

      List<Map.Entry<Integer, Double>> entries = new ArrayList<>(coefficientMap.entrySet());
      entries.sort((var entry1, var entry2) -> {
         return entry2.getKey() - entry1.getKey();
      });

      var sb = new StringBuilder("<");
      for (var entry : entries) {
         double coefficient = entry.getValue();
         if (coefficient != 1) {
            sb.append(coefficient).append(" ");
         }

         int power = entry.getKey();
         if (power == 1) {
            sb.append("x");
         } else if (power != 0) {
            sb.append(String.format("x^%d", entry.getKey()));
         } else {
            sb.delete(sb.length() - 1, sb.length());
         }

         sb.append(", ");
      }
      sb.delete(sb.length() - 2, sb.length());
      sb.append(">");

      return sb.toString();
   }

   private double getCoefficientAt(Integer key) {
      Double coefficient = coefficientMap.get(key);
      return coefficient == null ? 0 : coefficient;
   }

   private double getMainCoefficient() {
      return getCoefficientAt(deg());
   }

   private void deleteUnusedKeys() {
      for (var entry : coefficientMap.entrySet()) {
         if (entry.getValue() == 0) {
            coefficientMap.remove(entry.getKey());
         }
      }
   }
}
