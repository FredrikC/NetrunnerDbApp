package se.fredrikcarlbom.netrunnerdbapp;

public interface IConsumer<T> {
    void onResponse(T item);
}