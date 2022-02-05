package Lexer;

//Token class with attribute and other details
public class Token {
    private TokenType type;
    private TokenAttribute attribute;
    //this number used for in which line of code we detect this token
    private int lineNumber;
    //this number used for in which part of current line we detect this token
    private int columnNumber;

    public TokenType getType() {
        return type;
    }

    public TokenAttribute getAttribute() {
        return attribute;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }


    public Token(TokenType type, TokenAttribute attribute, int lineNumber, int columnNumber) {
        this.type = type;
        this.attribute = attribute;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

}