package checker.cast;

import checker.CommonVisitor;
import checker.util.Info;
import checker.util.Rewrite;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;

import java.util.Set;

public class CastVisitor<T> extends CommonVisitor<Void> {
    private Set<CppClass> classSet;

    CastVisitor(Set<CppClass> classSet, TokenStreamRewriter reWriter) {
        this.classSet = classSet;
        this.reWriter = reWriter;
    }

    @Override
    public Void visitPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        if (ctx.thetypeid() == null)
            return null;
        String typeId = Info.getTypeId(ctx.thetypeid());
        classSet.stream()
                .filter(x -> x.getFullName().equals(typeId))
                .findAny()
                .ifPresent(x -> {
                    if (x.isWeak()) {
                        Rewrite.castTempClass(reWriter, ctx);
                    }
                });
        super.visitPostfixexpression(ctx);
        return null;
    }

    @Override
    public Void visitIdexpression(CPP14Parser.IdexpressionContext ctx) {
        if (ctx.Limited() != null) {
            reWriter.replace(ctx.start, "");
            classSet.stream()
                    .filter(x -> x.getFullName().equals(ctx.idexpression().getText()))
                    .findAny()
                    .ifPresent(x -> {
                        if (x.isWeak()) {
                            Rewrite.castLimited(reWriter, ctx);
                            reWriter.insertBefore(ctx.stop, "_");
                        }
                    });
        }
        super.visitIdexpression(ctx);
        return null;
    }

}
