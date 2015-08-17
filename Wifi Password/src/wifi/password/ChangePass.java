/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wifi.password;

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

/**
 *
 * @author tstreit
 */
public class ChangePass {
    
    private static final Logger logger = Logger.getLogger(ChangePass.class.getName());

    public static void GetData(String password, boolean wifi) throws JSchException, InterruptedException, IOException {

        /* Variables */
        String[] ips;
        File logs = new File("logs");
        
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

        /* Depending on if wifi is true or fales create ips string array */
        if (wifi == true) {
            ips = new String[6];
            ips[0] = "192.168.61.201";
            ips[1] = "192.168.61.200";
            ips[2] = "192.168.61.202";
            ips[3] = "192.168.45.14";
            ips[4] = "192.168.47.240";
            ips[5] = "192.168.47.253";
        } else {
            ips = new String[4];
            ips[0] = "192.168.46.201";
            ips[1] = "192.168.46.200";
            ips[2] = "192.168.46.202";
            ips[3] = "192.168.45.15";
        }

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

            if (wifi == true) {
                out.write(("dot11 ssid Belt-Employee\n").getBytes());
            } else {
                out.write(("dot11 ssid Belt-Visitors\n").getBytes());
            }
            out.write(("no wpa-psk\n").getBytes());
            out.write(("wpa-psk ascii " + password + "\n").getBytes());
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
}
