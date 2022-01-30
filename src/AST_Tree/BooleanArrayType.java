package AST_Tree;

import VisitorPattern.Visitor;

public class BooleanArrayType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
