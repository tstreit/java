/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wifi.password;

import com.jcraft.jsch.JSchException;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author tstreit
 */
public final class PasswordGen extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(PasswordGen.class.getName());

    /* Gobal variables */
    Properties prop = new Properties();
    boolean employee;
    boolean visitors;

    /**
     * Creates new form PasswordGen
     */
    public PasswordGen() {
        initComponents();
        this.setLocationRelativeTo(null);
        GetRandom();
    }

    /* Exit application */
    public void Exit() {
        System.exit(0);
    }

    /* Get 10-digit Random key */
    public void GetRandom() {
        Random r = new Random();
        String random = r.nextSessionId();
        jLabel3.setText(random);
    }

    public void SendEmail() throws FileNotFoundException, IOException {

        File logs = new File("logs");
        if (logs.exists()) {
            // Output logging to output.log in logs folder
            FileHandler fh = new FileHandler("logs/output.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.FINE);
            logger.info("The directory logs exists.");
        } else {
            logs.mkdirs();
            // Output logging to output.log in logs folder
            FileHandler fh = new FileHandler("logs/output.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.FINE);
            logger.info("The directory logs doesn't exist.");
        }

        if (employee) {

            // Sender's email ID needs to be mentioned
            final String from = "no_reply@beltrailway.com";
            final String password = "Pr0gr@mS";
            String host = "smtp01.wncloud.com";
            prop.put("mail.smtp.host", host);

            // Get the default Session object.
            Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress("deptheads@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("misdept@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("wkizior@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("srtransportation@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("trainmaster@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("mto@beltrailway.com"));

                // Set Subject: header field
                message.setSubject("New Wifi Password");

                // Send the actual HTML message, as big as you like
                message.setContent("The new Wifi Password for Belt-Employee is " + jLabel3.getText(),
                        "text/html");
                logger.info("The new Wifi Password for Belt-Employee is " + jLabel3.getText());

                // Send message
                Transport.send(message);
                logger.info("Belt-Employee: Email has been sent.");
            } catch (MessagingException mex) {
            }
        }

        if (visitors) {

            // Sender's email ID needs to be mentioned
            // Sender's email ID needs to be mentioned
            final String from = "no_reply@beltrailway.com";
            final String password = "Pr0gr@mS";
            String host = "smtp01.wncloud.com";
            prop.put("mail.smtp.host", host);

            // Get the default Session object.
            Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress("deptheads@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("misdept@beltrailway.com"));
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("wkizior@beltrailway.com"));

                // Set Subject: header field
                message.setSubject("New Wifi Password");
                message.setContent("The new Wifi Password for Belt-Visitors is " + jLabel3.getText(),
                        "text/html");
                logger.info("The new Wifi Password for Belt-Visitors is " + jLabel3.getText());
                
                // Send message
                Transport.send(message);
                logger.info("Belt-Visitor: Email has been sent.");
            } catch (MessagingException mex) {
            }
        }
    }

    /* Update AP passwords and send email with new password */
    public void Update() {
        try {
            ChangePass.GetData(jLabel3.getText(), employee);
            SendEmail();
        } catch (IOException | JSchException | InterruptedException ex) {
            Logger.getLogger(PasswordGen.class.getName()).log(Level.SEVERE, null, ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wifi Password Generator");

        jLabel2.setText("Generate Password:");

        jButton1.setText("Regenerate Password");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Update Password");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("Belt-Employee");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Belt-Visitors");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2))
                        .addGap(44, 44, 44))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        GetRandom();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Exit();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        /**
         * Fires event that state has changed
         */
        employee = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        /**
         * Fires event that state has changed
         */
        visitors = (evt.getStateChange() == ItemEvent.SELECTED);
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Update();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    // End of variables declaration//GEN-END:variables
}
