package AST_Tree;

import java.util.ArrayList;
import java.util.List;

import VisitorPattern.Visitor;

public class Declarations {
    private List<VarDeclList> list;

    public Declarations() {
        list = new ArrayList<VarDeclList>();
    }

    public void addElement(VarDeclList varDecl) {
        getList().add(varDecl);
    }

    public List<VarDeclList> getList() {
        return list;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
