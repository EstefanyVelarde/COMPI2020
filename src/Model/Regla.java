package Model;

public class Regla {
    private String edo, topePila, valorReal;
    private int id, linea, ambito;
    
    public Regla(int id, String topePila, String valorReal, int linea, String edo, int ambito) {
        this.id = id;
        this.topePila = topePila; 
        this.valorReal = valorReal;
        this.linea = linea;
        this.edo = edo;
        this.ambito = ambito;
    }

    public String getTopePila() {
        return topePila;
    }

    public void setTopePila(String topePila) {
        this.topePila = topePila;
    }

    public String getValorReal() {
        return valorReal;
    }

    public void setValorReal(String valorReal) {
        this.valorReal = valorReal;
    }

    public String getEdo() {
        return edo;
    }

    public void setEdo(String edo) {
        this.edo = edo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }


    
}
