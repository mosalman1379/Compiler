package AST_Tree;

import VisitorPattern.Visitor;

public class CharType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
