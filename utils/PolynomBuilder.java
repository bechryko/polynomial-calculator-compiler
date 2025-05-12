package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Polynom;
import models.UnaryOperator;

public class PolynomBuilder {
   private final List<PolynomBuildMember> members = new ArrayList<>();

   public PolynomBuilder addCoefficient(PolynomBuildMember member, String prefix) {
      boolean isNegated = UnaryOperator.parseString(prefix) == UnaryOperator.NEGATIVE;
      if (isNegated) {
         member.negate();
      }
      members.add(member);
      return this;
   }

   public PolynomBuilder addCoefficient(PolynomBuildMember member) {
      return addCoefficient(member, null);
   }

   public PolynomBuilder addCoefficient(int power, double coefficient) {
      var member = new PolynomBuildMember(coefficient, null, power);
      return addCoefficient(member);
   }

   public PolynomBuilder incrementCoefficient(int power, double coefficient) {
      return addCoefficient(power, coefficient);
   }

   public Polynom build(VariableHandler handler) {
      return new Polynom(createCoefficientMap(handler));
   }

   public Polynom build() {
      return build(null);
   }

   private Map<Integer, Double> createCoefficientMap(VariableHandler handler) {
      var coefficientMap = new TreeMap<Integer, Double>();

      members.forEach(member -> {
         var power = member.getPower();
         var previousCoefficient = coefficientMap.get(power);
         if (previousCoefficient == null) {
            coefficientMap.put(power, member.getCoefficient(handler));
         } else {
            coefficientMap.put(power, previousCoefficient + member.getCoefficient(handler));
         }
      });

      return coefficientMap;
   }

   public static class PolynomBuildMember {
      private final double coefficient;
      private final String coefficientVarName;
      private final int power;
      private boolean isNegated = false;

      public PolynomBuildMember(double coefficient, String coefficientVarName, int power) {
         this.coefficient = coefficient;
         this.coefficientVarName = coefficientVarName;
         this.power = power;
      }

      public PolynomBuildMember(double coefficient, int power) {
         this(coefficient, null, power);
      }

      public void negate() {
         isNegated = !isNegated;
      }

      public double getCoefficient(VariableHandler handler) {
         return getBaseCoefficient(handler) * (isNegated ? -1 : 1);
      }

      public int getPower() {
         return power;
      }

      private double getBaseCoefficient(VariableHandler handler) {
         if (coefficientVarName == null) {
            return coefficient;
         } else {
            var value = handler.accessVariableValue(coefficientVarName);
            if (!value.isNumber()) {
               throw new RuntimeException(String.format(
                     "Polynom coefficients can only be numbers. Variable %s's value is %s, which is not a number.",
                     coefficientVarName, value.toString()));
            }
            return value.asNumber();
         }
      }
   }
}
