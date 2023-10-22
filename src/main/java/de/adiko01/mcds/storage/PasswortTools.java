package de.adiko01.mcds.storage;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * A little Toolbox for Passwords
 * @author adiko01
 * @version 1.0
 */
public class PasswortTools {

    /**
     * Checks if the Password is weak
     * @param password The Password
     * @return true if the password is weak
     * @since 1.0
     */
    public static boolean checkWeakPassword(String password) {
        //TODO implement better

        String[] weak = {
                "",
                "123456789",
                "password",
                "passwort",
                "admin",
                "root"
        };

        for (String wpw : weak) {
            if (password.equalsIgnoreCase(wpw)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Bilde den SHA256
     * @param Passwort Das Passwort
     * @return Den SHA256
     * @since 1.0
     */
    static String getSHA256 (String Passwort) {
        return DigestUtils.sha256Hex(Passwort);
    }

}
