package Model;

public class Operando {
    private Token token;
    
    private String temp;
    
    private int idsimbolos;

    public Operando(Token token) {
        this.token = token;
    }

    public Operando(Token token, int idsimbolos) {
        this.token = token;
        this.idsimbolos = idsimbolos;
    }

    public Operando(String temp) {
        this.temp = temp;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getIdsimbolos() {
        return idsimbolos;
    }

    public void setIdsimbolos(int idsimbolos) {
        this.idsimbolos = idsimbolos;
    }
    
    
}
