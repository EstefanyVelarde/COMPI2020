package Control;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MatrizSintaxis {

    XSSFSheet matriz;
    
    public MatrizSintaxis() {
        matriz = this.getMatriz();
    }

    private XSSFSheet getMatriz() {
        String fileName = "MatrizSintaxis.xlsx";
        String route = fileName;

        try (FileInputStream file = new FileInputStream(new File(route))) {
            
            XSSFWorkbook worbook = new XSSFWorkbook(file);

            return worbook.getSheetAt(0);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
    
    public int getValor(int elemento, int token) {
        
        int columna = getColumna(token);
        
        Row row = matriz.getRow(elemento + 1);
        Cell cell = row.getCell(columna);
        
        return (int) cell.getNumericCellValue();
    }
    
    public int getColumna(int token) {
        int i = token * -1;
        
        if(i < columnas.length)
            return columnas[i][1];
        
        return -1; 
    }
    
    public boolean terminales(int PS, int LT) {
        
        int i = LT * -1;
        
        return columnas[i][1] == PS * -1;
    }

    // ARREGLO DE TOKENS Y COLUMNAS
    public int[][] columnas = {
    //  Token  COL
        {0  , -1}, // 0
        {-1  , 44}, // (
        {-2  , 45}, // )
        {-3  , 48}, // [
        {-4  , 49}, // ]
        {-5  , 46}, // {
        {-6  , 47}, // }
        {-7  , 34}, // +
        {-8  , 35}, // -
        {-9  , 14}, // *
        {-10 , 15}, // **
        {-11 , 16}, // /
        {-12 , 17}, // //
        {-13 , 18}, // %
        {-14 , 54}, // ++
        {-15 , 55}, // --
        {-16 , 22}, // &&
        {-17 , 20}, // ||
        {-18 , 33}, // !
        {-19 , 27}, // <
        {-20 , 28}, // <=
        {-21 , 32}, // >
        {-22 , 31}, // >=
        {-23 , 30}, // ==
        {-24 , 29}, // !=
        {-25 , 21}, // &
        {-26 , 19}, // |
        {-27 , 26}, // ^
        {-28 , 24}, // <<
        {-29 , 25}, // >>
        {-30 , 36}, // =
        {-31 , 37}, // +=
        {-32 , 38}, // -=
        {-33 , 41}, // *=
        {-34 , 42}, // **=
        {-35 , 39}, // /=
        {-36 , 40}, // //=
        {-37 , 43}, // %=
        {-38 , 51}, // ;
        {-39 , 53}, // ,
        {-40 , 4}, // Cadena
        {-41 , 5}, // Caracter
        {-42 , -1}, // Comentario '''
        {-43 , -1}, // Comentario #
        {-44 , 2}, // id
        {-45 , 7}, // Decimal
        {-46 , 3}, // Flotante
        {-47 , 6}, // Complejo
        {-48 , 8}, // Binario
        {-49 , 9}, // Hexa
        {-50 , 10}, // Octal
        {-51 , 23}, // ##
        {-52 , 52}, // .
        {-53 , 50}, // :
        
    // PALABRAS RESERVADAS
    //  Token  COL
        {-54 , 11}, // true
        {-55 , 12}, // false
        {-56 , 13}, // none
        {-57 , 56}, // IS
        {-58 , 57}, // ISNOT
        {-59 , 58}, // IN
        {-60 , 59}, // INNOT
        {-61 , 60}, // sort
        {-62 , 61}, // reverse
        {-63 , 62}, // insert
        {-64 , 63}, // count
        {-65 , 64}, // index
        {-66 , 65}, // append
        {-67 , 66}, // extend
        {-68 , 67}, // pop
        {-69 , 68}, // remove
        {-70 , 69}, // random
        {-71 , 70}, // findall
        {-72 , 71}, // replace
        {-73 , 72}, // sample
        {-74 , 73}, // len
        {-75 , 74}, // choice
        {-76 , 75}, // randrange
        {-77 , 76}, // range
        {-78 , 77}, // mean
        {-79 , 78}, // median
        {-80 , 79}, // variance
        {-81 , 80}, // sum
        {-82 , 81}, // print
        {-83 , 82}, // println
        {-84 , 83}, // if
        {-85 , 84}, // elif
        {-86 , 85}, // else
        {-87 , 86}, // end
        {-88 , 87}, // wend
        {-89 , 88}, // for
        {-90 , 89}, // to
        {-91 , 90}, // while
        {-92 , 91}, // break
        {-93 , 92}, // continue
        {-94 , 93}, // return
        {-95 , 94}, // input
        {-96 , 95}, // def
        {-97 , 96}, // $
    };
    
}
