package phase2.trade.callback.status;

/**
 * The Result status wrapper. It wraps an entity and a ResultStatus.
 *
 * @param <T> the entity type
 * @author Dan Lyu
 */
public class ResultStatusWrapper<T> {

    private T entity;

    private ResultStatus resultState;

    /**
     * Constructs a new Result status wrapper.
     *
     * @param entity      the entity
     * @param resultState the result state
     */
    public ResultStatusWrapper(T entity, ResultStatus resultState) {
        this.entity = entity;
        this.resultState = resultState;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Sets entity.
     *
     * @param entity the entity
     */
    public void setEntity(T entity) {
        this.entity = entity;
    }

    /**
     * Gets result state.
     *
     * @return the result state
     */
    public ResultStatus getResultState() {
        return resultState;
    }

    /**
     * Sets result state.
     *
     * @param resultState the result state
     */
    public void setResultState(ResultStatus resultState) {
        this.resultState = resultState;
    }

}
