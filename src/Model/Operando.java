package Model;

public class Operando {
    Token token;
    
    String tipo, lex;
    
    int idsimbolos = -1;
    
    boolean Fun, arr, range;
    
    String[] simbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista

    public Operando(Token token, String tipo, int idsimbolos, String[] simbolos) {  // ID
        this.token = token;
        this.tipo = tipo;
        this.idsimbolos = idsimbolos;
        this.simbolos = simbolos;
        
        this.lex = token.getLexema();
    }
    
    public Operando(Token token, String tipo) {  // OPER
        this.token = token;
        this.tipo = tipo;
        
        this.lex = token.getLexema();
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

    public String getLex() {
        return lex;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public boolean isFun() {
        return Fun;
    }

    public void setFun(boolean Fun) {
        this.Fun = Fun;
    }

    public boolean isArr() {
        return arr;
    }

    public void setArr(boolean arr) {
        this.arr = arr;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    
    
}
