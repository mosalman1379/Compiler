package AST_Tree;

import VisitorPattern.Visitor;

public interface Type {
    public void accept(Visitor v);
}
