package weakclass;

import checker.util.Info;
import org.antlr.v4.runtime.ParserRuleContext;

public class CppNonVirtual extends CppMember {
    ParserRuleContext content;

    public CppNonVirtual(CppAccessSpecifier accessSpecifier) {
        super(accessSpecifier);
        this.content = null;
    }


    public String getContent() {
        return "    " + Info.getText(content) + "\n";
    }

    public ParserRuleContext getContext() {
        return content;
    }

    public void setContent(ParserRuleContext content) {
        this.content = content;
    }

}
