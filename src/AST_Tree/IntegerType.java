package AST_Tree;

import VisitorPattern.Visitor;

public class IntegerType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
