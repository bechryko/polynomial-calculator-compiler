import java.io.IOException;
import java.util.Stack;

import org.antlr.v4.runtime.*;

public class CalculatorLang {
    private static final Stack<Double> pwrStack = new Stack<>();
    private static String sign = "+";

    public static void main(String[] args) throws IOException {
        var lexer = new CalculatorLangLexer(new ANTLRFileStream(args[0]));
        var tokens = new CommonTokenStream(lexer);
        var parser = new CalculatorLangParser(tokens);
        var startCtx = parser.start();

        // System.out.println(startCtx.toStringTree(parser));
        System.out.println(startCtx.node.toString());
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
        while (!pwrStack.empty()) {
            value = Math.pow(pwrStack.pop(), value);
        }
        return value;
    }

    public static void initSignValue() {
        sign = "+";
    }

    public static void newSignValue(String newSign) {
        if ("-".equals(newSign)) {
            sign = "+".equals(sign) ? "-" : "+";
        }
    }

    public static String getSign() {
        return sign;
    }
}
