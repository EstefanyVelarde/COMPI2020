package Model;

public class Etiqueta {
    String comp, salida, est1, est2;

    public Etiqueta(String comp, String salida, String est1, String est2) {
        this.comp = comp;
        this.salida = salida;
        this.est1 = est1;
        this.est2 = est2;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getEst1() {
        return est1;
    }

    public void setEst1(String est1) {
        this.est1 = est1;
    }

    public String getEst2() {
        return est2;
    }

    public void setEst2(String est2) {
        this.est2 = est2;
    }
    
    
}
