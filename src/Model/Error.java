package Model;

public class Error {

    private int error;
    private int linea;
    private String lexema;
    private String tipo;
    private String desc;

    public Error(int error, int linea, String lexema, String desc, String tipo) {
        this.error = error;
        this.linea = linea;
        this.lexema = lexema;
        this.desc = desc;
        this.tipo = tipo;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesc() {
        return desc;
    }
    
}

