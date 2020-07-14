package group.menu.validator;

public class DateValidator implements Validator {

    @Override
    public boolean validate(String input) {
        String[] data = input.split("-");
        if (data.length == 5){
            for(String s: data) {
                try {
                    Integer.parseInt(s);
                } catch (NumberFormatException e) { // is this allowed
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
