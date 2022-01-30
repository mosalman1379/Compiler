package AST_Tree;

import VisitorPattern.Visitor;

public class IntegerArrayType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
