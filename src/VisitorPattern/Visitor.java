package VisitorPattern;

import AST_Tree.*;

 public interface Visitor {
    void visit(Program prog);

     void visit(VarDecl var);

     void visit(VarDeclList varList);

     void visit(Declarations dec);

     void visit(Formal param);

     void visit(IntegerArrayType intArrayT);

     void visit(FloatArrayType floatArrayT);

     void visit(BooleanArrayType booleanArrayT);

     void visit(CharArrayType charArrayT);

     void visit(BooleanType boolT);

     void visit(IntegerType intT);

     void visit(FloatType floatT);

     void visit(CharType charT);

     void visit(IdentifierType idT);

     void visit(Block blockStm);

    void visit(If ifStm);

     void visit(While whileStm);

     void visit(Assign assignStm);

     void visit(ArrayAssign arrayAssignStm);

     void visit(And andExp);

     void visit(Or orExp);

     void visit(MoreThan moreExp);

     void visit(LessThan lessThanExp);

     void visit(Equal equalExp);

     void visit(NotEqual notEqualExp);

     void visit(MoreThanEqual moreEqualExp);

     void visit(LessThanEqual lessEqualExp);

     void visit(Plus plusExp);

     void visit(Minus minusExp);

     void visit(Times timesExp);

     void visit(Divide divExp);

     void visit(Modules modExp);

     void visit(ArrayLookup arrayLookup);

     void visit(ArrayLength length);

     void visit(IntegerLiteral intLiteral);

     void visit(FloatLiteral floatLiteral);

     void visit(BooleanLiteral booleanLiteral);

     void visit(CharLiteral charLiteral);

     void visit(True trueLiteral);

     void visit(False falseLiteral);

     void visit(IdentifierExp identExp);

     void visit(NewArray array);

     void visit(Not notExp);

     void visit(Negative negExp);

     void visit(Identifier id);
}