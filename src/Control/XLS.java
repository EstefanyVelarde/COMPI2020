package Control;

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

    LinkedList<Token> tokens;
    LinkedList<Model.Error> errores;
    
    public XLS(LinkedList<Token> tokens, LinkedList<Error> errores, ContLexico contLexico, ContAmbito contAmbito) {
        
        this.tokens = tokens;
        this.errores = errores;
        this.contLexico = contLexico;
        this.contAmbito = contAmbito;
        
    }

    public void crearExcel() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();  
        
        String nombreArchivo="Estefany-Velarde-Ambito-"+ dtf.format(now) +".xls";
        String rutaArchivo= nombreArchivo;
        String nombreHoja1="Lista de Tokens";
        String nombreHoja2="Lista de Errores";
        String nombreHoja3="Contadores";
        String nombreHoja4="Ámbito";
        String nombreHoja5="Tabla de simbolos";

        Workbook libro= new HSSFWorkbook();
        Sheet hoja1 = libro.createSheet(nombreHoja1);
        Sheet hoja2 = libro.createSheet(nombreHoja2);
        Sheet hoja3 = libro.createSheet(nombreHoja3);
        Sheet hoja4 = libro.createSheet(nombreHoja4);
        Sheet hoja5 = libro.createSheet(nombreHoja5);

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
        
        
        
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file)){						
                if (file.exists()) {// si el archivo existe se elimina
                        file.delete();
                }
                libro.write(fileOuS);
                fileOuS.flush();
                fileOuS.close();

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }catch (IOException e) {
                e.printStackTrace();
        }
        
    }

}
