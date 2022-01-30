package AST_Tree;

import VisitorPattern.Visitor;

public class LessThanEqual implements Exp {
    private Exp lhs, rhs;

    public LessThanEqual(Exp lhs, Exp rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Exp getLHS() {
        return lhs;
    }

    public Exp getRHS() {
        return rhs;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
