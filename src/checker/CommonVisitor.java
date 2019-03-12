package checker;

import checker.util.Info;
import grammar.antlr.CPP14BaseVisitor;
import grammar.antlr.CPP14Parser;
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

    @Override
    public T visitUnnamednamespacedefinition(CPP14Parser.UnnamednamespacedefinitionContext ctx) {
        namespace.push("#");
        T result = super.visitUnnamednamespacedefinition(ctx);
        namespace.pop();
        return result;
    }

    @Override
    public T visitOriginalnamespacedefinition(CPP14Parser.OriginalnamespacedefinitionContext ctx) {
        namespace.push(ctx.Identifier().getText());
        T result =  super.visitOriginalnamespacedefinition(ctx);
        namespace.pop();
        return result;
    }

    @Override
    public T visitExtensionnamespacedefinition(CPP14Parser.ExtensionnamespacedefinitionContext ctx) {
        namespace.push(Info.getText(ctx.originalnamespacename()));
        T result =  super.visitExtensionnamespacedefinition(ctx);
        namespace.pop();
        return result;
    }

    public String getFullText() {
        return reWriter.getText();
    }
}
