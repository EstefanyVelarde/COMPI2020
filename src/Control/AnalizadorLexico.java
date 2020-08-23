package Control;

import Model.Token;
import Model.Error;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AnalizadorLexico {

    MatrizLexico matriz;

    LinkedList<Token> tokens;

    LinkedList<Error> errores;
    
    Contadores contadores;

    String lexema = "", texto;
    
    int[] cont;

    int estado = 0, columna = 0, linea = 1, caracter = 0, ind = 0;

    public AnalizadorLexico() {

        matriz = new MatrizLexico();

        tokens = new LinkedList();

        errores = new LinkedList();

    }

    public void leer(String texto) {
        this.texto = texto;
        
        if(texto.length() > 0) {
            do {
                caracter = texto.charAt(ind);

                columna = matriz.getColumna(caracter);

                estado = matriz.getEstado(estado, columna);        

                checarEstado();
            } while (ind < texto.length());

            checarFin();
        }
    }
    
    public boolean checarEstado() {
        if(token(estado)) {
            setToken(estado);
            
            return true;
        } else if(error(estado)) {
            setError(estado);
            
            return true;
        } else {
            if(!isEspacio(caracter))
                lexema += (char) caracter;
            
            ind++;
            
            setLinea();
            
            return false;
        }
    }
    
    public void checarFin() {
        if(lexema.length() > 0) {
            
            if(estado == 23 || (estado >= 14 && estado <= 18))    // Si es comentario o da error con \n
                columna = 42;
            else
                columna = 46;
            
            estado = matriz.getEstado(estado, columna);
            
            caracter = ' ';
            
            checarEstado();
        }
    }
    
    public void setLinea() {
        if(columna == 42) {
            linea++;
            
            lexema = "";
        }
    }
    
    public boolean isEspacio(int c) {
        if(estado == 0 && (c == 9 || c == 10 || c == 32))
            return true;
        else return false;
    }


    // TOKENS
    public boolean token(int estado) {
        if(estado < 0) {
            return true;
        }
        return false;
    }

    public void setToken(int token) {
       
        if(!matriz.OC(token)) {   // Si tokeniza directo
            lexema += (char) caracter;
            ind++;
            
            setLinea();
        }
        
        if(token == -47) // Complejo
            complejo();
        
        else if(token == -44)
            token = isReservada();
        
        
        
        tokens.add(new Token(token, linea, lexema));

        
        init();
    }
    
    public void complejo() {
        Token last, second, third;
        
        int size = tokens.size();
        int car;
        
        if(size > 1) {
            last = tokens.peekLast();
            
            if(last.getToken() == -7) { // + [comp]
                
                tokens.removeLast();
                
                second = tokens.peekLast();
                
                if(second.getToken() == -45 || second.getToken() == -46) { // D/F + [comp]
                    
                    tokens.removeLast();
                    
                    lexema = second.getLexema() + last.getLexema() + lexema;
                    
                    if(size > 2) {
                        
                        third = tokens.peekLast();

                        if(third.getToken() == -1) { // ( D/F + [comp]
                            
                            if(ind < texto.length()) {
                                
                                car = texto.charAt(ind);

                                if(car == 41) { // (  D/F + [comp] )
                                    
                                    tokens.removeLast();
                                    
                                    lexema = third.getLexema() + lexema + (char) car;
                                    
                                    ind++;
                                    
                                }
                            }
                        }
                    }
                } else 
                    tokens.add(last);
            }
        }
        
    }
    
    public int isReservada() {
        int token = -44;
        
        switch(lexema) {
            case "true":        token = -54;   break;
            case "false":       token = -55;   break;
            case "none":        token = -56;   break;
            
            case "is":          token = -57;   break; 
            case "isnot":          token = -58;   break; 
            
            case "in":          token = -59;   break;
            case "innot":          token = -60;   break; 
            
            case "sort":        token = -61;   break;
            case "reverse":     token = -62;   break;
            case "insert":      token = -63;   break;
            case "count":       token = -64;   break;
            case "index":       token = -65;   break;
            case "append":      token = -66;   break;
            case "extend":      token = -67;   break;
            case "pop":         token = -68;   break;
            case "remove":      token = -60;   break;
            case "random":      token = -70;   break;
            case "findall":     token = -71;   break;
            case "replace":     token = -72;   break;
            case "sample":      token = -73;   break;
            case "len":         token = -74;   break;
            case "choice":      token = -75;   break;
            case "randrange":   token = -76;   break;
            case "range":       token = -77;   break;
            case "mean":        token = -78;   break;
            case "median":      token = -79;   break;
            case "variance":    token = -80;   break;
            case "sum":         token = -81;   break;
            case "print":       token = -82;   break;
            case "println":     token = -83;   break;
            case "if":          token = -84;   break;
            case "elif":        token = -85;   break;
            case "else":        token = -86;   break;
            case "end":         token = -87;   break;
            case "wend":        token = -88;   break;
            case "for":         token = -89;   break;
            case "to":          token = -90;   break;
            case "while":       token = -91;   break;
            case "break":       token = -92;   break;
            case "continue":    token = -93;   break;
            case "return":      token = -94;   break;
            case "input":       token = -95;   break;
            case "def":         token = -96;   break;
            
            case "not":  
                        
                if(tokens.size() > 0) {
                    Token last = tokens.peekLast();
                
                    if(last.getToken() == -57) { // isnot
                        tokens.removeLast();
                        
                        lexema = last.getLexema() + lexema;
                        
                        token = -58;
                    } else {
                        if(last.getToken() == -59) { // innot
                            tokens.removeLast();
                        
                            lexema = last.getLexema() + lexema;

                            token = -60;
                        } else {
                            token = -97; 
                        }
                    }
                    
                } else
                    token = -97;   
                
            break;
            
            case "and":      token = -98;   break;
            case "as":       token = -99;   break;
            case "assert":   token = -100;   break;
            case "class":    token = -101;   break;
            case "except":   token = -102;   break;
            case "exec":     token = -103;   break;
            case "finally":  token = -104;   break;
            case "import":   token = -105;   break;
            case "lamda":    token = -106;   break;
            case "raise":    token = -107;   break;
            case "try":      token = -108;   break;
            case "with":     token = -109;   break;
        }
        
        return token;
    }

    // ERRORES
    String desc[] = {
        "Se esperaba OC que no fuera \\n o fin",
        "Se esperaba ‘",
        "Se esperaba un \\d",
        "Se esperaba un \\d o  -",
        "Se esperaba un [0-1]",
        "Se esperaba un [\\da-fA-F]",
        "Se esperaba \\w, (, ), [, ], {, }, “, ‘, #, +, -, *, /, %, &, |, ^, <, >, =, !, ;, o ,"
    };
    
    public boolean error(int estado) {
        if(estado > 499) {
            return true;
        }
        return false;
    }
    
    public void setError(int error) {
        
        lexema += (char) caracter;
        
        errores.add(new Error(error, linea, lexema, desc[error - 500], "Léxico"));
        
        if(columna == 42 || columna == 46 || columna == 41) {
            ind++; 
            
            setLinea();
        }
                
        init();
    }
    
    // CONTADORES
    public void setContadores() {
        contadores = new Contadores(linea);
        
        contadores.setContadores(tokens);
        
        contadores.setErrores(errores);
        
        cont = contadores.getContadores();
    }

    public Contadores getContadores() {
        return contadores;
    }
    
    public void setTablas(JTable tokens, JTable errores, JTable contadores) {

        Iterator it = getTokensCopy().iterator();

        DefaultTableModel model = (DefaultTableModel) tokens.getModel();
       
        model.setRowCount(0);
        
        while (it.hasNext()) {
            Token token = (Token) it.next();
            
            Object[] row = {token.getLinea(), token.getToken(), token.getLexema()};
            model.addRow(row);
        }

        it = this.errores.iterator();

        model = (DefaultTableModel) errores.getModel();
        
        model.setRowCount(0);
        
        while (it.hasNext()) {
            Model.Error error = (Model.Error) it.next();
            Object[] row = {error.getLinea(), error.getError(), error.getDesc(), error.getLexema(), error.getTipo()};
            model.addRow(row);
        }
        
        setContadores();
        
        model = (DefaultTableModel) contadores.getModel();
        
        for(int i = 0; i < cont.length; i++) {
            model.setValueAt(cont[i], i, 1);
        }
        
        
    }

    
    public void init() {
        lexema = "";
        estado = 0;
    }

    public LinkedList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }
    
    // COPIA LISTA DE TOKENS SIN COMENTARIOS
    public LinkedList<Token> getTokensCopy() {
        LinkedList<Token> tokensCopy = new LinkedList();
        
        Iterator it = tokens.iterator();
        
        while (it.hasNext()) {
            Token token = (Token) it.next();
            
            if(token.getToken() != -42 && token.getToken() != -43) // Si no son comentarios
                tokensCopy.add(new Token(token.getToken(), token.getLinea(), token.getLexema()));
        }
        
        return tokensCopy;
    }
    
    public LinkedList<Error> getErrores() {
        return errores;
    }

    public void setErrores(LinkedList<Error> errores) {
        this.errores = errores;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }
    
    
}
