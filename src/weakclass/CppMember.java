package weakclass;

import checker.util.Info;
import grammar.antlr.CPP14Parser;

import java.io.Serializable;

public class CppMember implements Serializable {
    CppAccessSpecifier accessSpecifier;
    String content;

    CppMember(CppAccessSpecifier accessSpecifier) {
        this.accessSpecifier = accessSpecifier;
        this.content = null;
    }

    public String getAccessSpecifier() {
        return accessSpecifier.getName().replaceAll("\n", "");
    }

    public String getContent() {
        return content;
    }

    public void setContent(CPP14Parser.MemberdeclarationContext content) {
        this.content = "    " + Info.getText(content) + "\n";
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addBeforeContent(String content) {
        this.content = content + this.content;
    }
}
