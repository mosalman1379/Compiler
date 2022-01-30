package AST_Tree;

import VisitorPattern.Visitor;

public interface Exp {
    public void accept(Visitor v);
}
