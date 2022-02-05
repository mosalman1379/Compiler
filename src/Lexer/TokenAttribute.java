package Lexer;


//this class used for token attribute
public class TokenAttribute {
    //for each token we just only one
    //integer or float or char or bool
    //or string attribute
    private int intValue;

    private float floatValue;

    private char charvalue;

    private boolean BooleanValue;

    private String identifierValue;

    public TokenAttribute(int intVal) {
        this.intValue = intVal;
    }

    public TokenAttribute(float floatVal) {
        this.floatValue = floatVal;
    }

    public TokenAttribute(char charVal) {
        this.charvalue = charVal;
    }

    public TokenAttribute(boolean booleanVal) {
        this.BooleanValue = booleanVal;
    }

    public TokenAttribute(String idVal) {
        this.identifierValue = idVal;
    }

    public int getIntValue() {
        return intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public char getCharvalue() {
        return charvalue;
    }

    public boolean getBooleanValue() {
        return BooleanValue;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }
}
