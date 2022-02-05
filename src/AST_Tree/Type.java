package AST_Tree;

import VisitorPattern.Visitor;

//Exp interface used for detect meaning of type of token in abstract syntax tree
public interface Type {
    void accept(Visitor v);
}
