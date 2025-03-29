package utils;

import java.util.Map;
import java.util.TreeMap;

import models.Polynom;

public class PolynomBuilder {
   private final Map<Integer, Double> coefficientMap = new TreeMap<>();

   public PolynomBuilder addCoefficient(int power, double coefficient) {
      coefficientMap.put(power, coefficient);
      return this;
   }

   public PolynomBuilder incrementCoefficient(int power, double coefficient) {
      Double previousValue = coefficientMap.get(power);
      double primitivePreviousValue = previousValue == null ? 0 : previousValue;
      return addCoefficient(power, primitivePreviousValue + coefficient);
   }

   public Polynom build() {
      return new Polynom(coefficientMap);
   }
}
