package Control.Lexico;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MatrizLexico {

    XSSFSheet matriz;
    
    Hashtable<Integer, Integer> columnas;
    
    public MatrizLexico() {
        matriz = this.getMatriz();
        
        initColumnas();
    }

    private XSSFSheet getMatriz() {
        String fileName = "MatrizLexico.xlsx";
        String route = fileName;

        try (FileInputStream file = new FileInputStream(new File(route))) {
            
            XSSFWorkbook worbook = new XSSFWorkbook(file);

            return worbook.getSheetAt(0);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
    
    public int getEstado(int estado, int columna) {
        Row row = matriz.getRow(estado + 1);
        Cell cell = row.getCell(columna);
        
        return (int) cell.getNumericCellValue();
    }
    
    public int getColumna(int caracter) {
        
        // SI ES LETRA MAYUSCULA
        if(caracter >= 65 && caracter <= 90) {
            if(caracter == 69)          // E
                return 26;
            else if(caracter <= 70)      // A-DF
                return 24;
            else
                return 28;              // G-Z
        } else {
            
            // SI ES LETRA MINUSCULA
            if(caracter >= 97 && caracter <= 122) {
                if(caracter == 98)          // b
                    return 25;
                else if(caracter == 101)    // e
                    return 26;
                else if(caracter == 106)    // j
                    return 27; 
                else if(caracter == 120)    // x
                    return 29;
                else if(caracter <= 102)      // acdf
                    return 24;
                else                        // g-ik-wy-z
                    return 28;
            } 
        }
        
        return columnas.containsKey(caracter) ? columnas.get(caracter) : 46;
    }
    
    public boolean OC(int e) {
        if(e == -7 || e == -8 || e == -9 || e == -10 || e == -11 ||
          e == -12 || e == -13 || e == -18 || e == -19 || e == -21 ||
          e == -25 || e == -26 || e == -30 || e == -44 || e == -45 ||
          e == -46 || e == -48 || e == -49 || e == -50) 
            return true;
        
        return false;
        
    }
    
    private void initColumnas() {
        columnas =  new Hashtable<Integer, Integer>(); 
        int col = 1;
        
        Row row = matriz.getRow(0);
        
        Cell cell = row.getCell(col);
        
        String val = cell.getStringCellValue();
        
        while(!val.equals("OC")) {
            if(val.length() > 1) {
                switch(val) {
                    case "\\t": columnas.put(9, 44); break;
                    case "\\n": columnas.put(10, 42); break;
                    case "\\s": columnas.put(32, 43); break;
                }
                
                cell = row.getCell(++col);
                
                try { val = cell.getStringCellValue(); } 
                catch(Exception e) { val = (int) cell.getNumericCellValue()+ ""; }
                
                continue;
            }
            
            columnas.put((int) val.charAt(0), col);
            
            cell = row.getCell(++col);
            
            try { val = cell.getStringCellValue();} 
            catch(Exception e) { val = (int) cell.getNumericCellValue() + ""; }
        }
    }
    
    int[] tokenOC = {
        -7, -8, -9, -10, -11, 
        -12, -13, -18, -19, -21, 
        -25, -26, -30, -44, -45,
        -46, -48, -49, -50
    };

}


// SIGNOS DE AGRUPACIÓN
/* 
        CHAR    (   )   [   ]   {   }
        ASCII   40  41  91  93  123 125
        COL     1   2   3   4   5   6
 */
// OPERADORES
/* 
        CHAR    +   -   *   /   %   &   |   !   <   >   =   ^
        ASCII   43  45  42  47  37  38  124 33  60  62  61  94 
        COL     7   8   9   10  11  12  13  14  15  16  17  18
 */
// SIGNOS DE PUNTUACIÓN
/* 
        CHAR    :    ;   , 
        ASCII   58   59  44
        COL     45   19  20
 */
// APOSTROFES, COMILLAS Y # (TEXTO Y COMENTARIOS)
/* 
        CHAR    "   '   # 
        ASCII   34  39  35
        COL     21  22  23
 */
// LETRAS MINUSCULAS
/* 
        CHAR    a       b       c       d       e       f-i     j       k-w     x       y       z    
        ASCII   97      98      99      100     101     102-105 106     107-119 120     121     122
        COL     24      25      24      24      26      24      27      28      29      28      28
 */
// LETRAS MAYUSCULAS
/* 
        CHAR    A-D     E       F-Z    
        ASCII   65-68   69      70-90
        COL     24      26      28     
 */
// GUION BAJO
/* 
        CHAR    _    
        ASCII   95
        COL     30   
 */
// NÚMEROS
/* 
        CHAR    0   1   2   3   4   5   6   7   8   9   
        ASCII   48  49  50  51  52  53  54  55  56  57
        COL     31  32  33  34  35  36  37  38  39  40   
 */
// PUNTO
/* 
        CHAR    .    
        ASCII   46
        COL     41   
 */
// ESPACIOS
/* 
        CHAR    \n  \s  \t    
        ASCII   10  32  9
        COL     42  43  44
 */
