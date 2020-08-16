package Control;

import java.io.BufferedReader;
import javax.swing.JTextArea;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Archivos {

    JTextArea txt;
    File name;

    public Archivos(JTextArea txt, File nombre) {
        this.txt = txt;
        this.name = nombre;
    }

    public void open() throws IOException {
        FileReader file = new FileReader(name.getAbsolutePath());
        BufferedReader read = new BufferedReader(file);
        
        txt.setText("");
        
        String text = "", aux = "";
        
        aux = read.readLine();
        
        while (aux != null) {
            txt.append(aux);

            if ((aux = read.readLine()) != null) {
                txt.append("\n");
            }
        }

        read.close();
    }
}
