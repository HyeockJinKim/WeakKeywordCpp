package checker.classinfo;

import checker.CommonVisitor;
import grammar.antlr.CPP14Parser;
import weakclass.CppClass;
import java.util.Set;

public class SuperClassVisitor<T> extends CommonVisitor<Void> {
    private Set<CppClass> classSet;
    private CppClass currentClass;

    SuperClassVisitor(Set<CppClass> classSet, CppClass currentClass) {
        this.classSet = classSet;
        this.currentClass = currentClass;
    }

    @Override
    public Void visitBaseclause(CPP14Parser.BaseclauseContext ctx) {
        super.visitBaseclause(ctx);
        currentClass.inheritSuperVirtualFunction();
        return null;
    }

    @Override
    public Void visitBasetypespecifier(CPP14Parser.BasetypespecifierContext ctx) {
        classSet.stream()
                .filter(x -> x.getFullName().equals(ctx.getText()))
                .findAny()
                .ifPresent(currentClass::addSuperSet);

        return super.visitBasetypespecifier(ctx);
    }
}
