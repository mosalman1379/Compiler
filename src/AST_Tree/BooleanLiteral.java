package AST_Tree;

import VisitorPattern.Visitor;

public class BooleanLiteral implements Exp {
    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
