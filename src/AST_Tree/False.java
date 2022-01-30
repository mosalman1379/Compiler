package AST_Tree;

import VisitorPattern.Visitor;

public class False implements Exp {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
