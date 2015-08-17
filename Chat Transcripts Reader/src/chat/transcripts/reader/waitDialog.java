/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.transcripts.reader;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;

// JDialog box asking to please while loading data

public class waitDialog extends JDialog {
    public waitDialog() {
        this((JDialog)null);
    }

    public waitDialog(JDialog owner) {
        super(owner, "Chat Transcript Reader");

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        this.setPreferredSize(new Dimension(375, 75));
 
        JLabel info = new JLabel("<html>Please wait, retrieving chat transcripts from the server....<html>");
        info.setBounds(10, 10,
                info.getPreferredSize().width, info.getPreferredSize().height);
        this.getContentPane().add(info);

        //this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.pack();
    }
}
