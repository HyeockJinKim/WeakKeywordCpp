package checker;

import grammar.antlr.CPP14BaseVisitor;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;

public class CommonVisitor<T> extends CPP14BaseVisitor<T> {
    TokenStreamRewriter reWriter;

    /**
     * Converts input ctx to existing code.
     * @param ctx
     * @return
     */
    protected String getText(ParserRuleContext ctx) {
        Interval interval = new Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        return ctx.start.getInputStream().getText(interval);
    }

    public String getFullText() {
        return reWriter.getText();
    }

}
