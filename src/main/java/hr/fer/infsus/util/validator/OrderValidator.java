package hr.fer.infsus.util.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class OrderValidator {

    public void notFutureDate(final Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        Date now = new Date();
        if (date.after(now)) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
    }

    public void creditCardNumber(final String creditCardNumber) {
        if (creditCardNumber == null) {
            throw new IllegalArgumentException("Credit card number cannot be null");
        }
        if (!creditCardNumber.matches("\\*{4}\\d{4}")) {
            throw new IllegalArgumentException("Invalid credit card number format. Should be '****xxxx', x represents single digit.");
        }
    }


}
