/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto.password.visitors;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author tstreit
 */
public class AutoPasswordVisitors {

    private static final Logger logger = Logger.getLogger(AutoPasswordVisitors.class.getName());

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws com.jcraft.jsch.JSchException
     */
    public static void main(String[] args) throws IOException, JSchException {
        ChangePass();
    }

    private static void ChangePass() throws IOException, JSchException {
        /* Variables */
        String[] ips;
        File logs = new File("logs");
        Random r = new Random();
        String pass = r.nextSessionId();
        SendMail(pass);

        /* Create log directory & log file */
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

        ips = new String[4];
        ips[0] = "192.168.46.201";
        ips[1] = "192.168.46.200";
        ips[2] = "192.168.46.202";
        ips[3] = "192.168.45.15";


        /* Loop through string array and connect through SSH to APs and run update password configuration */
        for (String s : ips) {
            String remoteHostUserName = "tstreit";
            String RemoteHostName = s;
            String remoteHostpassword = "W0lf!3434";

            JSch jsch = new JSch();
            Session session = jsch.getSession(remoteHostUserName, RemoteHostName, 22);

            session.setPassword(remoteHostpassword);
            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            Channel channel = session.openChannel("shell");
            channel.connect();

            InputStream in = channel.getInputStream();
            OutputStream out = channel.getOutputStream();

            out.write(("config t\n").getBytes());
            out.write(("dot11 ssid Belt-Visitors\n").getBytes());
            out.write(("no wpa-psk\n").getBytes());
            out.write(("wpa-psk ascii " + pass + "\n").getBytes());
            out.write(("exit\n").getBytes());
            out.write(("exit\n").getBytes());
            out.write(("copy run start\n").getBytes());
            out.write(("\n").getBytes());
            out.write(("exit\n").getBytes());
            out.flush();

            /* Write output to log file */
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    logger.info(new String(tmp, 0, i));
                    System.out.println(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    break;
                }
            }
            channel.disconnect();
            session.disconnect();
        }
    }

    public static void SendMail(String pass) throws IOException {

        File logs = new File("logs");
        Properties prop = new Properties();

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

        final String from = "no_reply@beltrailway.com";
        final String password = "Pr0gr@mS";
        String host = "smtp01.wncloud.com";
        prop.put("mail.smtp.host", host);

        // Get the default Session object.
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
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
            message.setContent("The new Wifi Password for Belt-Visitors is " + pass,
                    "text/html");
            logger.info("The new Wifi Password for Belt-Visitors is " + pass);

            // Send message
            Transport.send(message);
            logger.info("Belt-Visitor: Email has been sent.");
        } catch (MessagingException mex) {
        }
    }
}
