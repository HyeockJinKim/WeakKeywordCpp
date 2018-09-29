package checker;

import grammar.antlr.CPP14BaseVisitor;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.HashSet;
import java.util.Set;

public class StaticCastVisitor<T> extends CommonVisitor<T> {
    private Set<CppClass> classSet;

    public StaticCastVisitor(CommonTokenStream tokens, Set<CppClass> classSet) {
        super();
        this.reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    @Override
    public T visitPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        if (ctx.Static_cast() != null) {
            classSet.stream()
                    .filter(x -> x.className.equals(ctx.thetypeid().getText().replace("*", "")))
                    .findAny()
                    .ifPresent(x -> {
                        if (x.isVirtual()) {
                            reWriter.insertBefore(ctx.thetypeid().start, "_");
                        }
                    });
        }
        return super.visitPostfixexpression(ctx);
    }

    @Override
    public T visitIdexpression(CPP14Parser.IdexpressionContext ctx) {
        if (ctx.Limited() != null) {
            reWriter.replace(ctx.start, "");
            classSet.stream()
                    .filter(x -> x.className.equals(ctx.idexpression().getText()))
                    .findAny()
                    .ifPresent(x -> {
                        if (x.isVirtual()) {
                            reWriter.insertBefore(ctx.stop, "_");
                        }
                    });
        }
        return super.visitIdexpression(ctx);
    }

}
