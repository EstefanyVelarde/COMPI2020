package Control.Semantica;

import Model.Regla;
import java.util.LinkedList;

public class ContSemantica2 {
    public LinkedList<Regla> listaReglas;
    
    public int[][] aparece, contAmb;
    
    public int amb;

    public int getAmb() {
        return amb;
    }

    public void setAmb(int amb) {
        this.amb = amb;
        
        contAmb = new int[28][amb + 2];
        
        setContAmb();
    }

    public ContSemantica2() {
        
        aparece = new int[27][4];
        
        aparece[0][0] = 1010;
        aparece[1][0] = 1011;
        aparece[2][0] = 1012;
        aparece[3][0] = 1020;
        aparece[4][0] = 1021;
        aparece[5][0] = 1030;
        aparece[6][0] = 1031;
        aparece[7][0] = 1040;
        aparece[8][0] = 1050;
        aparece[9][0] = 1060;
        aparece[10][0] = 1061;
        aparece[11][0] = 1062;
        aparece[12][0] = 1070;
        aparece[13][0] = 1071;
        aparece[14][0] = 1080;
        aparece[15][0] = 1081;
        aparece[16][0] = 1082;
        aparece[17][0] = 1090;
        aparece[18][0] = 1100;
        aparece[19][0] = 1110;
        aparece[20][0] = 1120;
        aparece[21][0] = 1130;
        aparece[22][0] = 1140;
        aparece[23][0] = 1150;
        aparece[24][0] = 1160;
        aparece[25][0] = 1161;
        aparece[26][0] = 1170;
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
                    
                    contAmb[27][amb]++;
                    
                    contAmb[j][this.amb + 1]++;
                    
                    contAmb[27][this.amb + 1]++;
                }
            }
        }
    }
}
