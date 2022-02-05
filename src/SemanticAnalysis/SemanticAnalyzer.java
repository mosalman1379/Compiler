package SemanticAnalysis;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import AST_Tree.Assign;
import AST_Tree.BooleanArrayType;
import AST_Tree.BooleanLiteral;
import AST_Tree.BooleanType;
import AST_Tree.CharArrayType;
import AST_Tree.CharLiteral;
import AST_Tree.CharType;
import AST_Tree.Equal;
import AST_Tree.Exp;
import AST_Tree.FloatArrayType;
import AST_Tree.FloatLiteral;
import AST_Tree.FloatType;
import AST_Tree.Identifier;
import AST_Tree.IdentifierExp;
import AST_Tree.IntegerArrayType;
import AST_Tree.IntegerLiteral;
import AST_Tree.IntegerType;
import AST_Tree.LessThan;
import AST_Tree.LessThanEqual;
import AST_Tree.MoreThan;
import AST_Tree.MoreThanEqual;
import AST_Tree.NotEqual;
import AST_Tree.Type;
import AST_Tree.VarDecl;
import Parser.Parser;

public class SemanticAnalyzer {

    private Parser parser;


    private ArrayList<VarDecl> decelerations;

    private ArrayList<Identifier> identifiers;

    private ArrayList<Assign> assigns;

    private ArrayList<Exp> conditions;

    private int errors;

    //start semantic analyzer
    public void analyzeProgram() throws IOException {

        this.parser.parseProgram();

        this.decelerations = this.parser.getDeclarations();

        checkMultipleDeclaration();

        this.identifiers = this.parser.getIdentifier();

        checkIdentifierExist();

        this.assigns = this.parser.getAssignments();

        checkAssigns();

        this.conditions = this.parser.getConditions();

        checkConditions();

    }

    public SemanticAnalyzer(FileReader file) throws IOException {
        this.parser = new Parser(file);
    }

    // get number of errors
    public int getErrors() {
        return errors;
    }


    private void checkMultipleDeclaration() {
        for (int i = 0; i < decelerations.size(); i++) {
            VarDecl varDecl = decelerations.get(i);
            String idName = varDecl.getId().getName();

            for (int j = i + 1; j < decelerations.size(); j++) {
                VarDecl _varDecl = decelerations.get(j);
                String _idName = _varDecl.getId().getName();

                //if we have multiple declaration we got error
                if (idName.equals(_idName))
                    error(ErrorType.MULTIPLE_DECLARATION, _idName);
            }
        }
    }


    private void checkConditions() {
        for (Exp exp : conditions) {
            if ((exp instanceof MoreThan || exp instanceof MoreThanEqual || exp instanceof LessThan ||
                    exp instanceof LessThanEqual || exp instanceof NotEqual || exp instanceof Equal))
                error(ErrorType.INVALID_CONDITION, null);
        }

    }

    //check identifier exist
    private void checkIdentifierExist() {

        for (Identifier identifier : identifiers) {

            if (!isIdentifierExists(identifier.getName()))

                error(ErrorType.NO_DECLARATION, identifier.getName());

        }

    }


    // check if a specific identifier name is exists
    private boolean isIdentifierExists(String name) {
        for (VarDecl varDecl : decelerations) {
            String idName = varDecl.getId().getName();

            if (idName.equals(name))
                return true;
        }
        return false;
    }


    // type checking of all the assign expressions
    private void checkAssigns() {
        for (Assign assign : assigns) {

            Exp type = assign.getValue();

            String Id_Name = assign.getId().getName();

            Type Id_Type = getIdentifierType(Id_Name);

            // assign to int for array expressions
            if ((Id_Type instanceof IntegerType || Id_Type instanceof IntegerArrayType)) {


                // boolean to int conversion is invalid
                if (type instanceof BooleanLiteral)
                    error(ErrorType.BOOLEAN_INT_CASTING, Id_Name);


                // float to int conversion is invalid
                if (type instanceof FloatLiteral)
                    error(ErrorType.FLOAT_INT_CASTING, Id_Name);


                // assign variable to int
                if (type instanceof IdentifierExp) {
                    String ID_Name = ((IdentifierExp) type).getName();
                    Type ID_Type = getIdentifierType(ID_Name);

                    if (ID_Type != null) {
                        // float to int conversion is invalid
                        if (ID_Type instanceof FloatType)
                            error(ErrorType.FLOAT_INT_CASTING, Id_Name);


                            // boolean to int conversion is invalid
                        else if (ID_Type instanceof BooleanType)
                            error(ErrorType.BOOLEAN_INT_CASTING, Id_Name);


                        if (Id_Type instanceof IntegerType)
                            // identifier with array type
                            if (ID_Type instanceof FloatArrayType || ID_Type instanceof BooleanArrayType
                                    || ID_Type instanceof IntegerArrayType || ID_Type instanceof CharArrayType)
                                error(ErrorType.ARRAY_TO_SINGLE, Id_Name);

                        if (Id_Type instanceof IntegerArrayType)
                            // identifier with single type
                            if (ID_Type instanceof FloatType || ID_Type instanceof BooleanType
                                    || ID_Type instanceof IntegerType || ID_Type instanceof CharType)
                                error(ErrorType.SINGLE_TO_ARRAY, Id_Name);

                    }

                }
            }

            // assign to float
            if ((Id_Type instanceof FloatType || Id_Type instanceof FloatArrayType)) {

                // boolean to float conversion is invalid
                if (type instanceof BooleanLiteral)
                    error(ErrorType.BOOLEAN_FLOAT_CASTING, Id_Name);

                // char to float conversion is invalid
                if (type instanceof CharLiteral)
                    error(ErrorType.CHAR_FLOAT_CASTING, Id_Name);

                if (type instanceof IdentifierExp) {

                    String ID_Name = ((IdentifierExp) type).getName();
                    Type ID_Type = getIdentifierType(ID_Name);

                    if (ID_Type != null) {
                        // boolean to float conversion is invalid
                        if (ID_Type instanceof BooleanType)
                            error(ErrorType.BOOLEAN_FLOAT_CASTING, Id_Name);

                            // char to float conversion is invalid
                        else if (ID_Type instanceof CharType)
                            error(ErrorType.CHAR_FLOAT_CASTING, Id_Name);

                        if (Id_Type instanceof FloatArrayType)
                            // identifier with single type
                            if (ID_Type instanceof FloatType || ID_Type instanceof BooleanType
                                    || ID_Type instanceof IntegerType || ID_Type instanceof CharType)
                                error(ErrorType.SINGLE_TO_ARRAY, Id_Name);

                        if (Id_Type instanceof FloatType)
                            // identifier with array type
                            if (ID_Type instanceof FloatArrayType || ID_Type instanceof BooleanArrayType
                                    || ID_Type instanceof IntegerArrayType || ID_Type instanceof CharArrayType)
                                error(ErrorType.ARRAY_TO_SINGLE, Id_Name);
                    }
                }
            }

            // assign to char
            if ((Id_Type instanceof CharType || Id_Type instanceof CharArrayType)) {

                //int to char conversion is invalid
                if (type instanceof IntegerLiteral)
                    error(ErrorType.INT_CHAR_CASTING, Id_Name);

                // float to char conversion is invalid
                if (type instanceof FloatLiteral)
                    error(ErrorType.FLOAT_CHAR_CASTING, Id_Name);

                // boolean to char conversion is invalid
                if (type instanceof BooleanLiteral)
                    error(ErrorType.BOOLEAN_CHAR_CASTING, Id_Name);

                // type(id) to int conversion is invalid
                if (type instanceof IdentifierExp) {

                    String ID_Name = ((IdentifierExp) type).getName();

                    Type ID_Type = getIdentifierType(ID_Name);

                    if (ID_Type != null) {

                        // float to char conversion is invalid
                        if (ID_Type instanceof FloatType)
                            error(ErrorType.FLOAT_CHAR_CASTING, Id_Name);

                            // int to char conversion is invalid
                        else if (ID_Type instanceof IntegerType)
                            error(ErrorType.INT_CHAR_CASTING, Id_Name);

                            // boolean to char conversion is invalid
                        else if (ID_Type instanceof BooleanType)
                            error(ErrorType.BOOLEAN_CHAR_CASTING, Id_Name);

                        if (Id_Type instanceof CharArrayType)
                            // identifier with single type
                            if (ID_Type instanceof FloatType || ID_Type instanceof BooleanType
                                    || ID_Type instanceof IntegerType || ID_Type instanceof CharType)
                                error(ErrorType.SINGLE_TO_ARRAY, Id_Name);

                        if (Id_Type instanceof CharType)
                            // identifier with array type
                            if (ID_Type instanceof FloatArrayType || ID_Type instanceof BooleanArrayType
                                    || ID_Type instanceof IntegerArrayType || ID_Type instanceof CharArrayType)
                                error(ErrorType.ARRAY_TO_SINGLE, Id_Name);
                    }

                }
            }

            // assign to boolean
            if ((Id_Type instanceof BooleanType || Id_Type instanceof BooleanArrayType)) {
                //int to boolean conversion is invalid
                if (type instanceof IntegerLiteral)
                    error(ErrorType.INT_BOOLEAN_CASTING, Id_Name);


                // char to boolean conversion is invalid
                if (type instanceof CharLiteral)
                    error(ErrorType.CHAR_BOOLEAN_CASTING, Id_Name);


                // float to boolean conversion is invalid
                if (type instanceof FloatLiteral)
                    error(ErrorType.FLOAT_BOOLEAN_CASTING, Id_Name);

                // type(id) to int conversion is invalid
                if (type instanceof IdentifierExp) {
                    String _idName = ((IdentifierExp) type).getName();
                    Type _idType = getIdentifierType(_idName);

                    if (_idType != null) {

                        // float to boolean conversion is invalid
                        if (_idType instanceof FloatType)
                            error(ErrorType.FLOAT_CHAR_CASTING, Id_Name);

                            // int to boolean conversion is invalid
                        else if (_idType instanceof IntegerType)
                            error(ErrorType.INT_CHAR_CASTING, Id_Name);

                            // char to boolean conversion is invalid
                        else if (_idType instanceof CharType)
                            error(ErrorType.CHAR_BOOLEAN_CASTING, Id_Name);

                        if (Id_Type instanceof BooleanType)
                            // identifier with array type
                            if (_idType instanceof FloatArrayType || _idType instanceof BooleanArrayType
                                    || _idType instanceof IntegerArrayType || _idType instanceof CharArrayType)
                                error(ErrorType.ARRAY_TO_SINGLE, Id_Name);

                        if (_idType instanceof BooleanArrayType)
                        // identifier with single type
                        {
                        }
                    }

                }
            }

        }
    }

    // get identifier type with name
    private Type getIdentifierType(String name) {

        for (VarDecl dec : decelerations) {

            Identifier id = dec.getId();

            if (id.getName().equals(name))

                return dec.getType();

        }
        return null;
    }

    // print errors report
    private void error(ErrorType errorType, Object parm) {
        errors++;
        switch (errorType) {
            case MULTIPLE_DECLARATION:
                System.err.println("Declaration Error: MULTIPLE_DECLARATION, variable (" + (String) parm + ")");
                break;
            case NO_DECLARATION:
                System.err.println("Declaration Error: NO_DECLARATION, variable (" + (String) parm + ")");
                break;
            case FLOAT_INT_CASTING:
                System.err.println("Casting Error: FLOAT_INT_CASTING, variable (" + parm + ")");
                break;
            case BOOLEAN_INT_CASTING:
                System.err.println("Casting Error: BOOLEAN_INT_CASTING, variable (" + parm + ")");
                break;
            case INT_BOOLEAN_CASTING:
                System.err.println("Casting Error: INT_BOOLEAN_CASTING, variable (" + parm + ")");
                break;
            case BOOLEAN_FLOAT_CASTING:
                System.err.println("Casting Error: BOOLEAN_FLOAT_CASTING, variable (" + parm + ")");
                break;
            case FLOAT_BOOLEAN_CASTING:
                System.err.println("Casting Error: FLOAT_BOOLEAN_CASTING, variable (" + parm + ")");
                break;
            case CHAR_FLOAT_CASTING:
                System.err.println("Casting Error: CHAR_FLOAT_CASTING, variable (" + parm + ")");
                break;
            case CHAR_BOOLEAN_CASTING:
                System.err.println("Casting Error: CHAR_BOOLEAN_CASTING, variable (" + parm + ")");
                break;
            case FLOAT_CHAR_CASTING:
                System.err.println("Casting Error: FLOAT_CHAR_CASTING, variable (" + parm + ")");
                break;
            case BOOLEAN_CHAR_CASTING:
                System.err.println("Casting Error: BOOLEAN_CHAR_CASTING, variable (" + parm + ")");
                break;
            case INT_CHAR_CASTING:
                System.err.println("Casting Error: INT_CHAR_CASTING, variable (" + parm + ")");
                break;
            case ARRAY_TO_SINGLE:
                System.err.println("Invalid Assignment: ARRAY_TO_SINGLE, variable (" + parm + ")");
                break;
            case SINGLE_TO_ARRAY:
                System.err.println("Invalid Assignment: SINGLE_TO_ARRAY, variable (" + parm + ")");
                break;
            case INVALID_CONDITION:
                System.err.println("Invalid Condition: INVALID_CONDITION");
                break;
            default:
                break;
        }
    }
}