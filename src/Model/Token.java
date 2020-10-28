package Model;

public class Token {

    private int token;
    private int linea;
    private String lexema;

    public Token(int token, int linea, String lexema) {
        this.token = token;
        this.linea = linea;
        this.lexema = lexema;
    }
    
    public Token(String lexema, int linea) {
        this.lexema = lexema;
        this.linea = linea;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

}
