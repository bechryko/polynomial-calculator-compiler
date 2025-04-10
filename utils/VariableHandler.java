package utils;

import java.util.HashMap;
import java.util.Map;
import models.Polynom;
import models.Variable;

public class VariableHandler {
   private final Map<String, Variable> variableMap = new HashMap<>();

   public void declareVariable(String name, String type) {
      variableMap.put(name, new Variable(type, null));
   }

   public Polynom assignValue(String variableName, Polynom value) {
      var variable = getVariableByName(variableName);
      if (variable.type == Variable.Type.NUMBER && !value.isNumber()) {
         throw new RuntimeException(
               String.format(
                     "Variable '%s' has type of Number, but value '%s' is a Polynom!",
                     variableName,
                     value.toString()));
      }
      variable.value = value;
      return value;
   }

   public Polynom accessVariableValue(String name) {
      var variable = getVariableByName(name);
      if (variable.value == null) {
         throw new RuntimeException(String.format("Variable '%s' has undefined value!", name));
      }
      return variable.value;
   }

   public void print() {
      for (var entry : variableMap.entrySet()) {
         System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
      }
   }

   private Variable getVariableByName(String name) {
      var variable = variableMap.get(name);
      if (variable == null) {
         throw new RuntimeException(String.format("Variable '%s' is not declared!", name));
      }
      return variable;
   }
}
