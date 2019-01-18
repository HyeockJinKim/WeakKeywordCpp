package checker;

import grammar.antlr.CPP14BaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;

import java.util.Stack;

public class CommonVisitor<T> extends CPP14BaseVisitor<T> {
    protected static Stack<String> namespace;
    protected TokenStreamRewriter reWriter;
    // util 객체 !
    static {
        namespace = new Stack<>();
    }

    public String getFullText() {
        return reWriter.getText();
    }
}
