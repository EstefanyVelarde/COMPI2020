package View;

import Control.Ambito.Ambito;
import Control.Lexico.Lexico;
import Control.Sintaxis.Sintaxis;
import Control.XLS;
import Control.Archivos;
import Control.Semantica.Semantica1;
import Control.Semantica.Semantica2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.Element;

public class Interface extends javax.swing.JFrame {
    
    private JTextArea codigo;
    private JTextArea lines;
    public Archivos file;
    
    public Lexico lexico;
    public Sintaxis sintaxis;
    public Ambito ambito;
    public Semantica1 semantica1;
    public Semantica2 semantica2;

    public Interface() {
        initComponents();
        createAndShowGUI();
    }
    
    Color bg = new Color(7,38, 59);
    Color fg = new Color(120,150,174);
        
    public void createAndShowGUI(){
        // FULL SCREEN
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // JTEXTAREA
        codigo = new JTextArea();
        
        codigo.setBackground(new Color(21,61,87));
        codigo.setForeground(new Color(120,150,174));
        codigo.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        
        lines = new JTextArea(" 1");

        lines.setBackground(new Color(12,48,72));
        lines.setForeground(new Color(84,116,139));
        lines.setEditable(false);
        lines.setFont(new Font("Lucida Console", Font.PLAIN, 18));

        codigo.getDocument().addDocumentListener(new DocumentListener(){
            public String getText(){
                    int caretPosition = codigo.getDocument().getLength();
                    Element root = codigo.getDocument().getDefaultRootElement();
                    String text = " 1" + System.getProperty("line.separator");
                    for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
                            if(i < 10)
                                text += " " + i + System.getProperty("line.separator");
                            else
                                if(i < 100)
                                    text += i + System.getProperty("line.separator");
                                else
                                    text += i + System.getProperty("line.separator");
                    }
                    return text;
            }
            @Override
            public void changedUpdate(DocumentEvent de) {
                    lines.setText(getText());
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                    lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                    lines.setText(getText());
            }

        });

        codigoScroll.getViewport().add(codigo);
        codigoScroll.setRowHeaderView(lines);
        codigoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // JTABLE
        errores.setOpaque(true);
        errores.setFillsViewportHeight(true);
        errores.setBackground(bg);
        errores.setForeground(fg);
        
        
        errores.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        
        errores.setRowHeight(30);
        
        tokens.setOpaque(true);
        tokens.setFillsViewportHeight(true);
        tokens.setBackground(bg);
        tokens.setForeground(fg);
        
        tokens.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        
        tokens.setRowHeight(30);
        
        contadores.setOpaque(true);
        contadores.setFillsViewportHeight(true);
        contadores.setBackground(bg);
        contadores.setForeground(fg);
        contadores.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        contadores.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        
        
        contadores.setRowHeight(35);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgScroll = new javax.swing.JScrollPane();
        background = new javax.swing.JPanel();
        areaTituloPanel = new javax.swing.JPanel();
        areaTitulo = new javax.swing.JLabel();
        codigoScroll = new javax.swing.JScrollPane();
        botonesPanel = new javax.swing.JPanel();
        jbOpen = new javax.swing.JButton();
        jbCompile = new javax.swing.JButton();
        jbXLS = new javax.swing.JButton();
        jbClear = new javax.swing.JButton();
        sidePanel = new javax.swing.JPanel();
        tokensPanel = new javax.swing.JPanel();
        tokensTitulo = new javax.swing.JLabel();
        tokensScroll = new javax.swing.JScrollPane();
        tokens = new javax.swing.JTable();
        contadoresScroll = new javax.swing.JScrollPane();
        contadores = new javax.swing.JTable();
        contadoresPanel = new javax.swing.JPanel();
        contadoresTitulo = new javax.swing.JLabel();
        downPanel = new javax.swing.JPanel();
        erroresPanel = new javax.swing.JPanel();
        erroresTitulo = new javax.swing.JLabel();
        erroresScroll = new javax.swing.JScrollPane();
        errores = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(7, 38, 59));

        bgScroll.setAlignmentX(0.0F);
        bgScroll.setAlignmentY(0.0F);
        bgScroll.setAutoscrolls(true);
        bgScroll.setPreferredSize(new java.awt.Dimension(1920, 1080));

        background.setBackground(new java.awt.Color(7, 38, 59));
        background.setMaximumSize(null);

        areaTituloPanel.setBackground(new java.awt.Color(33, 133, 145));

        areaTitulo.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        areaTitulo.setForeground(new java.awt.Color(153, 241, 253));
        areaTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        areaTitulo.setText("ÁREA DE CODIGO");

        javax.swing.GroupLayout areaTituloPanelLayout = new javax.swing.GroupLayout(areaTituloPanel);
        areaTituloPanel.setLayout(areaTituloPanelLayout);
        areaTituloPanelLayout.setHorizontalGroup(
            areaTituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(areaTituloPanelLayout.createSequentialGroup()
                .addComponent(areaTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        areaTituloPanelLayout.setVerticalGroup(
            areaTituloPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(areaTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        botonesPanel.setBackground(new java.awt.Color(12, 48, 72));

        jbOpen.setBackground(new java.awt.Color(7, 38, 59));
        jbOpen.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jbOpen.setForeground(new java.awt.Color(33, 133, 145));
        jbOpen.setText("ABRIR");
        jbOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOpenActionPerformed(evt);
            }
        });

        jbCompile.setBackground(new java.awt.Color(7, 38, 59));
        jbCompile.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jbCompile.setForeground(new java.awt.Color(33, 133, 145));
        jbCompile.setText("COMPILAR");
        jbCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCompileActionPerformed(evt);
            }
        });

        jbXLS.setBackground(new java.awt.Color(7, 38, 59));
        jbXLS.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jbXLS.setForeground(new java.awt.Color(33, 133, 145));
        jbXLS.setText("XLS");
        jbXLS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbXLSActionPerformed(evt);
            }
        });

        jbClear.setBackground(new java.awt.Color(7, 38, 59));
        jbClear.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jbClear.setForeground(new java.awt.Color(33, 133, 145));
        jbClear.setText("LIMPIAR");
        jbClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout botonesPanelLayout = new javax.swing.GroupLayout(botonesPanel);
        botonesPanel.setLayout(botonesPanelLayout);
        botonesPanelLayout.setHorizontalGroup(
            botonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, botonesPanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jbOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jbClear, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jbCompile, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jbXLS, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        botonesPanelLayout.setVerticalGroup(
            botonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(botonesPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(botonesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbXLS)
                    .addComponent(jbCompile)
                    .addComponent(jbOpen)
                    .addComponent(jbClear))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        sidePanel.setBackground(new java.awt.Color(12, 48, 72));

        tokensPanel.setBackground(new java.awt.Color(229, 217, 73));

        tokensTitulo.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        tokensTitulo.setForeground(new java.awt.Color(255, 255, 255));
        tokensTitulo.setText("LISTA DE TOKENS");

        javax.swing.GroupLayout tokensPanelLayout = new javax.swing.GroupLayout(tokensPanel);
        tokensPanel.setLayout(tokensPanelLayout);
        tokensPanelLayout.setHorizontalGroup(
            tokensPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tokensPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(tokensTitulo)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        tokensPanelLayout.setVerticalGroup(
            tokensPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tokensPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tokensTitulo)
                .addContainerGap())
        );

        tokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Token", "Lexema"
            }
        ));
        tokens.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tokensScroll.setViewportView(tokens);
        if (tokens.getColumnModel().getColumnCount() > 0) {
            tokens.getColumnModel().getColumn(0).setPreferredWidth(60);
            tokens.getColumnModel().getColumn(1).setPreferredWidth(70);
            tokens.getColumnModel().getColumn(2).setPreferredWidth(235);
        }

        contadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Errores", ""},
                {"Identificadores", null},
                {"Comentarios", null},
                {"Palabras Reservadas", null},
                {"CE-DEC", null},
                {"CE-BIN", null},
                {"CE-HEX", null},
                {"CE-OCT", null},
                {"CText", null},
                {"CFLOAT", null},
                {"CNCOMP", null},
                {"CCAR", null},
                {"Aritmeticos", null},
                {"Monogamo", null},
                {"Logico", null},
                {"Bit", null},
                {"Identidad", null},
                {"Puntuacion", null},
                {"Agrupacion ", null},
                {"Asignacion ", null},
                {"Relacional ", null}
            },
            new String [] {
                "", ""
            }
        ));
        contadores.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        contadoresScroll.setViewportView(contadores);
        if (contadores.getColumnModel().getColumnCount() > 0) {
            contadores.getColumnModel().getColumn(0).setPreferredWidth(175);
        }

        contadoresPanel.setBackground(new java.awt.Color(117, 83, 215));

        contadoresTitulo.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        contadoresTitulo.setForeground(new java.awt.Color(255, 255, 255));
        contadoresTitulo.setText("LISTA DE CONTADORES");

        javax.swing.GroupLayout contadoresPanelLayout = new javax.swing.GroupLayout(contadoresPanel);
        contadoresPanel.setLayout(contadoresPanelLayout);
        contadoresPanelLayout.setHorizontalGroup(
            contadoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contadoresPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(contadoresTitulo)
                .addContainerGap(102, Short.MAX_VALUE))
        );
        contadoresPanelLayout.setVerticalGroup(
            contadoresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contadoresPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(contadoresTitulo)
                .addContainerGap())
        );

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tokensScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(tokensPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contadoresScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(contadoresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contadoresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tokensPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tokensScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE)
                    .addComponent(contadoresScroll))
                .addGap(25, 25, 25))
        );

        downPanel.setBackground(new java.awt.Color(12, 48, 72));

        erroresPanel.setBackground(new java.awt.Color(241, 78, 81));

        erroresTitulo.setFont(new java.awt.Font("Calibri", 1, 16)); // NOI18N
        erroresTitulo.setForeground(new java.awt.Color(255, 255, 255));
        erroresTitulo.setText("LISTA DE ERRORES");

        javax.swing.GroupLayout erroresPanelLayout = new javax.swing.GroupLayout(erroresPanel);
        erroresPanel.setLayout(erroresPanelLayout);
        erroresPanelLayout.setHorizontalGroup(
            erroresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(erroresPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(erroresTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        erroresPanelLayout.setVerticalGroup(
            erroresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, erroresPanelLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(erroresTitulo)
                .addContainerGap())
        );

        errores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Linea", "Numero", "Descripcion", "Lexema", "Tipo"
            }
        ));
        errores.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        errores.getTableHeader().setBackground(bg);
        erroresScroll.setViewportView(errores);

        javax.swing.GroupLayout downPanelLayout = new javax.swing.GroupLayout(downPanel);
        downPanel.setLayout(downPanelLayout);
        downPanelLayout.setHorizontalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(erroresScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1026, Short.MAX_VALUE)
                    .addComponent(erroresPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        downPanelLayout.setVerticalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(erroresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(erroresScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(codigoScroll)
                    .addComponent(areaTituloPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(areaTituloPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(codigoScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(downPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(botonesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        bgScroll.setViewportView(background);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();

        background.setSize(screensize);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bgScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bgScroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenActionPerformed
        
        JFileChooser explorer = new JFileChooser();
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        
        explorer.setFileFilter(filter);
        explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        explorer.showOpenDialog(null);
        
        File file = explorer.getSelectedFile();
			
        if(file != null) {
            Archivos newFile = new Archivos(codigo, file);
            
            try {
                newFile.open();
            } catch (IOException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jbOpenActionPerformed

    private void jbCompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCompileActionPerformed
        lexico = new Lexico();
        
        lexico.leer(codigo.getText());
        
        if(!lexico.getTokens().isEmpty()) {
            ambito = new Ambito();
            
            semantica1 = new Semantica1(ambito);
            
            semantica2 = new Semantica2(ambito, semantica1);
            
            sintaxis = new Sintaxis(lexico.getTokensCopy(), lexico.getErrores(),
                                    ambito, semantica1, semantica2);
        
            sintaxis.analizarTokens();
        }
        
        lexico.setTablas(tokens, errores, contadores);
    }//GEN-LAST:event_jbCompileActionPerformed

    private void jbXLSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbXLSActionPerformed
        XLS ce = new XLS(lexico.getTokensCopy(), lexico.getErrores(), 
                lexico.getContadores(), ambito.getContador(), semantica1.getContador());
        ce.crearExcel();
        
        System.out.println("\n++ Se creo excel");
    }//GEN-LAST:event_jbXLSActionPerformed

    private void jbClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbClearActionPerformed
        codigo.setText("");
        
    }//GEN-LAST:event_jbClearActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaTitulo;
    private javax.swing.JPanel areaTituloPanel;
    private javax.swing.JPanel background;
    private javax.swing.JScrollPane bgScroll;
    private javax.swing.JPanel botonesPanel;
    private javax.swing.JScrollPane codigoScroll;
    private javax.swing.JTable contadores;
    private javax.swing.JPanel contadoresPanel;
    private javax.swing.JScrollPane contadoresScroll;
    private javax.swing.JLabel contadoresTitulo;
    private javax.swing.JPanel downPanel;
    private javax.swing.JTable errores;
    private javax.swing.JPanel erroresPanel;
    private javax.swing.JScrollPane erroresScroll;
    private javax.swing.JLabel erroresTitulo;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbCompile;
    private javax.swing.JButton jbOpen;
    private javax.swing.JButton jbXLS;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JTable tokens;
    private javax.swing.JPanel tokensPanel;
    private javax.swing.JScrollPane tokensScroll;
    private javax.swing.JLabel tokensTitulo;
    // End of variables declaration//GEN-END:variables
}
