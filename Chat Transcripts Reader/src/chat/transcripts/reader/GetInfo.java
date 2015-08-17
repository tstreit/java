package chat.transcripts.reader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author Ted Streit
 */
public class GetInfo {

    String remoteHostUserName = "root";
    String RemoteHostName = "stcomm.beltrailway.com";
    String remoteHostpassword = "W0lf!3434";
    int SFTPPORT = 22;
    String SFTPWORKINGDIR = "/root";
    String user_home = System.getProperty("user.home");
    String cmd = "cd /local/notesdata/CLData/ && zip -r /root/chat_logs.zip *";
    JDialog waitDialog = new waitDialog();

    // Method that starts a thread to load data while displaying JDialog box asking to please wait
    public void doSomeWork() {
        this.waitDialog.setVisible(true);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GetData();
                } catch (JSchException | IOException ex) {
                    Logger.getLogger(GetInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        th.start();
        try {
            th.join();
        } catch (InterruptedException ex) {
            System.out.println("Oh god...");
        }

        this.waitDialog.setVisible(false);
    }

    public void GetData() throws JSchException, IOException {

        // SSH into stcomm.beltrailway.com and make zip file
        JSch jsch = new JSch();
        Session session = jsch.getSession(remoteHostUserName, RemoteHostName, SFTPPORT);

        session.setPassword(remoteHostpassword);
        Properties config = new Properties();

        config.put(
                "StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));

        channel.setCommand(cmd);

        channel.connect();

        /* String msg = null;
        while ((msg = in.readLine()) != null) {
            System.out.println(msg);
        } */

        channel.disconnect();
        session.disconnect();

        Download();
    }

    private void Download() {

        // SFTP and download chats_logs.zip
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(remoteHostUserName, RemoteHostName, SFTPPORT);
            session.setPassword(remoteHostpassword);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(channelSftp.get("chat_logs.zip"));
            File newFile = new File("chat_logs.zip");
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
        } catch (JSchException | SftpException | IOException ex) {
        }
        Unzip();
    }

    private void Unzip() {
        
        // Unzip chats_logs.zip to user home directory and delete zip file
        String source = "chat_logs.zip";
        String destination = user_home + "/chat_logs";

        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
        }

        File file = new File("chat_logs.zip");
        file.delete();
    }
}
