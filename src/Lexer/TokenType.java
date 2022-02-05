package Lexer;


//all of the valid tokens in the Decaf language
public enum TokenType {
    // binary operators
    AND, OR, EQ, NEQ, LT, RT, LT_EQ,
    RT_EQ, PLUS, MINUS, TIMES, DIV, MOD,


    // reserved words
    MAIN, INT, CHAR, FLOAT,
    BOOLEAN, IF, ELSE, WHILE,

    ID, // identifier regex: [a-zA-Z][a-zA-Z0-9_]*
    INT_CONST, // integer regex: [0-9]+
    FLOAT_CONST, // float regex: [0-9]+.[0-9]+
    CHAR_CONST, //'ASCII Char'
    BOOLEAN_CONST,
    EOF, // End of File
    UNKNOWN, // token could not be processed


    // punctuation tokens
    LPAREN, // (
    RPAREN, // )
    LBRACKET, // [
    RBRACKET, // ]
    LBRACE, // {
    RBRACE, // }
    SEMI, // ;
    COMMA, // ,
    ASSIGN, // =
    NEGATIVE, // -
    NOT, // !


    // for error reporting
    STATEMENT,
    EXPRESSION,
    OPERATOR,
    TYPE
}