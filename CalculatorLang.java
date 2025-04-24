import java.io.IOException;

import org.antlr.v4.runtime.*;

public class CalculatorLang {
    public static void main(String[] args) throws IOException {
        var lexer = new CalculatorLangLexer(new ANTLRFileStream(args[0]));
        var tokens = new CommonTokenStream(lexer);
        var parser = new CalculatorLangParser(tokens);
        var startCtx = parser.start();

        // System.out.println(startCtx.toStringTree(parser));
        System.out.println(startCtx.node.toString());
        startCtx.node.execute();
        System.out.println(startCtx.node.getValue());
    }
}
