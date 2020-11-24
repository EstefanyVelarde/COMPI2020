package Control.Semantica;

import Model.Regla;
import java.util.LinkedList;

public class ContSemantica3 {
    public LinkedList<Regla> listaReglas;
    
    public String[] funcion =  {
            "append",
            "append_par1",
            "choice",
            "choice_par1",
            "count",
            "count_par1",
            "extend",
            "extend_par1",
            "findall",
            "findall_par1",
            "findall_par2",
            "index",
            "index_par1",
            "insert",
            "insert_par1",
            "insert_par2",
            "len",
            "len_par1",
            "mean",
            "mean_par1",
            "pop",
            "pop_par1",
            "randrange",
            "randrange_par1",
            "randrange_par2",
            "remove",
            "remove_par1",
            "replace",
            "replace_par1",
            "replace_par2",
            "replace_par3",
            "reverse",
            "sample",
            "sample_par1",
            "sample_par2",
            "sort",
            "sum",
            "sum_par1",
            "variance",
            "variance_par1",
            "print_parN"
        };
    
    
    public int[][] salida;
    

    public ContSemantica3() {
        salida = new int[funcion.length][5];
        
    }
    
    
    public void setContSalida() {
        for (int i = 0; i < listaReglas.size(); i++) {
            String fun = listaReglas.get(i).getFuncion();
            
            String edo = listaReglas.get(i).getEdo();
            
            int id = listaReglas.get(i).getId();
            
            int tipo = 0;
            
            for (int j = 0; j < funcion.length; j++) {
                if(fun.equals(funcion[j])) {
                    tipo = getTipo(id);
                    
                    salida[j][tipo]++;
        
                    switch(edo) {
                        case "Acepta":
                            salida[j][3]++;
                        break;

                        case "Error":
                            salida[j][4]++;
                        break;
                    }
                    
                    
                    break;
                    
                } else {
                    if(fun.contains("print_par") && funcion[j].equals("print_parN")) { // print_parN
                        tipo = getTipo(id);
                    
                        salida[j][tipo]++;

                        switch(edo) {
                            case "Acepta":
                                salida[j][3]++;
                            break;

                            case "Error":
                                salida[j][4]++;
                            break;
                        }
                        
                        break;
                    }
                }
            }
        }
    }
    
    public int getTipo(int id) {
        
        if(isEntrada(id)) 
            return 0;
        else
            if(isSalida(id))
                return 1;
            else
                if(isUso(id))
                    return 2;
        
        return 0;
    }
    
    public boolean isEntrada(int id) {
        return (id >= 2001 && id <= 2008) || id == 2021;
    }
    
    public boolean isSalida(int id) {
        return id >= 2009 && id <= 2016;
    }
    
    public boolean isUso(int id) {
        return id >= 2017 && id <= 2020;
    }
    
    public void printSalida() {
        for (int i = 0; i < funcion.length; i++) {
                System.out.println(funcion[i] + " " + salida[i][0] + " " + salida[i][1] 
                        + " " + salida[i][2] + " " + salida[i][3] + " " + salida[i][4]);
            
        }
    }
}
