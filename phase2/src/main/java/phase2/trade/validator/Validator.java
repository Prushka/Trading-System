package phase2.trade.validator;


@FunctionalInterface
public interface Validator<T> {

    boolean validate(T input);

}
