package phase2.trade.callback.status;

public class ResultStatusWrapper<T> {

    private T entity;

    private ResultStatus resultState;

    public ResultStatusWrapper(T entity, ResultStatus resultState) {
        this.entity = entity;
        this.resultState = resultState;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public ResultStatus getResultState() {
        return resultState;
    }

    public void setResultState(ResultStatus resultState) {
        this.resultState = resultState;
    }

}
