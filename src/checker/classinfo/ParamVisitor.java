package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import org.abego.treelayout.internal.util.java.lang.string.StringUtil;

import java.util.ArrayList;

public class ParamVisitor<T> extends CommonVisitor<ArrayList<String>> {
    private ArrayList<String> paramList;
    private ArrayList<String> paramNameList;

    ParamVisitor() {
        paramList = new ArrayList<>();
        paramNameList = new ArrayList<>();
    }

    @Override
    public ArrayList<String> visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        super.visitParametersandqualifiers(ctx);
        return paramList;
    }

    @Override
    public ArrayList<String> visitParameterdeclaration(CPP14Parser.ParameterdeclarationContext ctx) {
        super.visitParameterdeclaration(ctx);
        return null;
    }

    @Override
    public ArrayList<String> visitDeclarator(CPP14Parser.DeclaratorContext ctx) {
        String param = Info.getText(ctx);
        if (param.contains("*")) {
            int index = paramList.size()-1;
            paramList.add(index, paramList.remove(index)+Info.getStarOfString(param));
            param = param.replace("*", "");
        }
        paramNameList.add(param);
        return null;
    }

    @Override
    public ArrayList<String> visitTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        if (!Info.getText(ctx).equals("const"))
        paramList.add(Info.getText(ctx));
        return null;
    }

    ArrayList<String> getParamNameList() {
        return paramNameList;
    }
}
