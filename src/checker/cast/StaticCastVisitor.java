package checker.cast;

import checker.CommonVisitor;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;
import java.util.Set;

public class StaticCastVisitor extends CommonVisitor<Void> {
    private Set<CppClass> classSet;

    public StaticCastVisitor(CommonTokenStream tokens, Set<CppClass> classSet) {
        super();
        this.reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    @Override
    public Void visitPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        if (ctx.Static_cast() != null) {
            CastVisitor visitor = new CastVisitor(classSet, reWriter);
            visitor.visitPostfixexpression(ctx);
        } else
           super.visitPostfixexpression(ctx);

        return null;
    }

}
