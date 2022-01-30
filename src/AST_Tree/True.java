package AST_Tree;

import VisitorPattern.Visitor;

public class True implements Exp {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
