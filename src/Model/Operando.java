package Model;

public class Operando {
    Token token;
    
    String tipo;
    
    int idsimbolos = -1;

    public Operando(Token token, String tipo, int idsimbolos) {  // ID
        this.token = token;
        this.tipo = tipo;
        this.idsimbolos = idsimbolos;
    }
    
    public Operando(Token token, String tipo) {  // OPER
        this.token = token;
        this.tipo = tipo;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdsimbolos() {
        return idsimbolos;
    }

    public void setIdsimbolos(int idsimbolos) {
        this.idsimbolos = idsimbolos;
    }
    
    
}
