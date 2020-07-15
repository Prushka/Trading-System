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
     * <code>true</code> if the Request is successfully made
     */
    private boolean success;

    /**
     * The persistent Request key.
     * If <code>success == true</code> and this key is not null,
     * the corresponding Request will be persistent.
     */
    private String persistentKey;

    /**
     * A list of pairs of translatable Strings and parameters
     */
    private final List<TranslatablePair> translatablePairs = new ArrayList<>();

    /**
     * The master node identifier.
     * If this is not null, the corresponding {@link group.menu.node.MasterOptionNode} will be prompted.
     */
    private String nextMasterNodeIdentifier;

    /**
     * @param builder the {@link Response.Builder} used to build this Response
     */
    Response(Builder builder) {
        this.success = builder.success;
        this.translatablePairs.addAll(builder.translatablePairs);
        this.nextMasterNodeIdentifier = builder.master;
        this.persistentKey = builder.persistentKey;
    }

    public Response(String translatable) {
        this(new Response.Builder(true).translatable(translatable));
    }

    /**
     * @return <code>true</code> if the Response represents a successful Response
     */
    public boolean success() {
        return success;
    }

    /**
     * @return the list of translatable pairs
     */
    public List<TranslatablePair> getTranslatablePairs() {
        return translatablePairs;
    }

    /**
     * @return the master node identifier
     */
    public String getNextMasterNodeIdentifier() {
        return nextMasterNodeIdentifier;
    }

    /**
     * @param identifier the next master node's identifier to be set when the Response is successful
     */
    public void setNextMasterNodeIdentifier(String identifier) {
        nextMasterNodeIdentifier = identifier;
    }

    /**
     * @return the persistent key for the corresponding Request to allow it to be persistent
     */
    public String getPersistentKey() {
        return persistentKey;
    }

    /**
     * @param key the persistent key for the corresponding Request to allow it to be persistent
     */
    public void setPersistentKey(String key) {
        this.persistentKey = key;
    }

    /**
     * @param success the success state
     */
    public void setSuccess(boolean success) {
        this.success = success;
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

        /**
         * @param success if the Request succeeds
         */
        public Builder(boolean success) {
            this.success = success;
            this.translatablePairs = new ArrayList<>();
        }

        /**
         * @param persistentKey the persistent key for the Request object
         * @return the builder itself
         */
        public Builder responseType(String persistentKey) {
            this.persistentKey = persistentKey;
            return this;
        }

        /**
         * @param identifier the identifier for a flexible {@link group.menu.node.MasterOptionNode}
         * @return the builder itself
         */
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
         * Builds the Response object
         *
         * @return the Response object
         */
        public Response build() {
            return new Response(this);
        }

    }

}
