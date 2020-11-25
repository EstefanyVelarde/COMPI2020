package Model;

public class Cuadruplo {
    String etiqueta, accion, arg1, arg2, res;

    public int amb;
    
    public Cuadruplo(String etiqueta, String accion, String arg1, String arg2, String res) {
        this.etiqueta = etiqueta;
        this.accion = accion;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
    }

    public Cuadruplo(String etiqueta, String accion, String arg1, String arg2, String res, int amb) {
        this.etiqueta = etiqueta;
        this.accion = accion;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
        this.amb = amb;
    }
    
    

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
    
    
    
    
}
