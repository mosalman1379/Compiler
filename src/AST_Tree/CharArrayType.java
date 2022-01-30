package AST_Tree;

import VisitorPattern.Visitor;

public class CharArrayType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
