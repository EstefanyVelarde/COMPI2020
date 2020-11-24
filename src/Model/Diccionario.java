package Model;

import java.util.LinkedList;

public class Diccionario {
    int idsimbolos;
    String id, tipo, clase;
    public int ambito, tArr, dimArr;
    String valor;
    public int noPar;
    String llave, tpArr;

    public Diccionario(int idsimbolos, String id, String tipo, String clase, int ambito) {
        this.idsimbolos = idsimbolos;
        this.id = id;
        this.tipo = tipo;
        this.clase = clase;
        this.ambito = ambito;
    }
    
    public Diccionario(String[] simbolos) {
        setDiccionario(simbolos);
    }
    
    public void setDiccionario(String[] simbolos) {
        this.idsimbolos = getInteger(simbolos[0]);
        this.id = simbolos[1];
        this.tipo = simbolos[2];
        this.clase = simbolos[3];
        this.ambito = getInteger(simbolos[4]);
    }
    
    public boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    public int getInteger(String num) {
        if(isInteger(num))
            return Integer.parseInt(num);
        else
            return -1;
    }

    public int getIdsimbolos() {
        return idsimbolos;
    }

    public void setIdsimbolos(int idsimbolos) {
        this.idsimbolos = idsimbolos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

    public int gettArr() {
        return tArr;
    }

    public void settArr(int tArr) {
        this.tArr = tArr;
    }

    public int getDimArr() {
        return dimArr;
    }

    public void setDimArr(int dimArr) {
        this.dimArr = dimArr;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getNoPar() {
        return noPar;
    }

    public void setNoPar(int noPar) {
        this.noPar = noPar;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getTpArr() {
        return tpArr;
    }

    public void setTpArr(String tpArr) {
        this.tpArr = tpArr;
    }
    
    
}
