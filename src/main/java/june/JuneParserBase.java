package june;

import com.danyazero.JuneLexer;
import com.danyazero.JuneParser;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

import java.util.HashSet;
import java.util.Set;

public abstract class JuneParserBase extends Parser
{
    private static final boolean debug = false;
    private Set<String> table = new HashSet<>();

    protected JuneParserBase(TokenStream input) {
        super(input);
    }

    protected boolean closingBracket()
    {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        var la = stream.LT(1);
        return la.getType() == JuneLexer.R_PAREN || la.getType() == JuneLexer.R_CURLY || la.getType() == Token.EOF;
    }

    public boolean isOperand() {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        var la = stream.LT(1);
        if ("err".equals(la.getText())) {
            return true;
        }
        boolean result = true;
        if (la.getType() != JuneParser.IDENTIFIER) {
            if (debug) System.out.println("isOperand Returning " + result + " for " + la);
            return result;
        }
        result = table.contains(la.getText());
        Token la2 = stream.LT(2);
        if (la2.getType() != JuneParser.DOT) {
            result = true;
            if (debug) System.out.println("isOperand Returning " + result + " for " + la);
            return result;
        }
        Token la3 = stream.LT(3);
        if (la3.getType() == JuneParser.L_PAREN) {
            result = true;
            if (debug) System.out.println("isOperand Returning " + result + " for " + la);
            return result;
        }
        if (debug) System.out.println("isOperand Returning " + result + " for " + la);
        return result;
    }

    public boolean isMethodExpr() {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        Token la = stream.LT(1);
        boolean result = true;

        if (la.getType() == JuneParser.STAR) {
            if (debug) System.out.println("isMethodExpr Returning " + result + " for " + la);
            return result;
        }

        if (la.getType() != JuneParser.IDENTIFIER) {
            result = false;
            if (debug) System.out.println("isMethodExpr Returning " + result + " for " + la);
            return result;
        }

        result = !table.contains(la.getText());
        if (debug) System.out.println("isMethodExpr Returning " + result + " for " + la);
        return result;
    }

    protected boolean isConversion()
    {
        BufferedTokenStream stream = (BufferedTokenStream)_input;
        var la = stream.LT(1);
        var result = la.getType() != JuneLexer.IDENTIFIER;
        if (debug) System.out.println("isConversion Returning " + result + " for " + la);
        return result;
    }
}
