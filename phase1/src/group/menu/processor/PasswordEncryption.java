package group.menu.processor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Adrian Stamin - <a href="https://stackoverflow.com/questions/6592010/encrypt-and-decrypt-a-password-in-java">crypt with md5</a>
 */
public class PasswordEncryption implements InputPreProcessor {
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
            e.printStackTrace();
            return null;
        }
    }
}