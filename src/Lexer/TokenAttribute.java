package Lexer;

public class TokenAttribute {
    private int intVal;
    private float floatVal;
    private char charVal;
    private boolean booleanVal;
    private String idVal;

    public TokenAttribute() {
    }

    public TokenAttribute(int intVal) {
        this.intVal = intVal;
    }

    public TokenAttribute(float floatVal) {
        this.floatVal = floatVal;
    }

    public TokenAttribute(char charVal) {
        this.charVal = charVal;
    }

    public TokenAttribute(boolean booleanVal) {
        this.booleanVal = booleanVal;
    }

    public TokenAttribute(String idVal) {
        this.idVal = idVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public float getFloatVal() {
        return floatVal;
    }

    public char getCharVal() {
        return charVal;
    }

    public boolean getBooleanVal() {
        return booleanVal;
    }

    public String getIdVal() {
        return idVal;
    }
}
