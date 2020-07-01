package menu.data;

public class Response {

    private final boolean success;

    private final Object[] paras;

    private final String translatable;

    public boolean getSuccess() {
        return success;
    }

    public Object[] getParas() {
        return paras;
    }

    public String getTranslatable() {
        return translatable;
    }

    public Response(boolean success, String translatable, Object... paras) {
        this.success = success;
        this.translatable = translatable;
        this.paras = paras;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Response: ");
        builder.append("success: ").append(success).append(",").append(" translatable: ").append(translatable);
        for (Object para : paras) {
            builder.append(", ").append(para);
        }
        return builder.toString();
    }
}
