/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.Error;
import Model.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Estefany
 */
public class XLS {
    
    Contadores cont;

    LinkedList<Token> tokens;
    LinkedList<Model.Error> errores;
    
    public XLS(LinkedList<Token> tokens, LinkedList<Error> errores, Contadores cont, int len) {
        
        this.tokens = tokens;
        this.errores = errores;
        this.cont = cont;
        
    }

    public void crearExcel() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        LocalDateTime now = LocalDateTime.now();  
        
        String nombreArchivo="Estefany-Velarde-Ambito-"+ dtf.format(now) +".xls";
        String rutaArchivo= nombreArchivo;
        String nombreHoja1="Lista de Tokens";
        String nombreHoja2="Lista de Errores";
        String nombreHoja3="Contadores";
        String nombreHoja4="MatrizErrores";

        Workbook libro= new HSSFWorkbook();
        Sheet hoja1 = libro.createSheet(nombreHoja1);
        Sheet hoja2 = libro.createSheet(nombreHoja2);
        Sheet hoja3 = libro.createSheet(nombreHoja3);
        Sheet hoja4 = libro.createSheet(nombreHoja4);

        //cabecera de la hoja de excel
        String [] Cabecera1= new String[]{"Linea", "Token","Lexema"};
        String [] Cabecera2= new String[]{"Linea", "Error","Descripcion","Lexema", "Tipos"};
        String [] Cabecera3= new String[]{"Errores", "Identificadores","Comentarios","Palabras Reservadas",
                                            "CE-DEC","CE-BIN","CE-HEX","CE-OCT","CText","CFLOAT","CNCOMP","CCAR",
                                            "Aritmeticos","Monogamo","Logico","Bit","Identidad","Puntuacion",
                                            "Agrupacion","Asignacion","Relacional"};
        String [] Cabecera4= new String[]{"Linea","Errores"};

        
        

        //poner negrita a la cabecera
        CellStyle style = libro.createCellStyle();
        Font font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        
        Row row=hoja1.createRow(0);
        for (int j = 0; j <Cabecera1.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera1[j]);
           
        }
        
        // LISTA DE TOKENS
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
        
        row=hoja2.createRow(0);
        for (int j = 0; j <Cabecera2.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera2[j]);
           
        }
        
        // LISTA DE ERRORES
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
        
        int[] arr = cont.getContadores();
        
        row=hoja3.createRow(0);
        for (int j = 0; j <Cabecera3.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera3[j]);
           
        }
        
        // CONTADORES
            		
            row=hoja3.createRow(1);
            
            for (int j = 0; j <Cabecera3.length; j++) {		
                Cell cell= row.createCell(j);

                for (int k = 0; k < 21; k++) {
                    if(j == k)
                        cell.setCellValue(arr[j]);
                }
                				
            }
        
        
        arr = cont.getErrores();
        
        row=hoja4.createRow(0);
        for (int j = 0; j <Cabecera4.length; j++) {
            
            Cell cell= row.createCell(j);
            cell.setCellStyle(style); 
            cell.setCellValue(Cabecera4[j]);
           
        }
        
        // MATRIZ ERRORES
        for (int i = 1; i < arr.length; i++) {
            
            row=hoja4.createRow(i);
            
            for (int j = 0; j <Cabecera4.length; j++) {
                    Cell cell= row.createCell(j);
                    
                    switch(j){
                        case 0:
                            cell.setCellValue(i);
                            break;
                        case 1:
                            cell.setCellValue(arr[i]); 
                            break;
                    }
                			
            }
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
