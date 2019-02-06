package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;

import java.util.ArrayList;

public class ParamVisitor<T> extends CommonVisitor<ArrayList<String>> {
    private ArrayList<String> paramList;

    ParamVisitor() {
        paramList = new ArrayList<>();
    }

    @Override
    public ArrayList<String> visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        super.visitParametersandqualifiers(ctx);
        return paramList;
    }

    @Override
    public ArrayList<String> visitTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        paramList.add(Info.getText(ctx));
        super.visitTypespecifier(ctx);
        return null;
    }

}
