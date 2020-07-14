package group.menu.validator;

import group.repository.Repository;

public class RepositoryIdValidator implements Validator {

    private final Repository<?> repository;

    public RepositoryIdValidator(Repository<?> repository) {
        this.repository = repository;
    }

    @Override
    public boolean validate(String input) {
        if (!input.matches("\\d+")) return false;
        return repository.ifExists(Integer.parseInt(input));
    }
}
