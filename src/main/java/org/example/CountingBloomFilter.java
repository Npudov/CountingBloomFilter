package org.example;

public interface CountingBloomFilter<T> {
    void add(T element);
    void remove(T element);
    boolean contains(T element);
}

