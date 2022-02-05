package AST_Tree;

import VisitorPattern.Visitor;

//Exp interface used for detect meaning of statement in abstract syntax tree
public interface Statement {
    void accept(Visitor v);
}
