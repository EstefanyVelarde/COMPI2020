package Control.Semantica;

import Model.Regla;
import java.util.LinkedList;

public class ContSemantica3 {
    public LinkedList<Regla> listaReglas;
    
    public int[][] aparece, contAmb;
    
    public int amb;

    public int getAmb() {
        return amb;
    }

    public void setAmb(int amb) {
        this.amb = amb;
        
        contAmb = new int[20][amb + 2];
        
        setContAmb();
    }

    public ContSemantica3() {
        
        aparece = new int[19][4];
        
        aparece[0][0] = 2001;
        aparece[1][0] = 2002;
        aparece[2][0] = 2003;
        aparece[3][0] = 2004;
        aparece[4][0] = 2005;
        aparece[5][0] = 2006;
        aparece[6][0] = 2007;
        aparece[7][0] = 2008;
        aparece[8][0] = 2009;
        aparece[9][0] = 2010;
        aparece[10][0] = 2011;
        aparece[11][0] = 2012;
        aparece[12][0] = 2013;
        aparece[13][0] = 2014;
        aparece[14][0] = 2016;
        aparece[15][0] = 2017;
        aparece[16][0] = 2018;
        aparece[17][0] = 2019;
        aparece[18][0] = 2020;
    }
    
    public void setAparicion() {
        for (int i = 0; i < listaReglas.size(); i++) {
            int id = listaReglas.get(i).getId();
            String edo = listaReglas.get(i).getEdo();
            
            for (int j = 0; j < aparece.length; j++) {
                if(id == aparece[j][0]) {
                    aparece[j][1]++;
                    
                    if(edo.equals("Acepta"))
                        aparece[j][2]++;
                    else
                        aparece[j][3]++;
                }
            }
        }
    }
    
    public void setContAmb() {
        for (int i = 0; i < listaReglas.size(); i++) {
            int id = listaReglas.get(i).getId();
            int amb = listaReglas.get(i).getAmbito();
            
            for (int j = 0; j < aparece.length; j++) {
                if(id == aparece[j][0]) {
                    contAmb[j][amb]++;
                    
                    contAmb[19][amb]++;
                    
                    contAmb[j][this.amb + 1]++;
                    
                    contAmb[19][this.amb + 1]++;
                }
            }
        }
    }
}
