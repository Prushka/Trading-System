package group.menu.validator;

import group.repository.Repository;

/**
 * Verify if the user input is a valid unique id in a given repository
 *
 * @author Dan Lyu
 */
public class RepositoryIdValidator implements Validator {

    /**
     * The repository to exam
     */
    private final Repository<?> repository;

    /**
     * @param repository The repository to exam
     */
    public RepositoryIdValidator(Repository<?> repository) {
        this.repository = repository;
    }

    /**
     * @param input String input
     * @return if the input is a validate unique id in the given repository
     */
    @Override
    public boolean validate(String input) {
        if (!input.matches("\\d+")) return false;
        return repository.ifExists(Integer.parseInt(input));
    }
}
