package phase2.trade.view;

public interface TextFieldPredicate<T> {
    boolean test(T entity, String textField);
}
