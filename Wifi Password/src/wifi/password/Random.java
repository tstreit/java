/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wifi.password;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author tstreit
 */
public class Random {
    
    /* Generate random 10-digit key */
    private final SecureRandom random = new SecureRandom();

  public String nextSessionId() {
    return new BigInteger(50, random).toString(32);
  }
    
}