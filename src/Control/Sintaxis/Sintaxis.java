package Control.Sintaxis;

import Control.Ambito.Ambito;
import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import Model.Token;
import Model.Error;
import java.util.LinkedList;

public class Sintaxis {
    
    MatrizSintaxis matriz;
    
    Ambito ambito;
    
    Semantica1 semantica1;
    
    Semantica2  semantica2;

    LinkedList<Integer> prodStack;
    
    LinkedList<Token> tokens;
    
    LinkedList<Error> errores;
    
    
    public Sintaxis(LinkedList<Token> tokens, LinkedList<Error> errores, 
            Ambito ambito, Semantica1 semantica1, Semantica2 semantica2) {
        matriz = new MatrizSintaxis();
        
        this.tokens = tokens;
        
        this.tokens.addLast(new Token(-97, tokens.getLast().getLinea(), "$"));
        
        this.errores = errores;
        
        prodStack = new LinkedList();
        
        addProdStack(0);
        
        // INIT AMBITO
        this.ambito = ambito;
        
        this.ambito.initAmbito(prodStack, tokens, errores);
        
        // INIT SEMANTICA
        this.semantica1 = semantica1;
        
        this.semantica2 = semantica2;
        
        this.semantica1.addSem2(this.semantica2);
        
        this.ambito.sem2 = this.semantica2;
        
    }
    
    public void analizarTokens() {
        while(!prodStack.isEmpty() && !tokens.isEmpty()) {
            
            int PS = prodStack.peekLast();
            
            int LT = tokens.peekFirst().getToken();
            
            if(PS > 799) { 
                ambito.zona(PS);
                
                semantica1.zona(PS);
                
                semantica2.zona(PS, LT);
                
                prodStack.removeLast();
                
                continue;
            }
            
            if(PS >= 0 && LT >= -97 ) { // Si PS es NT y token esta en matriz
                
                int valor = matriz.getValor(PS, LT);
                
                if(produccion(valor)) 
                    setProduccion(valor);
                else 
                    if(epsilon(valor)) 
                        setEpsilon();
                    else 
                        if(error(valor)) {
                            setError(valor);
                        }
            } else {
                if(terminales(PS, LT)) { 
                    ambito.checar(LT); // CHECA AMBITO
                    
                    if(!ambito.declaracion) // SI ESTA EN ZONA DE EJECUCION
                        semantica1.checar(LT); // CHECA SEMANTICA
                    
                    setTerminales();
                } else {
                    setError(633);
                    System.out.println("-- FINAL INESPERADO "
                            + "\n\tPS: " + prodStack.toString() 
                            + "\n\tLT: " + LT);
                    break;
                }
            }
            
            
        }
        
        
        ambito.setContador();
        semantica2.setContador(ambito.contAmb);
    }
    
    // PRODUCCION
    public boolean produccion(int valor) {
        return valor >= 0 && valor < 599;
    }
    
    public void setProduccion(int id) {
        prodStack.removeLast();
        
        addProdStack(id);
    }
    
    public void addProdStack(int id) {
        for (int i = prod[id].length -1; i > 0; i--) {
            prodStack.add(prod[id][i]);
        }
    }
    
    // EPSILON
    public boolean epsilon(int valor) {
        return valor == -1;
    }
    
    public void setEpsilon() {
        prodStack.removeLast();
    }
    
    // TERMINALES
    public boolean terminales(int PS, int LT) {
        return matriz.terminales(PS, LT);
    }
    
    public void setTerminales() {
        tokens.removeFirst();
        
        prodStack.removeLast();
    }
    
    
    // ERROR
    public boolean error(int valor) {
        return valor >= 600;
    }
    
    public void setError(int error) {
        Token token = tokens.removeFirst();
        errores.add(new Error(error, token.getLinea(), token.getLexema(), desc[error - 600], "Sintaxis"));
    }
    
    String desc[] = { 
        "Está de más",
        "Se esperaba un def id {",
        "Se esperaba un def id ",
        "Se esperaba un id",
        "Se esperaba una ,",
        "Se esperaba un ;",
        "Se esperaba un ConstFlotante ConstCadena ConstCaracter Decimal Binario Hexadecimal Octar ConstCompleja true false ( [ range { None",
        "Se esperaba un Decimal Binario Hexadecimal Octal",
        "Se esperaba un ( [ range {",
        "Se esperaba un :",
        "Se esperaba un ConstFlotante ConstCadena ConstCaracter Decimal Binario Hexadecimal Octar ConstCompleja true false ( [ range { None findall replace sample len choice randrange mean median variance sum ++ -- id",
        "Se esperaba un * / // %",
        "Se esperaba un **",
        "Se esperaba un + -",
        "Se esperaba un !",
        "Se esperaba un ||",
        "Se esperaba un << >>",
        "Se esperaba un [",
        "Se esperaba un ; ,",
        "Se esperaba un =  +=  /=  *=  -=  //=  **=  %=",
        "Se esperaba un ++ --",
        "Se esperaba un ++  --  . [  =  +=  /=  *=  -=  //=  **=  %=",
        "Se esperaba un ConstFlotante ConstCadena ConstCaracter Decimal Binario Hexadecimal Octar ConstCompleja true false ( [ range { None findall replace sample len choice randrange mean median variance sum ++ -- id input",
        "Se esperaba un ConstCadena",
        "Se esperaba un print println if for while break continue return ConstFlotante ConstCadena ConstCaracter Decimal Binario Hexadecimal Octar ConstCompleja true false ( [ range { None findall replace sample len choice randrange mean median variance sum ++ -- id",
        "Se esperaba un elif else End",
        "Se esperaba un <  <=  ==  !=  >= >  IS  ISNOT  IN  INNOT ",
        "Se esperaba un &",
        "Se esperaba un ^",
        "Se esperaba un |",
        "Se esperaba un && ##",
        "Se esperaba un Sort Reverse insert Count index Append Extend pop remove",
        "Se esperaba un findall replace sample len choice randrange mean median variance sum random",
        "Final inesperado"
    };
    
    // PRODUCCIONES
    int prod[][] = {
        {0, 1, -96}, // S0 -> PROGRAM $
        {1, 2, 800, -46, 36, 5, -47, 801}, // PROGRAM -> A0 @ { EST A3 } @
        {2, -95, 804, -2, -44, 802, 3, 807, -45, 1, 803, 1150, -51, 2}, // A0 -> def @ id ( @ A1 @ ) PROGRAM @ ; A0
        {2, 805, -2, -36, 6, -51, 2}, // A0 -> @ id = CONSTANTE ; A0
        {3, 806, -2, 4}, // A1 -> @ id A2
        {4, -53, 806, -2, 4}, // A2 -> , @ id A2
        {5, -51, 862, 36, 5}, // A3 -> ; @ EST A3
        {6, -3}, // CONSTANTE -> Constflotante
        {6, -4}, // CONSTANTE -> Constcadena
        {6, -5}, // CONSTANTE -> Constcaracter
        {6, 7}, // CONSTANTE -> CONSTENTERO
        {6, -6}, // CONSTANTE -> Constcompleja
        {6, -11}, // CONSTANTE -> true
        {6, -12}, // CONSTANTE -> false
        {6, 8}, // CONSTANTE -> LIST-TUP-RANGOS
        {6, -13}, // CONSTANTE -> None
        {7, -10}, // CONSTENTERO -> Octal
        {7, -8}, // CONSTENTERO -> Binario
        {7, -9}, // CONSTENTERO -> Hexadecimal
        {7, -7}, // CONSTENTERO -> Decimal
        {8, 808, -44, 20, 9, -45, 809}, // LIST-TUP-RANGOS -> @ ( OR B0 ) @
        {8, 810, 24, 811}, // LIST-TUP-RANGOS -> @ ARR @
        {8, 812, -76, -44, 813, 7, -53, 814, 7, -53, 815, 7, -45, 816}, // LIST-TUP-RANGOS -> @ range ( @ CONSTENTERO , @ CONSTENTERO , @ CONSTENTERO ) @
        {8, 817, -46, 6, 10, 11, -47, 819}, // LIST-TUP-RANGOS -> @ { CONSTANTE B1 B2 } @
        {9, -53, 20, 9}, // B0 -> , OR B0
        {10, -50, 818, 6, 11}, // B1 -> : CONSTANTE B2
        {11, -53, 6, 10, 11}, // B2 -> , CONSTANTE B1 B2
        {12, 14, 13}, // TERMINOPASCAL -> ELEVACION C0
        {13, -14, 14, 850, 13}, // C0 -> * ELEVACION @ C0
        {13, -16, 14, 850, 13}, // C0 -> / ELEVACION @ C0
        {13, -17, 14, 850, 13}, // C0 -> // ELEVACION @ C0
        {13, -18, 14, 850, 13}, // C0 -> % ELEVACION @ C0
        {14, 29, 15}, // ELEVACION -> FACTOR D0
        {15, -15, 29, 850, 15}, // D0 -> ** FACTOR @ D0
        {16, 12, 17}, // SIMPLEEXP-PAS -> TERMINOPASCAL E0
        {17, -35, 12, 850, 17}, // E0 -> - TERMINOPASCAL @ E0
        {17, -34, 12, 850, 17}, // E0 -> + TERMINOPASCAL @ E0
        {18, 38, 19}, // NOT -> EXP-PAS F0
        {19, -33, 38, 850, 19}, // F0 -> ! EXP-PAS @ F0
        {20, 46, 21}, // OR -> AND G0
        {21, -20, 46, 850, 21}, // G0 -> || AND @ G0
        {22, 16, 23}, // OPBIT -> SIMPLEEXP-PAS H0
        {23, -24, 16, 850, 23}, // H0 -> << SIMPLEEXP-PAS @ H0
        {23, -25, 16, 850, 23}, // H0 -> >> SIMPLEEXP-PAS @ H0
        {24, -48, 25, -49}, // ARR -> [ I0 ]
        {25, 20, 26, 27}, // I0 -> OR I1 I2 
        {26, -51, 822, 25}, // I1 -> ; @ I0
        {26, -53, 823, 25}, // I1 -> , @ I0
        {27, -50, 824, 25}, // I2 -> : @ I0
        {28, -36}, // ASIGN -> =
        {28, -37}, // ASIGN -> +=
        {28, -39}, // ASIGN -> /=
        {28, -41}, // ASIGN -> *=
        {28, -38}, // ASIGN -> -=
        {28, -40}, // ASIGN -> //=
        {28, -42}, // ASIGN -> **=
        {28, -43}, // ASIGN -> %=
        {29, 6}, // FACTOR -> CONSTANTE
        {29, 49}, // FACTOR -> FUNCION
        {29, 30, -2, 844, 31}, // FACTOR -> J0 id @ J1
        {30, -54}, // J0 -> ++
        {30, -55}, // J0 -> --
        {31, 860, 24, 861, 851, 32}, // J1 -> @ ARR @ @ J2
        {31, 851, 28, 33}, // J1 -> @ ASIGN J3
        {31, 845, -44, 35, -45, 846}, // J1 -> @ ( J5 ) @
        {31, -54}, // J1 -> ++
        {31, -55}, // J1 -> --
        {31, -52, 48}, // J1 -> . FUNLIST
        {32, 28, 33}, // J2 -> ASIGN J3
        {33, 20, 852}, // J3 -> OR @
        {33, -94, -44, 34, -45}, // J3 -> input ( J4 )
        {34, -4}, // J4 -> ConstCadena
        {35, 20, 9}, // J5 -> OR B0
        {36, -81, -44, 20, 9, -45}, // EST -> print ( OR B0 )
        {36, -82, -44, 35, -45}, // EST -> println ( J5 )
        {36, -83, 20, 1010, -50, 36, 5, 37}, // EST -> if OR @ : EST A3 K0
        {36, -88, 802, 801, 820, 20, 821, 800, -89, 863, 20, 864, -50, 36, 5, -86, 803}, // EST -> for @ @ @ OR @ to @ OR @ : EST A3 End @
        {36, -90, 20, 1011, -50, 36, 5, -87}, // EST -> while OR : EST A3 wend
        {36, -91}, // EST -> Break
        {36, -92}, // EST -> Continue
        {36, -93, 20, 1140}, // EST -> Return OR @
        {36, 20}, // EST -> OR
        {37, -85, 36, 5, -86}, // K0 -> else EST A3 End
        {37, -84, 20, 1012, -50, 36, 5, 37}, // K0 -> elif OR @ : EST A3 K0
        {37, -86}, // K0 -> End
        {38, 44, 39}, // EXP-PAS -> ORLOG L0
        {39, -27, 44, 850, 39}, // L0 -> < ORLOG @ L0
        {39, -28, 44, 850, 39}, // L0 -> <= ORLOG @ L0
        {39, -30, 44, 850, 39}, // L0 -> == ORLOG @ L0
        {39, -29, 44, 850, 39}, // L0 -> != ORLOG @ L0
        {39, -31, 44, 850, 39}, // L0 -> >= ORLOG @ L0
        {39, -32, 44, 850, 39}, // L0 -> > ORLOG @ L0
        {39, -56, 44, 850, 39}, // L0 -> IS ORLOG @ L0
        {39, -57, 44, 850, 39}, // L0 -> ISNOT ORLOG @ L0
        {39, -58, 44, 850, 39}, // L0 -> IN ORLOG @ L0
        {39, -59, 44, 850, 39}, // L0 -> INNOT ORLOG @ L0
        {40, 22, 41}, // ANDLOG -> OPBIT M0
        {41, -21, 22, 850, 41}, // M0 -> & OPBIT @ M0
        {42, 40, 43}, // XORLOG -> ANDLOG O0
        {43, -26, 40, 850, 43}, // O0 -> ^ ANDLOG @ O0
        {44, 42, 45}, // ORLOG -> XORLOG P0
        {45, -19, 42, 850, 45}, // P0 -> | XORLOG @ P0
        {46, 18, 47}, // AND -> NOT Q0
        {47, -22, 18, 850, 47}, // Q0 -> && NOT @ Q0
        {47, -23, 18, 850, 47}, // Q0 -> ## NOT @ Q0
        {48, -60, -44, 35, -45}, // FUNLIST -> Sort ( J5 )
        {48, -61, -44, 35, -45}, // FUNLIST -> Reverse ( J5 )
        {48, -63, -44, 35, -45}, // FUNLIST -> Count ( J5 )
        {48, -64, -44, 35, -45}, // FUNLIST -> index ( J5 )
        {48, -65, -44, 35, -45}, // FUNLIST -> append ( J5 )
        {48, -66, -44, 35, -45}, // FUNLIST -> extend ( J5 )
        {48, -67, -44, 35, -45}, // FUNLIST -> pop ( J5 )
        {48, -68, -44, 35, -45}, // FUNLIST -> remove ( J5 )
        {48, -62, -44, 35, -45}, // FUNLIST -> insert ( J5 )
        {49, -70, -44, 35, -45}, // FUNCIONES -> findall ( J5 )
        {49, -71, -44, 35, -45}, // FUNCIONES -> replace ( J5 )
        {49, -73, -44, 35, -45}, // FUNCIONES -> len ( J5 )
        {49, -72, -44, 35, -45}, // FUNCIONES -> Sample ( J5 )
        {49, -74, -44, 35, -45}, // FUNCIONES -> choice ( J5 )
        {49, -69, -44, 35, -45}, // FUNCIONES -> random ( J5 )
        {49, -75, -44, 35, -45}, // FUNCIONES -> randrange ( J5 )
        {49, -77, -44, 35, -45}, // FUNCIONES -> mean ( J5 )
        {49, -78, -44, 35, -45}, // FUNCIONES -> median ( J5 )
        {49, -79, -44, 35, -45}, // FUNCIONES -> variance ( J5 )
        {49, -80, -44, 35, -45},  // FUNCIONES -> sum ( J5 )
        {7, 853, -35, -7} // CONSTENTERO -> - Decimal
    };
}