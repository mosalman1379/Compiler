package AST_Tree;

import VisitorPattern.Visitor;

//Exp interface used for detect meaning of expresion in abstract syntax tree
public interface Exp {
    void accept(Visitor v);
}
