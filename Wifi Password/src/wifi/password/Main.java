/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wifi.password;

import com.jcraft.jsch.JSchException;
import java.io.IOException;

public class Main {

    /**
     * @param args the command line arguments
     * @throws com.jcraft.jsch.JSchException
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws JSchException, InterruptedException, IOException {
        PasswordGen pg = new PasswordGen();
        pg.setVisible(true);
    }
}