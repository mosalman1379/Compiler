package AST_Tree;

import VisitorPattern.Visitor;

public interface Statement {
    public void accept(Visitor v);
}
