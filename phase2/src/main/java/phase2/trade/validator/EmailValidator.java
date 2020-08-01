package phase2.trade.validator;

import java.util.regex.Pattern;

/**
 * The validator used to validate an email String
 *
 * @author GeeksforGeeks - <a href="https://www.geeksforgeeks.org/check-email-address-valid-not-java/">Check Email Address Valid</a>
 */
public class EmailValidator implements Validator {

    /**
     * @param input String input
     * @return <code>true</code> if the input is in valid email format
     */
    public boolean validate(String input) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(input).matches();
    }

}
