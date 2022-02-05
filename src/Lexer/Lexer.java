package Lexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lexer {
    private BufferedReader stream;
    private Token nextToken;
    private int nextChar;
    private int lineNumber = 1;
    private int columnNumber = 1;

    private final static Map<String, TokenType> reservedWords;
    private final static Map<Character, TokenType> punctuation;
    private final static Map<String, TokenType> operators;

    private int errors; //number of errors



    public Lexer(FileReader file) {
        this.stream = new BufferedReader(file);
        nextChar = getChar();
    }


    static {
        //punctuation of Decaf scanner
        punctuation = new HashMap<Character, TokenType>();
        punctuation.put('(', TokenType.LPAREN);
        punctuation.put(')', TokenType.RPAREN);
        punctuation.put('[', TokenType.LBRACKET);
        punctuation.put(']', TokenType.RBRACKET);
        punctuation.put('{', TokenType.LBRACE);
        punctuation.put('}', TokenType.RBRACE);
        punctuation.put(';', TokenType.SEMI);
        punctuation.put(',', TokenType.COMMA);
        punctuation.put('=', TokenType.ASSIGN);
        punctuation.put('-', TokenType.NEGATIVE);
        punctuation.put('!', TokenType.NOT);


        //ReserveWords of Decaf Scanner
        reservedWords = new HashMap<String, TokenType>();
        reservedWords.put("int", TokenType.INT);
        reservedWords.put("float", TokenType.FLOAT);
        reservedWords.put("char", TokenType.CHAR);
        reservedWords.put("boolean", TokenType.BOOLEAN);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("while", TokenType.WHILE);
        reservedWords.put("main", TokenType.MAIN);

        //Operators in Decaf scanner
        operators = new HashMap<String, TokenType>();
        operators.put("&&", TokenType.AND);
        operators.put("||", TokenType.OR);
        operators.put("==", TokenType.EQ);
        operators.put("!=", TokenType.NEQ);
        operators.put("<", TokenType.LT);
        operators.put(">", TokenType.RT);
        operators.put("<=", TokenType.LT_EQ);
        operators.put(">=", TokenType.RT_EQ);
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.TIMES);
        operators.put("/", TokenType.DIV);
        operators.put("%", TokenType.MOD);
    }

    //Read character from input file
    //if we reach end of file throw exception
    private int getChar() {
        try {
            return stream.read();
        } catch (IOException e) {
            System.err.print(e.getMessage());
            System.err.println("IOException occurred ");
            return -1;
        }
    }

    //in this method we ignore every newlines between
    //codes and for each new line we add lineNumber
    private boolean skipNewline() {
        //changes for newline character
        //adding lineNumber and change columnNumber to one
        if (nextChar == '\n') {
            lineNumber++;
            columnNumber = 1;
            nextChar = getChar();
            return true;
        }
        if (nextChar == '\r') {
            lineNumber++;
            columnNumber = 1;
            nextChar = getChar();

            if (nextChar == '\n')
                nextChar = getChar();
            return true;
        }
        return false;
    }

    //this method detect proper token from nextToken value
    public Token getToken() throws IOException {
        if (nextToken != null) {
            Token token = nextToken;
            nextToken = null;
            return token;
        }

        //in this loop we ignore every whitespace before nextToken value like trim method
        while (Character.isWhitespace(nextChar)) {
            if (!skipNewline()) {
                columnNumber++;
                nextChar = getChar();
            }
            //for each \t we add 3 to columnNumber value
            if (nextChar == '\t')
                columnNumber += 3;
        }

        //if nextCharacter start with letter it can be
        //identifier or reserved word ...
        if (Character.isLetter(nextChar)) {
            StringBuilder current = new StringBuilder(Character.toString((char) nextChar));
            columnNumber++;
            nextChar = getChar();
            //for each letter or digit we add nextChar to current string
            //and we get character from stream
            while (Character.isLetterOrDigit(nextChar)) {
                current.append((char) nextChar);
                columnNumber++;
                nextChar = getChar();
            }

            //we check current is reserved word or not
            TokenType type = reservedWords.get(current.toString());

            if (type != null)
                return new Token(type, new TokenAttribute(current.toString()), lineNumber, columnNumber - current.length());
            //if current is true it is Boolean token
            if (current.toString().equals("true"))
                return new Token(TokenType.BOOLEAN_CONST, new TokenAttribute(true), lineNumber, columnNumber - current.length());
            //if current is false it is Boolean token
            else if (current.toString().equals("false"))
                return new Token(TokenType.BOOLEAN_CONST, new TokenAttribute(false), lineNumber, columnNumber - current.length());
            //otherwise it is identifier
            return new Token(TokenType.ID, new TokenAttribute(current.toString()), lineNumber, columnNumber - current.length());
        }

        //if nextChar start with digit it is float or integer
        if (Character.isDigit(nextChar)) {
            StringBuilder numString = new StringBuilder(Character.toString((char) nextChar));
            columnNumber++;
            nextChar = getChar();
            //while nextChar is digit we add character to current
            while (Character.isDigit(nextChar)) {
                numString.append((char) nextChar);
                columnNumber++;
                nextChar = getChar();
            }

            //if next char is dot it can be double or float
            if (nextChar == '.') {
                nextChar = getChar();
                columnNumber++;

                if (Character.isDigit(nextChar)) {
                    numString.append('.');
                    while (Character.isDigit(nextChar)) {
                        numString.append((char) nextChar);
                        columnNumber++;
                        nextChar = getChar();
                    }

                    return new Token(TokenType.FLOAT_CONST, new TokenAttribute(Float.parseFloat(numString.toString())), lineNumber, columnNumber - numString.length());
                }
                while (!Character.isWhitespace(nextChar)) {
                    columnNumber++;
                    numString.append(nextChar);
                    nextChar = getChar();
                }
                //examples 12.
                return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columnNumber - numString.length() + 1);
            }
            //otherwise it is Integer constant token
            return new Token(TokenType.INT_CONST, new TokenAttribute(Integer.parseInt(numString.toString())), lineNumber, columnNumber - numString.length());
        }


        //if nextChar start with \ this is escape character
        if (nextChar == '\'') {
            nextChar = getChar();
            columnNumber++;
            if (Character.isAlphabetic(nextChar)) {
                char current = (char) nextChar;
                stream.mark(0);
                nextChar = getChar();
                columnNumber++;

                //in this line we have \ character like \\
                if (nextChar == '\'') {
                    nextChar = getChar();
                    columnNumber++;
                    return new Token(TokenType.CHAR_CONST, new TokenAttribute(current), lineNumber, columnNumber - 1);
                }
                stream.reset();
            }
            return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columnNumber - 1);
        }


        //-1 show we read whole file
        if (nextChar == -1)
            return new Token(TokenType.EOF, new TokenAttribute(), lineNumber, columnNumber);

        //in this switch case we detect operators
        switch (nextChar) {

            case '|':
                columnNumber++;
                nextChar = getChar();

                // check if next char is '|' to match '||' binop
                if (nextChar == '|') {
                    nextChar = getChar();
                    return new Token(TokenType.OR, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '&':
                columnNumber++;
                nextChar = getChar();

                if (nextChar == '&') {
                    nextChar = getChar();
                    return new Token(TokenType.AND, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.UNKNOWN, new TokenAttribute(), lineNumber, columnNumber - 1);


            case '=':
                columnNumber++;
                nextChar = getChar();

                if (nextChar == '=') {
                    nextChar = getChar();
                    return new Token(TokenType.EQ, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.ASSIGN, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '!':
                columnNumber++;
                nextChar = getChar();

                if (nextChar == '=') {
                    nextChar = getChar();
                    return new Token(TokenType.NEQ, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.NOT, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '<':
                columnNumber++;
                nextChar = getChar();

                if (nextChar == '=') {
                    nextChar = getChar();
                    return new Token(TokenType.LT_EQ, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.LT, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '>':
                columnNumber++;
                nextChar = getChar();

                if (nextChar == '=') {
                    nextChar = getChar();
                    return new Token(TokenType.RT_EQ, new TokenAttribute(), lineNumber, columnNumber - 2);
                } else
                    return new Token(TokenType.RT, new TokenAttribute(
                    ), lineNumber, columnNumber - 1);

            case '-':
                columnNumber++;
                nextChar = getChar();
                return new Token(TokenType.MINUS, new TokenAttribute(), lineNumber, columnNumber - 1);


            case '+':
                columnNumber++;
                nextChar = getChar();
                return new Token(TokenType.PLUS, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '*':
                columnNumber++;
                nextChar = getChar();
                return new Token(TokenType.TIMES, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '/':
                columnNumber++;
                nextChar = getChar();
                return new Token(TokenType.DIV, new TokenAttribute(), lineNumber, columnNumber - 1);

            case '%':
                columnNumber++;
                nextChar = getChar();
                return new Token(TokenType.MOD, new TokenAttribute(), lineNumber, columnNumber - 1);
        }

        TokenType type = punctuation.get((char) nextChar);
        columnNumber++;
        nextChar = getChar();

        return new Token(Objects.requireNonNullElse(type, TokenType.UNKNOWN), new TokenAttribute(), lineNumber, columnNumber - 1);

    }
}
