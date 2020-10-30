package Model;

public class Operando {
    Token token;
    
    String tipo;
    
    int idsimbolos = -1;
    
    String[] simbolos; // [0] tipo [1] clase [2] idsimbolos

    public Operando(Token token, String tipo, int idsimbolos, String[] simbolos) {  // ID
        this.token = token;
        this.tipo = tipo;
        this.idsimbolos = idsimbolos;
        this.simbolos = simbolos;
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

    public String[] getSimbolos() {
        return simbolos;
    }

    public void setSimbolos(String[] simbolos) {
        this.simbolos = simbolos;
    }
    
    
}
