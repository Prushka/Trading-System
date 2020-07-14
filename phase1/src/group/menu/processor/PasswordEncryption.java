package group.menu.processor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 * This classes uses md5 to encrypt password.<p>
 * This method seems to be discouraged since collision may happen even it's rare.<p>
 * This class needs to be replaced with a better approach.
 *
 * @author Adrian Stamin - <a href="https://stackoverflow.com/questions/6592010/encrypt-and-decrypt-a-password-in-java">crypt with md5</a>
 */
public class PasswordEncryption implements InputPreProcessor {

    /**
     * Returns an encrypted String using md5 from input
     *
     * @param input the raw input String
     * @return the encrypted password using md5
     */
    @Override
    public String process(String input) {
        try {
            byte[] digested = MessageDigest.getInstance("MD5").digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digested) {
                sb.append(Integer.toHexString(0xff & b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong with password encryption.", e);
            return null;
        }
    }
}
