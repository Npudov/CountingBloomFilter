package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountingBloomFilterImplTest {
    private CountingBloomFilter<String> bloomFilter;

    @BeforeEach
    public void setUp() {
        int totalCount = 10;
        double errorProbability = 0.01;
        bloomFilter = new CountingBloomFilterImpl<>(totalCount, errorProbability);
    }

    @Test
    public void testAddAndContains() {
        String element = "example";
        bloomFilter.add(element);
        assertTrue(bloomFilter.contains(element), "Bloom filter should contain the added element");
        System.out.println("BitSet after add: " + bloomFilter.toString());
    }

    @Test
    public void testRemove() {
        String element = "example";
        bloomFilter.add(element);
        assertTrue(bloomFilter.contains(element), "Bloom filter should contain the added element");

        bloomFilter.remove(element);
        assertFalse(bloomFilter.contains(element), "Bloom filter should not contain the removed element");
        System.out.println("BitSet after remove: " + bloomFilter.toString());
    }

    @Test
    public void testContainsNonExistentElement() {
        String element = "example";
        assertFalse(bloomFilter.contains(element), "Bloom filter should not contain a non-existent element");
    }

    @Test
    public void testMultipleAdds() {
        String element1 = "example1";
        String element2 = "example2";
        bloomFilter.add(element1);
        bloomFilter.add(element2);
        assertTrue(bloomFilter.contains(element1), "Bloom filter should contain the first added element");
        assertTrue(bloomFilter.contains(element2), "Bloom filter should contain the second added element");
        System.out.println("BitSet after multiple adds: " + bloomFilter.toString());
    }

    @Test
    public void testMultipleRemovals() {
        String element1 = "example1";
        String element2 = "example2";
        bloomFilter.add(element1);
        bloomFilter.add(element2);

        bloomFilter.remove(element1);
        assertFalse(bloomFilter.contains(element1), "Bloom filter should not contain the removed first element");
        assertTrue(bloomFilter.contains(element2), "Bloom filter should still contain the second element");

        bloomFilter.remove(element2);
        assertFalse(bloomFilter.contains(element2), "Bloom filter should not contain the removed second element");
        System.out.println("BitSet after multiple removals: " + bloomFilter.toString());
    }
}
