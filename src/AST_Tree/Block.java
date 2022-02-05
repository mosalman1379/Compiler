package AST_Tree;

import VisitorPattern.Visitor;

public class Block implements Statement {
    private StatementList statements;

    public Block(StatementList statements) {
        this.statements = statements;
    }

    public StatementList getStatements() {
        return statements;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
