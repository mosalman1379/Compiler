package AST_Tree;

import VisitorPattern.Visitor;

public class Equal implements Exp {
    private Exp LeftSide, RightSide;

    public Equal(Exp LeftSide, Exp RightSide) {
        this.LeftSide = LeftSide;
        this.RightSide = RightSide;
    }

    public Exp getLHS() {
        return LeftSide;
    }

    public Exp getRHS() {
        return RightSide;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
