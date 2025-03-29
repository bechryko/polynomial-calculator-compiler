import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.*;

public class CalculatorLang {
    private static final Map<String, Double> M = new HashMap<>();
    private static final Stack<Double> pwrStack = new Stack<>();
    private static String sign = "+";

    public static void main(String[] args) throws IOException {
        var lexer = new CalculatorLangLexer(new ANTLRFileStream(args[0]));
        var tokens = new CommonTokenStream(lexer);
        var parser = new CalculatorLangParser(tokens);
        var startCtx = parser.start();

        // System.out.println(startCtx.toStringTree(parser));
        System.out.println(startCtx.node.toString());
        // System.out.println(savedVariableMapToString());
    }

    public static String interpolateVarKey(String prefix, String suffix, double value) {
        var sb = new StringBuilder(prefix);
        sb.deleteCharAt(sb.toString().length() - 1);
        String parsedValue = value % 1 == 0 ? "" + ((int)value) : "" + value;
        sb.append(parsedValue);
        sb.append(suffix.substring(1));
        return sb.toString();
    }

    public static double setVarValue(String varName, double value) {
        M.put(varName, value);
        return value;
    }

    public static double getVarValue(String varName) {
        Double value = M.get(varName);
        if(value == null) {
            return 0;
        }
        return value;
    }

    public static void initStack(double firstValue) {
        pwrStack.clear();
        appendToStack(firstValue);
    }

    public static void appendToStack(double value) {
        pwrStack.add(value);
    }

    public static double getStackValue() {
        double value = pwrStack.pop();
        while(!pwrStack.empty()) {
            value = Math.pow(pwrStack.pop(), value);
        }
        return value;
    }

    public static void initSignValue() {
        sign = "+";
    }

    public static void newSignValue(String newSign) {
        if("-".equals(newSign)) {
            sign = "+".equals(sign) ? "-" : "+";
        }
    }

    public static String getSign() {
        return sign;
    }

    private static String savedVariableMapToString() {
        var sb = new StringBuilder("Saved variables: { ");
        for(var e : M.entrySet()) {
            sb.append(e.getKey());
            sb.append(": ");
            sb.append(e.getValue());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.toString().length() - 2);
        sb.append("}");
        return sb.toString();
    }
}
