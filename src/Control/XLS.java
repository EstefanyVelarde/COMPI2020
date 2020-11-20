package Control;

import Control.Ambito.ContAmbito;
import Control.Lexico.ContLexico;
import Control.Semantica.ContSemantica1;
import Control.Semantica.ContSemantica2;
import Control.Semantica.ContSemantica3;
import Model.Asign;
import Model.Error;
import Model.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class XLS {
    
    ContLexico contLexico;
    ContAmbito contAmbito;
    ContSemantica1 contSemantica1;
    ContSemantica2 contSemantica2;
    ContSemantica3 contSemantica3;

    LinkedList<Token> tokens;
    LinkedList<Model.Error> errores;
    
    public XLS(LinkedList<Token> tokens, LinkedList<Error> errores, 
            ContLexico contLexico, ContAmbito contAmbito, 
            ContSemantica1 contSemantica1, ContSemantica2 contSemantica2, 
            ContSemantica3 contSemantica3) {
        
        this.tokens = tokens;
        this.errores = errores;
        this.contLexico = contLexico;
        this.contAmbito = contAmbito;
        this.contSemantica1 = contSemantica1;
        this.contSemantica2 = contSemantica2;
        this.contSemantica3 = contSemantica3;
    }

    public void crearExcel() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();  
        
        String nombreArchivo="Estefany-Velarde-Semantica3-"+ dtf.format(now) +".xls";
        String rutaArchivo= nombreArchivo;
        
        String nombreHoja1="Lista de Tokens";
        String nombreHoja2="Lista de Errores";
        String nombreHoja3="Contadores";
        String nombreHoja4="Ámbito";
        String nombreHoja5="Tabla de simbolos";
        String nombreHoja6="Semántica 1";
        String nombreHoja7="Semántica 2 Tabla";
        String nombreHoja8="Semántica 2 Cont";
        String nombreHoja9="Semántica 2 Amb";
        String nombreHoja10="Semántica 3 Tabla";

        Workbook libro= new HSSFWorkbook();
        Sheet hoja1 = libro.createSheet(nombreHoja1);
        Sheet hoja2 = libro.createSheet(nombreHoja2);
        Sheet hoja3 = libro.createSheet(nombreHoja3);
        Sheet hoja4 = libro.createSheet(nombreHoja4);
        Sheet hoja5 = libro.createSheet(nombreHoja5);
        Sheet hoja6 = libro.createSheet(nombreHoja6);
        Sheet hoja7 = libro.createSheet(nombreHoja7);
        Sheet hoja8 = libro.createSheet(nombreHoja8);
        Sheet hoja9 = libro.createSheet(nombreHoja9);
        Sheet hoja10 = libro.createSheet(nombreHoja10);

        //cabecera de la hoja de excel
        String [] Cabecera1= new String[]{"Linea", "Token","Lexema"};
        String [] Cabecera2= new String[]{"Linea", "Error","Descripcion","Lexema", "Tipos"};
        String [] Cabecera3= new String[]{"Errores", "Identificadores","Comentarios","Palabras Reservadas",
                                            "CE-DEC","CE-BIN","CE-HEX","CE-OCT","CText","CFLOAT","CNCOMP","CCAR",
                                            "Aritmeticos","Monogamo","Logico","Bit","Identidad","Puntuacion",
                                            "Agrupacion","Asignacion","Relacional"};
        String [] Cabecera4= new String[]{"Ámbito","Decimal","Binario","Octal","Hexadecimal","Flotante","Cadena", "Caracter",
                                            "Compleja","Booleana","None","Arreglo","Tuplas","Listas","Rango",
                                            "Diccionarios","Datos de Estructuras","Total/Ámbito"};
        String [] Cabecera5= new String[]{"id", "tipo", "clase", "amb", "tArr", "tipoLista", "dimArr", "valor", "noPar", "llave", "tpArr"};
        String [] Cabecera6= new String[]{"Línea", "TD", "TDO", "TDB", "TDH", "TF", "TC", "TCH", "TCM", "TB", "TT", "TL", "TA", "TDIC", "TV", "Asignación", "Errores"};
        String [] Cabecera7= new String[]{"Regla", "Tope pila", "Valor real", "Linea", "Estado", "Ambito"};
        String [] Cabecera8= new String[]{"Regla", "Aparece", "Aceptada", "Errores"};
        String [] Cabecera9= new String[]{"Regla", "Totales"};
        String [] Cabecera10= new String[]{"Regla", "Funcion", "Tope pila", "Valor real", "Linea", "Estado", "Ambito"};

        //poner negrita a la cabecera
        CellStyle style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        
        // LISTA DE TOKENS
        Row row=hoja1.createRow(0);
        for (int j = 0; j <Cabecera1.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera1[j]);
           
        }
        
        for (int i = 0; i < tokens.size(); i++) {
                row=hoja1.createRow(i + 1);
                
                for (int j = 0; j <Cabecera1.length; j++) {
          
                        Cell cell= row.createCell(j);
                        switch(j){
                            case 0:
                                cell.setCellValue(tokens.get(i).getLinea());
                                break;
                            case 1:
                                cell.setCellValue(tokens.get(i).getToken()); 
                                break;
                            case 2:
                                cell.setCellValue(tokens.get(i).getLexema()); 
                                break;
                        }

                    			
                }
            
            
        }
        
        
        // LISTA DE ERRORES
        row=hoja2.createRow(0);
        for (int j = 0; j <Cabecera2.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera2[j]);
           
        }
        
        for (int i = 0; i < errores.size(); i++) {
            row=hoja2.createRow(i+1);
            
            for (int j = 0; j <Cabecera2.length; j++) {
                    Cell cell= row.createCell(j);

                    switch(j){
                        case 0:
                            cell.setCellValue(errores.get(i).getLinea());
                            break;
                        case 1:
                            cell.setCellValue(errores.get(i).getError()); 
                            break;
                        case 2:
                            cell.setCellValue(errores.get(i).getDesc()); 
                            break;
                        case 3:
                            cell.setCellValue(errores.get(i).getLexema()); 
                            break;
                        case 4:
                            cell.setCellValue(errores.get(i).getTipo()); 
                            break;
                    }

               			
            }
        }
        
        // CONTADORES
        int[] arr = contLexico.getContadores();
        
        row=hoja3.createRow(0);
        for (int j = 0; j <Cabecera3.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera3[j]);
           
        }
        
        row=hoja3.createRow(1);

        for (int j = 0; j <Cabecera3.length; j++) {		
            Cell cell= row.createCell(j);

            for (int k = 0; k < 21; k++) {
                if(j == k)
                    cell.setCellValue(arr[j]);
            }

        }

        // AMBITO
        int[][] ambito = contAmbito.getContador();
        int[] total = contAmbito.getTotal();
        
        row=hoja4.createRow(0);
        for (int j = 0; j <Cabecera4.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera4[j]);
           
        }
        
        for (int i = 0; i < ambito.length; i++) {
            row=hoja4.createRow(i+1);
            
            for (int j = 0; j < 18; j++) {
                Cell cell= row.createCell(j);
                cell.setCellValue(ambito[i][j]);
            }
        }
        
        row=hoja4.createRow(ambito.length + 1);
        for (int j = 0; j < 18; j++) {
            Cell cell= row.createCell(j);
            
            if(j == 0)
                cell.setCellValue("Total");
            else
                cell.setCellValue(total[j]);
        }
        
        
        
        
        
        // TABLA SIMBOLOS
        row=hoja5.createRow(0);
        for (int j = 0; j <Cabecera5.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera5[j]);
           
        }
        
        
        try {
            ResultSet rs = contAmbito.getSimbolos();
            int j = 1, i;
            while (rs.next()) {   
                row=hoja5.createRow(j);
                i = 2;
                while(i <= 12) {
                    Cell cell= row.createCell(i-2);
                    cell.setCellValue(rs.getString(i++));
                }
                
                j++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(XLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        // SEMANTICA 1
        row=hoja6.createRow(0);
        for (int j = 0; j <Cabecera6.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera6[j]);
           
        }
        
        for (int i = 0; i < contSemantica1.asigns.size(); i++) {
            row=hoja6.createRow(i+1);
            
            Asign asign = contSemantica1.asigns.get(i);
            
            Cell cell = row.createCell(0); //Linea
            cell.setCellValue(asign.getLine());
            
            int[] temp = asign.getTemp();
            
            for (int j = 0; j < temp.length; j++) { // Temps
                cell = row.createCell(j + 1);
                cell.setCellValue(temp[j]);
            }
            
            cell = row.createCell(temp.length + 1); // Asign
            cell.setCellValue(asign.getAsign());
            
            cell = row.createCell(temp.length + 2); // Error
            cell.setCellValue(asign.getErrors());
        }
        
        int[] tempTotal = contSemantica1.getTempTotal();
        
        int errorsTotal = contSemantica1.getErrorsTotal();
        
        row=hoja6.createRow(contSemantica1.asigns.size() + 1);
        
        for (int j = 0; j < tempTotal.length + 1; j++) { // Temp total
            Cell cell= row.createCell(j);
            
            if(j == 0)
                cell.setCellValue("Total");
            else
                cell.setCellValue(tempTotal[j - 1]);
        }
        
        Cell cellx = row.createCell(tempTotal.length + 2); // Error total
        cellx.setCellValue(errorsTotal);
        
        
        
        
        // SEMANTICA 2 tabla
        row = hoja7.createRow(0);
        
        
        for (int j = 0; j <Cabecera7.length; j++) {
            Cell cell2= row.createCell(j);
            cell2.setCellStyle(style); 
            cell2.setCellValue(Cabecera7[j]);
        }
        
        for (int i = 0; i < contSemantica2.listaReglas.size(); i++) {
            row = hoja7.createRow(i + 1);
            
            Cell cell = row.createCell(0); //Regla
            cell.setCellValue(contSemantica2.listaReglas.get(i).getId());
            
            
            cell = row.createCell(1); //Tope
            cell.setCellValue(contSemantica2.listaReglas.get(i).getTopePila());
            
            
            cell = row.createCell(2); //Valor
            cell.setCellValue(contSemantica2.listaReglas.get(i).getValorReal());
            
            cell = row.createCell(3); //Linea
            cell.setCellValue(contSemantica2.listaReglas.get(i).getLinea());
            
            cell = row.createCell(4); //edo
            cell.setCellValue(contSemantica2.listaReglas.get(i).getEdo());
            
            cell = row.createCell(5); //ambito
            cell.setCellValue(contSemantica2.listaReglas.get(i).getAmbito());
        }
        
        // SEMANTICA 2 cont
        row = hoja8.createRow(0);
        
        
        for (int j = 0; j <Cabecera8.length; j++) {
            Cell cell2= row.createCell(j);
            cell2.setCellStyle(style); 
            cell2.setCellValue(Cabecera8[j]);
        }
        
        for (int i = 0; i < contSemantica2.aparece.length; i++) {
            row = hoja8.createRow(i + 1);
            
            Cell cell = row.createCell(0); //Regla
            cell.setCellValue(contSemantica2.aparece[i][0]);
            
            
            cell = row.createCell(1); //Aparece
            cell.setCellValue(contSemantica2.aparece[i][1]);
            
            
            cell = row.createCell(2); //Aceptada
            cell.setCellValue(contSemantica2.aparece[i][2]);
            
            cell = row.createCell(3); //Errores
            cell.setCellValue(contSemantica2.aparece[i][3]);
            
        }
        
        // SEMANTICA 2 amb
        
        row = hoja9.createRow(0);
        
        Cell cell2= row.createCell(0);
        cell2.setCellStyle(style); 
        cell2.setCellValue(Cabecera9[0]); 
        
        for (int j = 1; j <= contSemantica2.amb + 1; j++) {
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(j - 1);
        }
        
        cell2= row.createCell(contSemantica2.amb + 2);
        cell2.setCellStyle(style); 
        cell2.setCellValue(Cabecera9[1]);
        
        
        for (int i = 0; i < contSemantica2.aparece.length; i++) {
            row = hoja9.createRow(i + 1);
            
            Cell cell = row.createCell(0); //Regla
            cell.setCellValue(contSemantica2.aparece[i][0]);
            
            for (int j = 1; j <= contSemantica2.amb + 2; j++) {
                cell = row.createCell(j);
                cell.setCellValue(contSemantica2.contAmb[i][j - 1]);
            }
        }
        
        row = hoja9.createRow(28);
        
        cell2= row.createCell(0);
        cell2.setCellStyle(style); 
        cell2.setCellValue("Total"); 
        
        for (int j = 1; j <= contSemantica2.amb + 2; j++) {
            cell2= row.createCell(j);
            cell2.setCellValue(contSemantica2.contAmb[27][j - 1]);
        }
        
        
        // SEMANTICA 3 tabla
        row = hoja10.createRow(0);
        
        
        for (int j = 0; j <Cabecera10.length; j++) {
            cell2= row.createCell(j);
            cell2.setCellStyle(style); 
            cell2.setCellValue(Cabecera10[j]);
        }
        
        for (int i = 0; i < contSemantica3.listaReglas.size(); i++) {
            row = hoja10.createRow(i + 1);
            
            Cell cell = row.createCell(0); //Regla
            cell.setCellValue(contSemantica3.listaReglas.get(i).getId());
            
            cell = row.createCell(1); //Funcion
            cell.setCellValue(contSemantica3.listaReglas.get(i).getFuncion());
            
            cell = row.createCell(2); //Tope
            cell.setCellValue(contSemantica3.listaReglas.get(i).getTopePila());
            
            
            cell = row.createCell(3); //Valor
            cell.setCellValue(contSemantica3.listaReglas.get(i).getValorReal());
            
            cell = row.createCell(4); //Linea
            cell.setCellValue(contSemantica3.listaReglas.get(i).getLinea());
            
            cell = row.createCell(5); //edo
            cell.setCellValue(contSemantica3.listaReglas.get(i).getEdo());
            
            cell = row.createCell(6); //ambito
            cell.setCellValue(contSemantica3.listaReglas.get(i).getAmbito());
        }
        
        
        
        ///////////////////////////////////////////////////////////
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file)){						
                if (file.exists()) {// si el archivo existe se elimina
                        file.delete();
                }
                libro.write(fileOuS);
                fileOuS.flush();
                fileOuS.close();
                
                
            System.out.println("\n\n++ Se creo excel");
            
            
            JOptionPane.showMessageDialog( null, "Excel creado" );

        } catch (FileNotFoundException e) {
            System.out.println("\n\n-- No se creo excel");
            System.out.println("Exception: " + e);

            JOptionPane.showMessageDialog(null, "No se creo excel", "Error", JOptionPane.ERROR_MESSAGE);
        }catch (IOException e) {
            System.out.println("\n\n-- No se creo excel");
            System.out.println("Exception: " + e);

            JOptionPane.showMessageDialog(null, "No se creo excel", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
