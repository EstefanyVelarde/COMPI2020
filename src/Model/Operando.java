package Model;

public class Operando {
    Token token;
    
    String tipo, lex, tipoCuad;
    
    int idsimbolos = -1, noTemp;
    
    boolean temp, fun, arr, range;
    
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
    
    public Operando(Token token, String tipo, boolean temp, int noTemp) {  // TEMP
        this.token = token;
        this.tipo = tipo;
        
        this.lex = token.getLexema();
        
        this.temp = temp;
        
        this.noTemp = noTemp;
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
        return fun;
    }

    public void setFun(boolean fun) {
        this.fun = fun;
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

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    public int getNoTemp() {
        return noTemp;
    }

    public void setNoTemp(int noTemp) {
        this.noTemp = noTemp;
    }

    
    
}
