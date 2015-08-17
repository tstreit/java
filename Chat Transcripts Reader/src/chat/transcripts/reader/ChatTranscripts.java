package chat.transcripts.reader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ted Streit
 */
public class ChatTranscripts extends javax.swing.JFrame {

    // Global variables
    String dir = System.getProperty("user.home");
    String os = System.getProperty("os.name");
    String choosedir = dir + "/chat_logs";
    String deletedir = dir + "/chat_logs";
    String treevar;

    /**
     * Creates new form ChatTranscripts
     */
    public ChatTranscripts() {
        initComponents();
        // Set JFrame to center of the screen
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Exit();
                } catch (IOException ex) {
                    Logger.getLogger(ChatTranscripts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // About method
    private void About() {
        About a = new About();
        a.show();
    }

    // Exit method
    private void Exit() throws IOException {
        // Delete temporary directory
        File deldir = new File(deletedir);
        FileUtils.deleteDirectory(deldir);
        System.exit(0);
    }

    // Exporting select transcript to a PDF
    private void Export2PDF() throws DocumentException, FileNotFoundException {
        String filename = jTree1.getSelectionPath().toString();
        String result = null;
        if (filename.contains("IM_")) {
            result = filename.substring(filename.indexOf("IM_"), filename.indexOf(".dat"));
        } else if (filename.contains("ANNOUNCE")) {
            result = filename.substring(filename.indexOf("ANNOUNCE"), filename.indexOf(".dat"));
        } else {
            result = filename.substring(filename.indexOf("0#"), filename.indexOf(".dat"));
        }

        // Convert HTML from jTextPane1 to raw text
        HTML2Text parser = new HTML2Text();
        try {
            parser.parse(new StringReader(jTextPane1.getText()));
        } catch (IOException e) {
            System.out.println(e);
        }

        // Create PDF from parsed text
        Document document = new Document();
        if (os.equals("Mac OS X")) {
            PdfWriter.getInstance(document, new FileOutputStream(dir + "/" + result + ".pdf"));
        } else {
            PdfWriter.getInstance(document, new FileOutputStream(dir + "\\" + result + ".pdf"));
        }

        document.open();
        document.add(new Paragraph(parser.getText()));
        document.close();

        // Notify where PDF was created
        Component frame = null;

        if (os.equals("Mac OS X")) {
            JOptionPane.showMessageDialog(frame, "File has been exported to: " + dir + "/", "", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "File has been exported to: " + dir + "\\", "", JOptionPane.PLAIN_MESSAGE);
        }
    }

    // Read file method
    private void Read() {
        try {
            FileReader reader = new FileReader(treevar);
            try (BufferedReader br = new BufferedReader(reader)) {
                String line;
                String complete = "";
                if (br.ready()) {
                    while ((line = br.readLine()) != null) {
                        line = line + "<br/>";
                        complete = complete.concat(line);
                    }
                }
                jTextPane1.setText(complete);
            }
        } catch (Exception e2) {
            System.out.println(e2);
        }
    }

    // Print method
    private void Print() {
        Component frame = null;
        try {
            jTextPane1.setContentType("text/html");

            boolean done = jTextPane1.print();
            if (done) {
                JOptionPane.showMessageDialog(frame, "Printing is done", "", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Error while printing", "", JOptionPane.ERROR_MESSAGE);
            }
        } catch (PrinterException | HeadlessException pex) {
            JOptionPane.showMessageDialog(frame, "Error while printing", "", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat Transcript Reader");

        jTree1.setModel(new FileSystemModel(new File(choosedir)));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jTextPane1.setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        jTextPane1.setEditorKit(kit);
        jTextPane1.setEditable(false);
        jScrollPane3.setViewportView(jTextPane1);

        jMenu1.setText("File");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Export To PDF");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Print");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        jMenuItem3.setText("About");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            // Call Exit method
            Exit();
        } catch (IOException ex) {
            Logger.getLogger(ChatTranscripts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // Get Path
        treevar = jTree1.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "/");
        Read();
    }//GEN-LAST:event_jTree1ValueChanged

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // Print transcript
        Print();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        About();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // Export to PDF
        try {
            Export2PDF();
        } catch (DocumentException | FileNotFoundException ex) {
            Logger.getLogger(ChatTranscripts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
