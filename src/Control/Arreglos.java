package Control;

import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Operando;
import Model.Token;
import java.util.LinkedList;

public class Arreglos {
    Semantica1 sem1;
    
    public Semantica2 sem2;
    
    public LinkedList<Operando> operStack;
    
    public LinkedList<Token> opStack;
    
    public Operando lastId = null;
    
    public String[] idsimbolos; // [0] tipo [1] clase [2] idsimbolos [3] tArr [4] dimArr [5] tipoLista [6] noPar [7] funcion
    
    int[][] tArr;
    
    public boolean tupla;
    
    int nIntervalo, dimArr;

    public Arreglos(Semantica1 sem1) {
        this.sem1 = sem1;
        
        operStack =  new LinkedList();
        
        opStack = new LinkedList();
    }
    
    public void zona(int PS) {
        switch(PS) {
            
            case 810: // START ARR (en operaciones)
                
                emptyStacks();
            break;
            
            case 811: // FIN ARR (en operaciones)
                printZone(PS);
                checarArrIntervalo();
                printStacks();
                
                Operando oper = this.getLastOper();
                
                sem1.setOper(new Operando(oper.getToken(), "A"));
                
                sem1.operStack.peekLast().setArr(true);
            break;
            
            case 823: // ,
                printZone(PS);
                checarArrIntervalo();
                printStacks();
            break;

            case 824: // :
                printZone(PS);
                nIntervalo++;
            break;
            
            case 860: // START ARR
                printZone(PS);
                
                if(sem1.operStack.size() > 0) 
                    lastId = sem1.operStack.peekLast();
                
                if(lastId.getSimbolos() != null)
                    if(lastId.getSimbolos()[1].equals("arreglo"))
                        lastId.setTipo(sem1.getTipo(lastId.getSimbolos()[5]));
                
                emptyStacks();
                 
                System.out.println("\n ++ ARR / LASTID: " + this.lastId + "\n");
            break;
            
            case 861: // FIN ARR
                printZone(PS);
                checarArrIntervalo();
                printStacks();
                
                checarArrStack();
            break;
        }
    }
    
    public void printZone(int PS) {
        System.out.println("\n@ " + PS); printStacks();
    }
    
    public void checarArrStack() {
        if(idsimbolos == null) {
            while(!operStack.isEmpty()) {
                Operando dato = this.getFirstOper();
                
                setError(1030, 762, dato);
            }
        } else
            switch(idsimbolos[1]) {
                case "arreglo": arreglo(); break;
                case "lista": lista(); break;
                case "diccionario": diccionario(); break;
                case "tupla": tupla(); break;

            }
        
        printStacks();
    }
    
    public void arreglo() {
        int dim = 0;
        
        printTArr();
        
        Operando dato = new Operando(new Token("V", 1), "V", true, 
                sem2.getNoTemp("V"));
        
        if(operStack.size() == 1) { // CASO arr[x] =
            dato = this.getLastOper();
            
            if(regla1030(dato, dim)) {
                if(regla1040(dato)) {
                    regla1050(dato, tArr, dim);

                    dim++;
                }
            }
        } else {
        
            while(!operStack.isEmpty()) {
                dato = this.getFirstOper();

                if(regla1030(dato, dim)) {
                    if(regla1040(dato)) {
                        regla1050(dato, tArr, dim);
                    }
                }

                dim++;
            }
            
            
            regla1030(dato, dim, dimArr);
        }
        
    }
    public void lista() {
        int dim = 0;
        
        printTArr();
        
        Operando dato = new Operando(new Token(-18, 18, "!"), "V", true, 
                sem2.getNoTemp("V"));
        
        if(operStack.size() == 1) { // CASO arr[x] =
            dato = this.getLastOper();
            
            if(regla1030(dato, dim)) {
                if(regla1040(dato)) {
                    if(regla1050(dato, tArr, dim))
                        setTipoLista(dato);
                    dim++;
                }
            }
        } else {
        
            while(!operStack.isEmpty()) {
                dato = this.getFirstOper();

                if(regla1030(dato, dim)) {
                    if(regla1040(dato)) {
                        regla1050(dato, tArr, dim);
                    }
                }

                dim++;
            }
            
            
            regla1030(dato, dim, dimArr);
        }
        
    }
    
    public void diccionario() {
        this.printStacks();
    }
    
    public void tupla() {
        int dim = 0;
        
        dimArr = 1;
        
        while(!operStack.isEmpty()) {
            Operando dato = this.getFirstOper();
            
            if(regla1030(dato, dim)) { // DIM
                if(regla1040(dato)) { // ENT
                    if(regla1050(dato, tArr, dim)) { // LIM
                        regla1070(dato);
                        regla1071(dato, dim);
                    }
                }
            }
            
            dim++;
        }
    }
    
    public void setTipoTupla(Operando dato) {
        Operando tupla = lastId;
        
        if(tupla != null) {
            if(tupla.getSimbolos() != null) {
                
                if(dato != null) {
                    String tipo = null;
                    
                    if(isInteger(dato.getLex())) { // caso tupla[1]
                        int noPar =  Integer.parseInt(dato.getLex()) + 1;
                        
                        tipo = sem1.getTipo(sem1.amb.getTipoDatoTupla(noPar, tupla.getLex()));

                    } else { // caso tupla[x]
                        tipo = sem1.getTipo(sem1.amb.getTipoDatoTupla(1, tupla.getLex())); // SE PONE EL PRIMER TIPO
                    }
                    
                    
                    tupla.setTipo(tipo);
                }
            } else
                tupla.setTipo("V");
        }
    }
    
    public void setTipoLista(Operando dato) {
        Operando lista = lastId;
        
        if(lista != null) {
            if(lista.getSimbolos() != null) {
                
                if(dato != null) {
                    String tipo = null;
                    
                    if(isInteger(dato.getLex())) { // caso tupla[1]
                        int noPar =  Integer.parseInt(dato.getLex()) + 1;
                        
                        tipo = sem1.getTipo(sem1.amb.getTipoDatoLista(noPar, lista.getLex()));

                    } else { // caso tupla[x]
                        tipo = sem1.getTipo(sem1.amb.getTipoDatoLista(1, lista.getLex())); // SE PONE EL PRIMER TIPO
                    }
                    
                    
                    lista.setTipo(tipo);
                }
            } else
                lista.setTipo("V");
        }
    }
    
    
    // SEMANTICA 2
    public boolean regla1030(Operando dato, int dim) {
        if(dato != null) {
            if(dim < dimArr) {
               setRegla(1030, dato);

               return true;
            } else 
                setError(1030, 762, dato);

            this.printStacks();
        }
        return false;
    }
    
    public void regla1030(Operando dato, int dim, int dimArr) {
        if(dato != null) {
            if(dim < dimArr) {
               setError(1030, 762, dato);
            } else
                System.out.println("dim " + dim + " DIMARR " + dimArr);
        }
    }
    
    public void regla1031(Operando dato, int nIntervalo) {
        if(nIntervalo > 3) {
            setError(1031, 762, dato);
        }
    }
    
    public boolean regla1040(Operando dato) {
        if(dato != null) {
            if(dato.getTipo().equals("D")) {
               setRegla(1040, dato);

               return true;
            } else 
               setError(1040, 764, dato);
        }
        return false;
    }
    
    public boolean regla1050(Operando dato, int[][] tArr, int dim) {
        if(dato != null) {
            this.printZone(1050);
            this.printTArr();

            if(tArr != null) {

                int pos = 0;

                if(isInteger(dato.getLex())) { // CASO arr[1]
                    pos = Integer.parseInt(dato.getLex());

                    for (int i = 0; i < tArr.length; i++) {
                        if(tArr[i][0] == dim) {
                            if(pos < tArr[i][1]) {
                                setRegla(1050, dato);

                                return true;
                            } else {
                                setError(1050, 765, dato);

                                return false;
                            }
                        }
                    }

                } else { // CASO arr[x]
                    setRegla(1050, dato);

                    return false;
                }

                setError(1050, 765, dato);

                return false;
            } else 
               setError(1050, 765, dato);
        }
        return false;
        
    }
    
    public void regla1070(Operando dato) {
        if(dato != null) {
        
            setTipoTupla(dato);

            setRegla(1070, lastId);
        }
    }
    
    public void regla1071(Operando dato, int dim) {
        if(dato != null) {
            if(dato.getTipo().equals("D")) { // Si es decimal
                if(isInteger(dato.getLex())) { // caso tupla[1]
                    int num = Integer.parseInt(dato.getLex());

                    if(num < 0) { // Si es negativo da ERROR 1071
                        setError(1071, 769, dato);
                    } else {
                        setRegla(1071, dato);
                    }
                } else // caso tupla[x]
                    setRegla(1071, dato);
            } 
        }
    }
    
    public void setRegla(int regla, Operando dato) {
        if(dato != null) {
            String tipo = dato.getTipo();
            int line = dato.getToken().getLinea();
            
            if(dato.isTemp()) {
                sem2.setRegla(regla, sem2.getTipo(tipo), 
                        sem2.getTemp(tipo, dato.getNoTemp()),
                        line, "Acepta");
            } else {
                sem2.setRegla(regla, sem2.getTipo(tipo), dato.getLex(), 
                line, "Acepta");
            }
        }
        
        
        
    }
    
    public void setError(int regla, int error, Operando dato) {
        if(dato != null) {
            String tipo = dato.getTipo();
            int line = dato.getToken().getLinea();
            
            
            if(dato.isTemp()) {
                sem2.setRegla(regla, sem2.getTipo(tipo),
                        sem2.getTemp(tipo, dato.getNoTemp()),
                        line, "Error");
            } else {
                sem2.setRegla(regla, sem2.getTipo(tipo), dato.getLex(), 
                line, "Error");
            }
            
            sem2.setError(error, dato.getToken().getLinea(), dato.getLex());
        }
    }
    
    // INTERVALOS
    public void checarArrIntervalo() {
        if(nIntervalo != 0)
            setArrIntervalo();
    }
    
    public void setArrIntervalo() {
        int size = operStack.size();

        if(size > 1) {
            Operando oper1, oper2, oper3;
            
            int num1, num2, num3;
            
            
            if(nIntervalo == 1) { // x : x
                
                oper3 = getLastOper();
                oper2 = getLastOper();

                if(isInteger(oper3.getLex())) {
                    if(isInteger(oper2.getLex())) { // Si los dos son INT
            
                        num3 = Integer.parseInt(oper3.getLex());
                        num2 = Integer.parseInt(oper2.getLex());
                        
                        if(num2 > num3) 
                            num1 = num2 - num3;
                        else
                            num1 = num3 - num2; 
                        
                        sem2.regla1031(oper2.getToken(), num2, num3);
                        
                        operStack.add(new Operando(new Token(num1 + "", oper3.getToken().getLinea()), "D"));
                    } else
                        checarTD(oper2, oper3);
                } else
                    checarTD(oper2, oper3);
            } else { 
                if(nIntervalo == 2) { // x : x : x
                    oper3 = getLastOper();
                    oper2 = getLastOper();
                    oper1 = getLastOper();
                    
                    
                    if(isInteger(oper3.getLex())) {
                        if(isInteger(oper2.getLex())) { // Si los dos son INT
                            if(isInteger(oper1.getLex())) { // Si los tres son INT
                                num3 = Integer.parseInt(oper3.getLex());
                                num2 = Integer.parseInt(oper2.getLex());
                                num1 = Integer.parseInt(oper1.getLex());


                                sem2.regla1031(oper1.getToken(), num1, num2, num3);

                                if(num1 > num2) 
                                    num1 = (num1 - num2);
                                else
                                    num1 = (num2 - num1);


                                if(num3 < 0) // Si es dividendo es negativo
                                    num3 *= -1;

                                if((num1 % num3) != 0) 
                                    num1 = num1 / num3 + 1;
                                else 
                                    num1 /= num3;

                                operStack.add(new Operando(new Token(num1 + "", oper3.getToken().getLinea()), "D"));
                            } else
                                checarTD(oper1, oper2, oper3);
                        }  else
                            checarTD(oper1, oper2, oper3);
                    }  else
                        checarTD(oper1, oper2, oper3);
                } else {
                    oper1 = getLastOper();
                    operStack.add(new Operando(oper1.getToken(), "V", true, 
                    sem2.getNoTemp("V")));
                    
                    regla1031(oper1, nIntervalo);
                }
            }
        }
        
        nIntervalo = 0;
    }
    
    
    public void checarTD(Operando oper1, Operando oper2) {
        if(oper1.getTipo().equals("D") && 
                oper2.getTipo().equals("D")) {
            operStack.add(new Operando(oper1.getToken(), "D", true, 
                    sem2.getNoTemp("D")));
        } else
            operStack.add(new Operando(oper1.getToken(), "V", true, 
                    sem2.getNoTemp("V")));
    }
    
    public void checarTD(Operando oper1, Operando oper2, Operando oper3) {
        if(oper1.getTipo().equals("D") && 
                oper2.getTipo().equals("D") && 
                oper3.getTipo().equals("D") ) {
            operStack.add(new Operando(oper1.getToken(), "D", true, 
                    sem2.getNoTemp("D")));
        } else
            operStack.add(new Operando(oper1.getToken(), "V", true, 
                    sem2.getNoTemp("V")));
    }
    
    
    // ARRSTACKS
    public Operando getLastOper() {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.removeLast();
        else {
            Token tokenTemp = sem1.token;
            
            if(tokenTemp == null)
                tokenTemp = new Token("V", 1);
                                
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
        }
        
        return oper;
    }
    
    public Token getLastOp() {
        Token op;
        
        if(opStack.size() > 0) 
            op = opStack.removeLast();
        else 
            op = new Token("V", 1);
        
        return op;
    }
    
    public Operando getFirstOper() {
        Operando oper;
        
        if(operStack.size() > 0) 
            oper = operStack.removeFirst();
        else {
            Token tokenTemp = sem1.token;
            
            if(tokenTemp == null)
                tokenTemp = new Token("V", 1);
                                
            oper =  new Operando(tokenTemp, "V", true, sem2.getNoTemp("V"));
        }
        
        return oper;
    }
    
    public void setIdentificador(String[] idsimbolos, Token token) {
        if(idsimbolos != null) {
                operStack.offer(new Operando(token, sem1.getTipo(idsimbolos[0]), 
                Integer.parseInt(idsimbolos[2]), idsimbolos));
        } else {
            operStack.offer(new Operando(token, "V")); // Guardamos temp variant
        }
    }
    
    public void setOper(Token token, int LT) {
        if(sem1.negativo) {
            operStack.offer(new Operando(new Token(LT, 
                    token.getLinea(), "-"+token.getLexema()), sem1.getTipo(LT)));

            sem1.negativo = false;
        } else
            operStack.offer(new Operando(token, sem1.getTipo(LT)));
    }
    
    public void setOp(Token token) {
        opStack.offer(token);
    }
    
    public void printStacks() {
        System.out.println("\n*------------------------\n");
        System.out.println(" ARR - OPERSTACK:");
        
        for(Operando o : operStack) {
            if(!o.isTemp())
                System.out.println(" "+o.getToken().getLexema()+" "+o.getTipo());
            else
                System.out.println(" T"+o.getTipo());
        }
        
        System.out.println("\n ARR - OPSTACK:");
        for(Token t : opStack) 
            System.out.println(" "+t.getLexema());
        
        System.out.println("\n------------------------*\n");
    }
    
    public void emptyStacks() {
        operStack =  new LinkedList();
        
        opStack = new LinkedList();

        System.out.println("\n*--------------empty ARR stack----------*\n");
    }
   
    public boolean isInteger(String numero){
        try{
            Integer.parseInt(numero);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    // IDSIMBOLOS
    public void setIdSimbolos(String[] idsimbolos) {
        this.idsimbolos = idsimbolos;
        
        if(idsimbolos != null) {
            if(idsimbolos[4] != null)
                dimArr = Integer.parseInt(idsimbolos[4]);
            
            checarTArr();
        }
    }
    
    // TARR
    public void checarTArr() {
        if(idsimbolos[3] != null) {
            String tArr = idsimbolos[3], numS = "";
            
            this.tArr = new int[tArr.length()][2];
            
            int i = 0, num;

            if(tArr.contains(";") && tArr.contains(",")) { // 2;3,4
                for (int j = 0; j < tArr.length(); j++) {
                    if(tArr.charAt(j) == ';') {
                        numS = "";
                    } else {
                        if(tArr.charAt(j) == ',') {

                            if(isInteger(numS))
                                num = Integer.parseInt(numS);
                            else
                                num = -1;

                            this.tArr[i][0] = i + 1; // DIM
                            this.tArr[i][1] = num; // TAM
                            
                            numS = "";

                            i++;
                        } else {
                            numS += tArr.charAt(j);
                        }
                    }
                }
                
                if(isInteger(numS))
                    num = Integer.parseInt(numS);
                else
                    num = -1;

                this.tArr[i][0] = i + 1; // DIM
                this.tArr[i][1] = num; // TAM

                numS = "";
                
            } else {
                if(tArr.contains(";")) { // 2;3;4
                    for (int j = 0; j < tArr.length(); j++) {
                        if(tArr.charAt(j) == ';'){
                            if(isInteger(numS))
                                num = Integer.parseInt(numS);
                            else
                                num = -1;
                            
                            this.tArr[i][0] = i; // DIM
                            this.tArr[i][1] = num; // TAM

                            
                            numS = "";
                            
                            i++;
                        } else {
                            numS += tArr.charAt(j);
                        }
                    }
                    
                    if(isInteger(numS))
                        num = Integer.parseInt(numS);
                    else
                        num = -1;

                    this.tArr[i][0] = i; // DIM
                    this.tArr[i][1] = num; // TAM
                    
                    
                } else {// [1]
                    
                    if(isInteger(idsimbolos[3]))
                        num = Integer.parseInt(idsimbolos[3]);
                    else
                        num = -1;

                    this.tArr[0][0] = 0; // DIM
                    this.tArr[0][1] = num; // TAM
                    
                }
            }
            
            printTArr();
        }
        
    }
    
    public void printTArr() {
        
        System.out.println("\n*------------------------\n");
        System.out.println(" ARR - TARR:");
        for (int i = 0; i < tArr.length; i++) {
            System.out.println(" " + tArr[i][0] + " " + tArr[i][1]);
        }
        System.out.println("\n------------------------*\n");
    }
    
   
    
}
