package AST_Tree;

import VisitorPattern.Visitor;

public class BooleanType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
