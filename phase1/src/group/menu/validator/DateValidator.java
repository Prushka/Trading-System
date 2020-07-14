package group.menu.validator;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class DateValidator implements Validator {

    @Override
    public boolean validate(String input) {
        String[] data = input.split("-");
        if (data.length == 5){
            try {
                int year = Integer.parseInt(data[0]);
                int month = Integer.parseInt(data[1]);
                int day = Integer.parseInt(data[2]);
                int hour = Integer.parseInt(data[3]);
                int minute = Integer.parseInt(data[4]);
                return (LocalDateTime.of(year, month, day, hour, minute)).isAfter(LocalDateTime.now());
            } catch (NumberFormatException | DateTimeException e) { // is this allowed
                return false;
            }
        }
        return false;
    }
}
