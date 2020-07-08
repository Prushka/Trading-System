package group.menu.data;

import java.util.ArrayList;
import java.util.List;

public class Response {

    private final boolean success;

    private final List<TranslatablePair> translatablePairs = new ArrayList<>();

    Response(Builder builder) {
        this.success = builder.success;
        this.translatablePairs.addAll(builder.translatablePairs);
    }

    public boolean getSuccess() {
        return success;
    }

    public List<TranslatablePair> getTranslatablePairs() {
        return translatablePairs;
    }

    @Override
    public String toString() {
        return "Response: " + "success: " + success + "," + " translatable: " + translatablePairs.toString();
    }

    public static class Builder {

        private boolean success;

        private final List<TranslatablePair> translatablePairs;

        public Builder() {
            this.translatablePairs = new ArrayList<>();
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder translatable(String translatable) {
            this.translatable(translatable, new Object[0]);
            return this;
        }

        public Builder translatable(String translatable, Object... paras) {
            this.translatablePairs.add(new TranslatablePair(translatable, paras));
            return this;
        }

        public Response build() {
            return new Response(this);
        }

    }


}
