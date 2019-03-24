package weakclass;

import checker.util.Info;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.Serializable;

public class CppMember implements Serializable {
    CppAccessSpecifier accessSpecifier;
    String content;

    public CppMember(CppAccessSpecifier accessSpecifier) {
        this.accessSpecifier = accessSpecifier;
        this.content = null;
    }

    public String getAccessSpecifier() {
        return accessSpecifier.getName().replaceAll("\n", "");
    }

    public String getContent() {
        return content;
    }

    public void setContent(ParserRuleContext content) {
        this.content = "    " + Info.getText(content) + "\n";
    }

}
