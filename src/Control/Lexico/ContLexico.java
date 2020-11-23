package Control.Lexico;

import Model.Token;
import Model.Error;
import java.util.Iterator;
import java.util.LinkedList;

public class ContLexico {
    
    // LEXICO
    int cE;
    int cId;
    int cCom;
    int cPR;
    int cEDec;
    int cEBin;
    int cEHex;
    int cEOct;
    int cTexto;
    int cFloat;
    int cComp;
    int cCar;
    int cOArit;
    int cOMon;
    int cOLog;
    int cOBit;
    int cOId;
    int cSPun;
    int cSAgr;
    int cSAsig;
    int cORel;
    
    int[] lineas;
    
    public ContLexico(int len) {
        
        lineas = new int[len+1];
    }
    
    public void setContadores(LinkedList<Token> tokens) {
        Iterator it = tokens.iterator();

        while (it.hasNext()) {
            Token tmp = (Token) it.next();
            
            int token = tmp.getToken();
            
            if(token >= -6)
                cSAgr++;
            else if(token >= -13)
                cOArit++;
            else if(token >= -15)
                cOMon++;
            else if(token >= -18)
                cOLog++;
            else if(token >= -24)
                cORel++;
            else if(token >= -29)
                cOBit++;
            else if(token >= -37)
                cSAsig++;
            else if(token >= -39)
                cSPun++;
            else if(token == -40)
                cTexto++;
            else if(token == -41)
                cCar++;
            else if(token >= -43)
                cCom++;
            else if(token >= -50) {
                switch(token) {
                    case -44: cId++; break;
                    case -45: cEDec++; break;
                    case -46: cFloat++; break;
                    case -47: cComp++; break;
                    case -48: cEBin++; break;
                    case -49: cEHex++; break;
                    case -50: cEOct++; break;
                }
            } else {
                switch(token) {
                    case -51: cOLog++; break; // ## operador logico
                    case -52: cSPun++; break; // . signo de puntuación
                    case -53: cSPun++; break; // : signo de puntuación
                    case -57: 
                    case -58: cOId++; break;
                    default: cPR++;
                }
            }
            
        }
    }
    
    
    public int[] getContadores() {
        int[] cont = {cE, cId, cCom, cPR, cEDec, cEBin, cEHex, cEOct, cTexto, 
            cFloat, cComp, cCar, cOArit, cOMon, cOLog, cOBit, cOId, cSPun, 
            cSAgr, cSAsig, cORel};
        
        return cont;
    }
    
    public void setErrores(LinkedList<Error> errores) {
        Iterator it = errores.iterator();
        
        while (it.hasNext()) {
            Error tmp = (Error) it.next();
            lineas[tmp.getLinea()]++;
        }
        
        cE = errores.size();
    }
    
    public int[] getErrores() {
        return lineas;
    }
}
