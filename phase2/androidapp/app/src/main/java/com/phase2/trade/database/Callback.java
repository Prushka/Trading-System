package com.phase2.trade.database;

public interface Callback<V> {

    void call(V result);

}