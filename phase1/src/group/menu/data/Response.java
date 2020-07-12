package group.menu.data;

import java.util.ArrayList;
import java.util.List;

/**
 * The Response object used to pass information from the system to the nodes.
 *
 * @author Dan Lyu
 */
public class Response {

    /**
     * if the Request is successfully made
     */
    private final boolean success;

    private final String persistentKey;

    /**
     * A list of pairs of translatable Strings and parameters
     */
    private final List<TranslatablePair> translatablePairs = new ArrayList<>();

    private final String flexibleMasterIdentifier;

    /**
     * @param builder the {@link Response.Builder} used to build this Response
     */
    Response(Builder builder) {
        this.success = builder.success;
        this.translatablePairs.addAll(builder.translatablePairs);
        this.flexibleMasterIdentifier = builder.master;
        this.persistentKey = builder.persistentKey;
    }

    /**
     * @return {@link #success}
     */
    public boolean success() {
        return success;
    }

    /**
     * @return {@link #translatablePairs}
     */
    public List<TranslatablePair> getTranslatablePairs() {
        return translatablePairs;
    }

    public String getFlexibleMasterIdentifier() {
        return flexibleMasterIdentifier;
    }

    public String getPersistentKey() {
        return persistentKey;
    }

    /**
     * @return the String representation of this Response object
     */
    @Override
    public String toString() {
        return "Response: " + "success: " + success + "," + " translatable: " + translatablePairs.toString();
    }

    /**
     * The Builder used to build a Response object
     */
    public static class Builder {

        private boolean success;

        private final List<TranslatablePair> translatablePairs;

        private String master;

        private String persistentKey;

        public Builder(boolean success) {
            this.success = success;
            this.translatablePairs = new ArrayList<>();
        }

        public Builder responseType(String persistentKey) {
            this.persistentKey = persistentKey;
            return this;
        }

        public Builder masterIdentifier(String identifier) {
            master = identifier;
            return this;
        }

        /**
         * @param success if the Response stands for a successful Response
         * @return the builder itself
         */
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        /**
         * @param translatable A translatable String to be added into translatable Pairs
         * @return the builder itself
         */
        public Builder translatable(String translatable) {
            this.translatable(translatable, new Object[0]);
            return this;
        }

        /**
         * @param translatable A translatable String to be added into translatable Pairs
         * @param paras        paras to be formatted into the translatable String
         * @return the builder itself
         */
        public Builder translatable(String translatable, Object... paras) {
            this.translatablePairs.add(new TranslatablePair(translatable, paras));
            return this;
        }

        /**
         * Add all translatable pairs in another response object to the current one
         *
         * @param response the response object
         * @return the builder itself
         */
        public Builder response(Response response) {
            this.translatablePairs.addAll(response.getTranslatablePairs());
            return this;
        }

        /**
         * @return the Response object
         */
        public Response build() {
            return new Response(this);
        }

    }


}
