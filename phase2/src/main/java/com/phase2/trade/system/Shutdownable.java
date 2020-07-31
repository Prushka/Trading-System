package com.phase2.trade.system;

/**
 * The interface to be used in classes that contain shutdown operations.
 *
 * @author Dan Lyu
 */
public interface Shutdownable {
    /**
     * Shuts down the current object
     */
    void shutdown();
}
