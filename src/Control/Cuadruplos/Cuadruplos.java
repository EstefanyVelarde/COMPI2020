package Control.Cuadruplos;

import Control.Ambito.Ambito;
import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Control.Semantica.Semantica3;
import Model.Cuadruplo;
import Model.Etiqueta;
import Model.Funcion;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;

public class Cuadruplos {
    public Ambito amb;
    
    public Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public Semantica3 sem3;
    
    public LinkedList<Cuadruplo> cuadruplos;
    
    public LinkedList<Token> funciones;
    
    public LinkedList<Operando> opers;
    
    public LinkedList<Etiqueta> etiquetas;
    
    int[] tempCont, etiquetaCont;
    
    public Cuadruplos() {
        
        cuadruplos = new LinkedList();
        
        funciones = new LinkedList();
        
        opers = new LinkedList();
        
        etiquetas = new LinkedList();
        
        tempCont = new int[25];
        
        etiquetaCont = new int[3];
    }
    
    public void zona(int PS) {
       
        switch(PS) {
            case 864: // FOR
                printZone(PS);
                
                pushFor();
            break;
            
            case 821: // FOR
                printZone(PS);
                
                idFor = sem1.peekLastOper(null);
            break;
            
            case 80110: // main
                printZone(PS);
                
                if(funciones.isEmpty())
                    pushMain();
            break;
                
            
            case 80111: // end main
                printZone(PS);
                
                if(funciones.isEmpty())
                    popMain();
                
            break;
                
            case 1010: // IF
                printZone(PS);
                
                pushIf(sem2.oper);
            break;   
            
            
            case 1011: // WHILE
                printZone(PS);
                
            break;
                
            case 1012: // ELIF
                printZone(PS);
                
            break;     
            
            case 10101: // END
                printZone(PS);
                
                pushEnd();
            break; 
            
            case 10102: // ELSE
                printZone(PS);
                
            break; 
            
            
            case 10103: // END FOR
                printZone(PS);
                
                pushEndFor();
            break; 
            
            case 8041: // def fun
                printZone(PS);
                
                pushFuncion(amb.lastFuncionToken);
                
            break;
                
            case 1804: // enddef
                printZone(PS);
                
                popFuncion();
                
            break;
                
        }
    }
    
    public void pushEndFor() {
        if(noErrores() && !etiquetas.isEmpty()) {
            Etiqueta lastEtiqueta = etiquetas.removeLast();
            
            Cuadruplo newCuad = new Cuadruplo("", "++", lastEtiqueta.getEst2(), "", lastEtiqueta.getEst2());
            
            cuadruplos.offer(newCuad);
            
            newCuad = new Cuadruplo("", "JMP", "", "", lastEtiqueta.getSalida());
            
            cuadruplos.offer(newCuad);
            
            newCuad = new Cuadruplo(lastEtiqueta.getEst1()+ ":", "", "", "", "");
            
            cuadruplos.offer(newCuad);
        }
    }
    
    Operando idFor, forVal;
    public void setIdFor(Operando oper) {
        this.idFor = oper;
    }
    
    public void setForVal(Operando oper) {
        this.forVal = oper;
    }
    
    public void pushFor() {
        if(noErrores() && notNull(forVal) && notNull(idFor)) {
            Cuadruplo newCuad;
            
            String tipoCuad, resultado, etiqueta, JMP;
            
            int noTempCuad;
            
            JMP = getEtiqueta("FOR-E");
            
            newCuad = new Cuadruplo(JMP + ":", "", "", "", "");
            
            cuadruplos.offer(newCuad);
            
            // <
            
            tipoCuad = getTempTipoCuad("for");
            
            resultado = getTempTipoCuad("forb");
            
            newCuad = new Cuadruplo("", "<", tipoCuad, forVal.getLex(), resultado);
            
            cuadruplos.offer(newCuad);
            
            // JF
            etiqueta = getEtiqueta("FOR-E");
            
            newCuad = new Cuadruplo("", "JF", resultado, "", etiqueta); 
            
            cuadruplos.offer(newCuad);
            
            // ETIQUETA
            Etiqueta newEtiqueta = new Etiqueta("FOR", JMP, etiqueta, tipoCuad);
            
            etiquetas.offer(newEtiqueta);
            
            // =
            newCuad = new Cuadruplo("", "=", idFor.getLex(), "", tipoCuad);
            
            cuadruplos.offer(newCuad);
            
            
            
        }
    }
    
    public void pushReturnCall(Token fun, Operando dato) {
        if(noErrores()) {
            Cuadruplo newCuad = new Cuadruplo("", "call", "return", "", "");
            
            cuadruplos.offer(newCuad);
            
            String resultado = "T"+"def"+fun.getLexema();
            
            newCuad = new Cuadruplo("", "", dato.getLex(), "", resultado);
                
            cuadruplos.offer(newCuad);
        }
    }
    
    public void pushCall(Funcion fun, String resultado) {
        if(noErrores()) {
            Cuadruplo newCuad = new Cuadruplo("", "call", fun.getFunLex(), "", "");
            
            cuadruplos.offer(newCuad);
            
            int noPar = fun.getNoPar();
            
            Operando par1 = null, par2 = null;
            
            int j = 1;
            
            for (int i = 0; i < noPar; i++) {
                
                if(j == 2) {
                    par2 = fun.getParStack().get(i);
                    
                    if(notNull(par1) && notNull(par2)) {
                        newCuad = new Cuadruplo("", "", par1.getLex(), par2.getLex(), "");
                    
                        cuadruplos.offer(newCuad);
                    }
                    
                    j = 1;
                } else {
                    par1 = fun.getParStack().get(i);
                    
                    j++;
                }
                
                        
            }
            
            resultado = getTempTipoCuad(resultado);
            
            if(noPar % 2 != 0) { // si es impar
                newCuad = new Cuadruplo("", "", par1.getLex(), "", resultado);
                
                cuadruplos.offer(newCuad);
            } else
                cuadruplos.peekLast().setRes(resultado);
        }
    }
    
    public void pushEnd() {
        if(noErrores() && !etiquetas.isEmpty()) {
            Etiqueta lastEtiqueta = etiquetas.removeLast();
            
            Cuadruplo newCuad = new Cuadruplo(lastEtiqueta.getSalida() + ":", "", "", "", "");
            
            cuadruplos.offer(newCuad);
        }
    }
    
    public void pushIf(Operando exp) {
        if(noErrores() && notNull(exp)) {
            Cuadruplo newCuad;
            
            String tipoCuad;
            
            int noTempCuad;
            
            
            String resultado = getEtiqueta("IF-E");
            
            
            if(exp.isTempCuad()) {
                tipoCuad = exp.getTipoTempCuad();
                
                if(tipoCuad.contains("TL-")){
                    noTempCuad = getNoTemp("B");
                    
                    newCuad = new Cuadruplo("", "==", 
                        tipoCuad, "true", "TB"+noTempCuad); // == TL-B0
                    
                    tipoCuad = "TB"+noTempCuad;
                    
                    cuadruplos.offer(newCuad);
                }
                    
                newCuad = new Cuadruplo("", "JF", 
                        tipoCuad, "", resultado); // [TEMP]
            } else { // if true: if x:
                
                noTempCuad = getNoTemp("B");
                    
                newCuad = new Cuadruplo("", "==", 
                    exp.getLex(), "true", "TB"+noTempCuad); // == x true TB0

                tipoCuad = "TB"+noTempCuad;

                cuadruplos.offer(newCuad);
                
                newCuad = new Cuadruplo("", "JF", 
                        tipoCuad, "", resultado); // [1], [x]
            }
            
            cuadruplos.offer(newCuad);
            
            // ETIQUETA
            Etiqueta newEtiqueta = new Etiqueta("IF", resultado, "", "");
            
            etiquetas.offer(newEtiqueta);
        }
    }
    
    public void pushLista(Operando lista, Operando pos) {
        if(noErrores()) {
            Cuadruplo newCuad;
            
            String tipoStruct = "lista";
                    
            if(isIdentificador(lista))
                tipoStruct = lista.getSimbolos()[1];
            
            // lista
            newCuad = new Cuadruplo("", tipoStruct, lista.getLex(), "", "");
            
            this.cuadruplos.offer(newCuad);  // Agregamos cuadruplo
            
            // RES TL0
            String tipoCuad = "L";
            
            int noTempCuad = getNoTemp(tipoCuad);
            
            String tipoTempCuad = "T" + tipoCuad + noTempCuad;
            
            if(pos.isTempCuad())
                newCuad = new Cuadruplo("", "", 
                        pos.getTipoTempCuad(), "", tipoTempCuad); // [TEMP]
            else
                newCuad = new Cuadruplo("", "", 
                        pos.getLex(), "", tipoTempCuad); // [1], [x]
            
            this.cuadruplos.offer(newCuad); // Agregamos cuadruplo
            
            pos.setTipoCuad(tipoCuad);
            
            pos.setNoTempCuad(noTempCuad);
            
            pos.setTempCuad(true);
            
            // valor
            tipoCuad = "L-"+lista.getTipo();
            
            noTempCuad = getNoTemp(tipoCuad);
            
            tipoTempCuad = "T" + tipoCuad + noTempCuad;
            
            newCuad = new Cuadruplo("", "valor", 
                        lista.getLex(), pos.getTipoTempCuad(), tipoTempCuad); // [1], [x]
            
            this.cuadruplos.offer(newCuad); // Agregamos cuadruplo
            
            lista.setTipoCuad(tipoCuad);
            
            lista.setNoTempCuad(noTempCuad);
            
            lista.setTempCuad(true);
            
            printCuadruplos();
        }
    }
    
    public void pushOperacion(Token op, Operando oper1, Operando oper2, String tempTipo,
            Operando tempOper) {
        
        if(noErrores()) {
            Cuadruplo newCuad;
            
            tempOper.setTipoCuad(tempTipo);
            
            tempOper.setNoTempCuad(getNoTemp(tempTipo));
            
            tempOper.setTempCuad(true);
            
            if(oper1.isTempCuad())
                newCuad = new Cuadruplo("", op.getLexema(), oper1.getTipoTempCuad(), 
                            oper2.getLex(), tempOper.getTipoTempCuad());
            else
                newCuad = new Cuadruplo("", op.getLexema(), oper1.getLex(), 
                        oper2.getLex(), tempOper.getTipoTempCuad());

            this.cuadruplos.offer(newCuad);
            
            opers.offer(tempOper); // Guardamos instancia del objeto de operacion
            
            printCuadruplos();
        }
    }
    
    public void pushFuncion(Token token) {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("def", token.getLexema(), "", "", ""));

            printCuadruplos();

            funciones.add(token);

            printFunciones();
        }
    }
    
    public void popFuncion() {
        if(noErrores()) {
            if(!funciones.isEmpty()) {
            Token token = funciones.removeLast();

            this.cuadruplos.offer(new Cuadruplo("enddef", token.getLexema(), "", "", ""));
            }
            printCuadruplos();


            printFunciones();
        }
    }
    
    public void pushMain() {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("PPAL", "Main:", "", "", ""));

            printCuadruplos();

        }
    }
    
    public void popMain() {
        if(noErrores()) {
            this.cuadruplos.offer(new Cuadruplo("ENDMAIN",  "", "", "", ""));
            
            printCuadruplos();
        }
    }
    
    
    // PRINTS
    public void printZone(int PS) {
        System.out.println("\n CUAD @ " + PS);  printCuadruplos(); printFunciones();
    }
    
    public void printCuadruplos() {
        System.out.println("\n//////////////////////// CUADRUPLOS /////////////////////////");
        System.out.println("ETIQ\tACCION\tARG1\tARG2\tRESULTADO");
        
        for (Cuadruplo c : cuadruplos) {
            System.out.println(c.getEtiqueta() + "\t" + c.getAccion() + "\t" + c.getArg1() + "\t" + c.getArg2() +
                    "\t" + c.getRes());
        }
        System.out.println("");
    }
    
    public void printFunciones() {
        System.out.println("\n*------------- CUAD -----------\n");
        System.out.println(" FUNCIONES:");
        
        for(Token o : funciones) {
            System.out.println(o.getLexema());
        }
        
        
        System.out.println("\n------------------------*\n");
    }
    
    // ISSSSS
    
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
    
    public boolean notNull(Object dato) {
        return dato != null;
    }
    
    public boolean isIdentificador(Operando oper) {
        if(notNull(oper))
            if(notNull(oper.getSimbolos()))
                return true;
        
        return false;
    }
    
    public boolean noErrores() {
        return amb.errores.isEmpty();
    }
    
    public String getTempTipoCuad(String tipo) {
        int noTemp = getNoTemp(tipo);
        
        if(noTemp != -1)
            return "T"+tipo+noTemp;
        else
            return tipo;
    }
    
    public int getNoTemp(String tipo) {
        int no = 0;
        
        switch(tipo) {
            case "B":   no = 1 + tempCont[0]++;  break;
            case "CH":  no = 1 + tempCont[1]++;  break;
            case "C":   no = 1 + tempCont[2]++;  break;
            case "D":   no = 1 + tempCont[3]++;  break;
            case "F":   no = 1 + tempCont[4]++;  break;
            case "CM":  no = 1 + tempCont[5]++;  break;
            case "DB":  no = 1 + tempCont[6]++;  break;
            case "DH":  no = 1 + tempCont[7]++;  break;
            case "DO":  no = 1 + tempCont[8]++;  break;
            case "N":   no = 1 + tempCont[9]++;  break;
            case "T":   no = 1 + tempCont[10]++; break;
            case "L":   no = 1 + tempCont[11]++; break;
            case "A":   no = 1 + tempCont[12]++; break;
            case "DIC": no = 1 + tempCont[13]++; break;
            case "L-D": no = 1 + tempCont[14]++; break;
            case "L-DO":
                        no = 1 + tempCont[15]++; break;     
            case "L-DB":
                        no = 1 + tempCont[16]++; break;
            case "L-DH":
                        no = 1 + tempCont[17]++; break;
            case "L-F": no = 1 + tempCont[18]++; break;
            case "L-C": no = 1 + tempCont[19]++; break;
            case "L-CH":
                        no = 1 + tempCont[20]++; break;  
            case "L-CM":
                        no = 1 + tempCont[21]++; break;    
            case "L-B": no = 1 + tempCont[22]++; break;     
            case "for": no = 1 + tempCont[23]++; break;      
            case "forb": no = 1 + tempCont[24]++; break;     
            case "none": no = -1;
        }
        
        return no;
    }
    
    public int getNoEtiqueta(String tipo) {
        int no = 0;
        
        switch(tipo) {
            case "IF-E": no = 1 + etiquetaCont[0]++; break;
            case "FOR-E":
                        no = 1 + etiquetaCont[1]++; break;     
            case "WHI-E":
                        no = 1 + etiquetaCont[2]++; break;     
        }
        
        return no;
    }
    
    public String getEtiqueta(String tipo) {
        return tipo+getNoEtiqueta(tipo);
    }
    
}
