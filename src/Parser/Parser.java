package Parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import AST_Tree.*;
import Lexer.Lexer;
import Lexer.Token;
import Lexer.TokenType;

public class Parser {
    //lexer Element for parsing code
    private Lexer lexer;
    //Token element for store current token
    private Token token;
    //errorToken element for store invalid token
    private Token errorToken;

    // this hash table used for procedure of operators
    private final static Map<TokenType, Integer> BinaryOperationLevels;

    private ArrayList<VarDecl> Declarations; //symbol table for declarations

    private ArrayList<Identifier> Identifier; //symbol table for identifiers

    private ArrayList<Assign> Assignments; //symbol table for assignments

    private ArrayList<Exp> Conditions; //symbol table for conditions

    //this integer value hold count of errors
    private int errors;

    static {
        //in this section we add operators with procedure in increasing order
        //numbers are random but we must order between operators

        BinaryOperationLevels = new HashMap<TokenType, Integer>();

        BinaryOperationLevels.put(TokenType.AND, 10);

        BinaryOperationLevels.put(TokenType.OR, 10);

        BinaryOperationLevels.put(TokenType.LT, 20);

        BinaryOperationLevels.put(TokenType.RT, 20);

        BinaryOperationLevels.put(TokenType.LT_EQ, 20);

        BinaryOperationLevels.put(TokenType.RT_EQ, 20);

        BinaryOperationLevels.put(TokenType.EQ, 20);

        BinaryOperationLevels.put(TokenType.NEQ, 20);

        BinaryOperationLevels.put(TokenType.PLUS, 30);

        BinaryOperationLevels.put(TokenType.MINUS, 30);

        BinaryOperationLevels.put(TokenType.TIMES, 40);

        BinaryOperationLevels.put(TokenType.DIV, 40);

        BinaryOperationLevels.put(TokenType.MOD, 40);

        BinaryOperationLevels.put(TokenType.LBRACKET, 50);

    }

    //in this method parser begins parsing process
    public Parser(FileReader file) throws IOException {
        //we pass fileReader to lexer object
        this.lexer = new Lexer(file);

        //we get first token of this token and hold it to token variable
        this.token = lexer.getToken();


        //initializations
        this.Declarations = new ArrayList<>();

        this.Identifier = new ArrayList<>();

        this.Assignments = new ArrayList<>();

        this.Conditions = new ArrayList<>();

    }


    // verifies current token type or reports error
    private boolean CheckType(TokenType type) throws IOException {
        if (token.getType() == type) {
            token = lexer.getToken();
            return true;
        } else {
            ShowError(type);
            return false;
        }
    }

    // reports an error to the console
    private void ShowError(TokenType type) {
        if (token == errorToken)
            return;

        System.err.print("ERROR: " + token.getType());

        System.err.print(" at line " + token.getLineNumber() + ", column " + token.getColumnNumber());

        System.err.println("; Expected " + type);

        errorToken = token; // set error token to prevent cascading

        errors++; // increment error counter
    }

    // skip tokens until match in follow set for error recovery
    private void skipTo(TokenType... follow) throws IOException {
        while (token.getType() != TokenType.EOF) {
            for (TokenType skip : follow) {
                if (token.getType() == skip)
                    return;
            }
            token = lexer.getToken();
        }
    }

    public int getErrors() {
        return errors;
    }

    public ArrayList<VarDecl> getDeclarations() {
        return Declarations;
    }

    public ArrayList<Identifier> getIdentifier() {
        return Identifier;
    }

    public ArrayList<Assign> getAssignments() {
        return Assignments;
    }

    public ArrayList<Exp> getConditions() {
        return Conditions;
    }

    // Program ::= int main '('')' { Declarations StatementList }
    public Program parseProgram() throws IOException {
        //first we check main function syntax is true or not
        CheckType(TokenType.INT);

        CheckType(TokenType.MAIN);

        CheckType(TokenType.LPAREN);

        CheckType(TokenType.RPAREN);

        CheckType(TokenType.LBRACE);

        Declarations declarations = parseDeclarations();

        StatementList statementList = parseStatementList();

        //check we have } at the end of main function
        CheckType(TokenType.RBRACE);
        //check we have EOF at the end of main function
        CheckType(TokenType.EOF);

        return new Program(statementList, declarations);
    }


    //in this method we add each declaration to declarations list
    private Declarations parseDeclarations() throws IOException {
        Declarations declarations = new Declarations();

        while (token.getType() == TokenType.INT || token.getType() == TokenType.FLOAT
                || token.getType() == TokenType.BOOLEAN || token.getType() == TokenType.CHAR)
            declarations.addElement(parseVarDecList());

        return declarations;
    }


    //in this method we check all variable that declare
    //are same type or not for example int a,b,f;
    private VarDeclList parseVarDecList() throws IOException {

        VarDeclList varDeclList = new VarDeclList();

        VarDecl varDecl = parseVarDecl();

        varDeclList.addElement(varDecl);

        getDeclarations().add(varDecl);


        // check for additional varDecl
        while (token.getType() == TokenType.COMMA) {

            CheckType(TokenType.COMMA);

            VarDecl newVarDecl = new VarDecl(varDecl.getType(), parseIdentifier());

            varDeclList.addElement(newVarDecl);

            getDeclarations().add(newVarDecl);

        }

        CheckType(TokenType.SEMI);

        return varDeclList;
    }


    private VarDecl parseVarDecl() throws IOException {

        Type type = parseType();

        Identifier id = parseIdentifier();

        return new VarDecl(type, id);

    }

    //in this method we parse token value and identify type of token
    private Type parseType() throws IOException {
        switch (token.getType()) {

            case INT:
                CheckType(TokenType.INT);

                if (token.getType() == TokenType.LBRACKET) {
                    CheckType(TokenType.LBRACKET);

                    if (CheckType(TokenType.INT_CONST)) {
                        if (token.getType() == TokenType.RBRACKET) {
                            CheckType(TokenType.RBRACKET);
                            return new IntegerArrayType();
                        }
                    }

                    CheckType(TokenType.TYPE);
                    return null;
                }
                return new IntegerType();

            case FLOAT:
                CheckType(TokenType.FLOAT);

                if (token.getType() == TokenType.LBRACKET) {
                    CheckType(TokenType.LBRACKET);

                    if (CheckType(TokenType.INT_CONST)) {
                        if (token.getType() == TokenType.RBRACKET) {
                            CheckType(TokenType.RBRACKET);
                            return new FloatArrayType();
                        }
                    }

                    CheckType(TokenType.TYPE);
                    return null;
                }
                return new FloatType();

            case BOOLEAN:
                CheckType(TokenType.BOOLEAN);

                if (token.getType() == TokenType.LBRACKET) {
                    CheckType(TokenType.LBRACKET);

                    if (CheckType(TokenType.INT_CONST)) {
                        if (token.getType() == TokenType.RBRACKET) {
                            CheckType(TokenType.RBRACKET);
                            return new BooleanArrayType();
                        }
                    }

                    CheckType(TokenType.TYPE);
                    return null;
                }
                return new BooleanType();

            case CHAR:
                CheckType(TokenType.CHAR);

                if (token.getType() == TokenType.LBRACKET) {
                    CheckType(TokenType.LBRACKET);

                    if (CheckType(TokenType.INT_CONST)) {
                        if (token.getType() == TokenType.RBRACKET) {
                            CheckType(TokenType.RBRACKET);
                            return new CharArrayType();
                        }
                    }

                    CheckType(TokenType.TYPE);
                    return null;
                }
                return new CharType();

            default:
                CheckType(TokenType.TYPE);
                return null;

        }
    }

    //in this method we parse identifier and verify it
    private Identifier parseIdentifier() throws IOException {
        //begins with null value to identifier
        Identifier identifier = null;

        //check token type is same with ID type
        if (token.getType() == TokenType.ID)
            identifier = new Identifier(token.getAttribute().getIdentifierValue());

        CheckType(TokenType.ID);

        return identifier;
    }

    private StatementList parseStatementList() throws IOException {
        StatementList statementList = new StatementList();
        while (isStatement())
            statementList.addElement(parseStatement());
        return statementList;
    }

    //if current token is each of below item it is statement
    private boolean isStatement() {
        switch (token.getType()) {
            case SEMI:
            case IF:
            case WHILE:
            case LPAREN:
            case LBRACE:
            case ID:
                return true;
            default:
                return false;
        }
    }

    private Statement parseStatement() throws IOException {

        if (token.getType() == TokenType.IF) {
            CheckType(TokenType.IF);


            //in this condition if we have space we ignore it
            if (!CheckType(TokenType.LPAREN))
                skipTo(TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);

            // parse conditional expression
            Exp condExp = parseExp();
            Conditions.add(condExp);

            if (!CheckType(TokenType.RPAREN))
                skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);

            // parse true statements
            Statement trueStm;

            //if we have { we parse in block level
            if (token.getType() == TokenType.LBRACE)
                trueStm = parseBlock();

            else
                // parse true statement in statement level
                trueStm = parseStatement();

            if (token.getType() == TokenType.ELSE) {
                if (!CheckType(TokenType.ELSE))
                    skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);

                //parse false statements
                Statement falseStm;

                //if we have { we parse in block level
                if (token.getType() == TokenType.LBRACE)
                    falseStm = parseBlock();


                else
                    //if we have { we parse in statement level
                    falseStm = parseStatement();

                return new If(condExp, trueStm, falseStm);

            }
            return new If(condExp, trueStm, null);
        }

        // WhileStatement
        if (token.getType() == TokenType.WHILE) {
            CheckType(TokenType.WHILE);

            // parse loop condition
            if (!CheckType(TokenType.LPAREN))
                skipTo(TokenType.RPAREN, TokenType.LBRACE, TokenType.RBRACE);

            //parse expresion and add to condition list
            Exp condExp = parseExp();
            Conditions.add(condExp);

            if (!CheckType(TokenType.RPAREN))
                skipTo(TokenType.LBRACE, TokenType.SEMI, TokenType.RBRACE);

            //parse loop statements
            Statement loopStm;

            // parsing in block level
            if (token.getType() == TokenType.LBRACE)
                loopStm = parseBlock();

            else
                // parse loop statement
                loopStm = parseStatement();

            return new While(condExp, loopStm);
        }

        // if current token is Identifier
        if (token.getType() == TokenType.ID) {

            //we make identifier with its attributes
            Identifier id = new Identifier(token.getAttribute().getIdentifierValue());
            //add id to Identifier list
            Identifier.add(id);

            CheckType(TokenType.ID);

            //if current token is assignment check it is valid or not
            if (token.getType() == TokenType.ASSIGN) {

                CheckType(TokenType.ASSIGN);

                Exp value = parseExp();

                CheckType(TokenType.SEMI);


                Assign assign = new Assign(id, value);

                Assignments.add(assign);

                return assign;
            }

            // checking arraay assignment exp: id [ Exp ] = Exp ;
            if (token.getType() == TokenType.LBRACKET) {

                CheckType(TokenType.LBRACKET);

                Exp index = parseExp();

                if (!(index instanceof IntegerLiteral)) {

                    CheckType(TokenType.TYPE);

                    token = lexer.getToken();

                    return null;
                }

                if (!CheckType(TokenType.RBRACKET))
                    skipTo(TokenType.ASSIGN, TokenType.SEMI);

                if (!CheckType(TokenType.ASSIGN))
                    skipTo(TokenType.SEMI);

                Exp value = parseExp();

                //checking ; at the end of expression
                CheckType(TokenType.SEMI);

                //checking array assignment
                Assign assign = new Assign(id, value);
                Assignments.add(assign);
                return new ArrayAssign(id, index, value);
            }
        }

        CheckType(TokenType.STATEMENT);

        token = lexer.getToken();

        return null;
    }

    private Exp parseExp() throws IOException {
        Exp lhs = parsePrimaryExp();
        return parseBinaryOperationRHS(0, lhs);
    }


    private Block parseBlock() throws IOException {
        CheckType(TokenType.LBRACE);
        StatementList stms = new StatementList();
        while (token.getType() != TokenType.RBRACE && token.getType() != TokenType.EOF)
            stms.addElement(parseStatement());

        if (!CheckType(TokenType.RBRACE))
            skipTo(TokenType.RBRACE, TokenType.SEMI);

        return new Block(stms);
    }


    private Exp parsePrimaryExp() throws IOException {
        // check this token is INT_CONST, Boolean or ...
        switch (token.getType()) {

            case BOOLEAN_CONST:

                boolean booleanVal = token.getAttribute().getBooleanValue();

                CheckType(TokenType.BOOLEAN_CONST);

                return new BooleanLiteral(booleanVal);

            case INT_CONST:
                int intValue = token.getAttribute().getIntValue();

                CheckType(TokenType.INT_CONST);

                return new IntegerLiteral(intValue);

            case FLOAT_CONST:

                float floatValue = token.getAttribute().getFloatValue();

                CheckType(TokenType.FLOAT_CONST);

                return new FloatLiteral(floatValue);

            case CHAR_CONST:

                char charVal = token.getAttribute().getCharvalue();

                CheckType(TokenType.CHAR_CONST);

                return new CharLiteral(charVal);

            case ID:

                Identifier id = parseIdentifier();

                Identifier.add(id);

                return new IdentifierExp(id.getName());

            case LPAREN:

                CheckType(TokenType.LPAREN);

                Exp exp = parseExp();

                CheckType(TokenType.RPAREN);

                return exp;

            case NOT:

                CheckType(TokenType.NOT);

                return new Not(parseExp());

            case NEGATIVE:

                CheckType(TokenType.NEGATIVE);

                return new Negative(parseExp());


            default:
                CheckType(TokenType.EXPRESSION);
                token = lexer.getToken();
                return null;
        }
    }

    private Exp parseBinaryOperationRHS(int level, Exp lhs) throws IOException {
        while (true) {
            Integer val = BinaryOperationLevels.get(token.getType());

            int tokenLevel = (val == null) ? -1 : val;

            //check lef hand side procedure is bigger or right hand side
            if (tokenLevel < level)
                return lhs;

            TokenType BinaryOperation = token.getType();
            CheckType(BinaryOperation);

            Exp RightHandSide = parsePrimaryExp(); // parse RightHandSide of exp
            val = BinaryOperationLevels.get(token.getType());
            int nextLevel = (val == null) ? -1 : val;
            if (tokenLevel < nextLevel)
                RightHandSide = parseBinaryOperationRHS(tokenLevel + 1, RightHandSide);

            switch (BinaryOperation) {
                case AND:
                    lhs = new And(lhs, RightHandSide);
                    break;
                case OR:
                    lhs = new Or(lhs, RightHandSide);
                    break;
                case EQ:
                    lhs = new Equal(lhs, RightHandSide);
                    break;
                case NEQ:
                    lhs = new NotEqual(lhs, RightHandSide);
                    break;
                case LT:
                    lhs = new LessThan(lhs, RightHandSide);
                    break;
                case RT:
                    lhs = new MoreThan(lhs, RightHandSide);
                    break;
                case LT_EQ:
                    lhs = new LessThanEqual(lhs, RightHandSide);
                    break;
                case RT_EQ:
                    lhs = new MoreThanEqual(lhs, RightHandSide);
                    break;
                case PLUS:
                    lhs = new Plus(lhs, RightHandSide);
                    break;
                case MINUS:
                    lhs = new Minus(lhs, RightHandSide);
                    break;
                case TIMES:
                    lhs = new Times(lhs, RightHandSide);
                    break;
                case DIV:
                    lhs = new Divide(lhs, RightHandSide);
                    break;
                case MOD:
                    lhs = new Modules(lhs, RightHandSide);
                    break;
                case LBRACKET:
                    lhs = new ArrayLookup(lhs, RightHandSide);
                    CheckType(TokenType.RBRACKET);
                    break;
                default:
                    CheckType(TokenType.OPERATOR);
                    break;
            }
        }
    }

}