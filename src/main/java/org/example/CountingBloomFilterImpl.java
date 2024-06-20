package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class CountingBloomFilterImpl<T> implements CountingBloomFilter<T> {
    private final int[] counts;

    private final BitSet bitSet;
    private final int bitSetSize;
    private final Set<Function<T, Integer>> hashFunctions;

    public CountingBloomFilterImpl(int totalCount, double errorProbability) {
        this.bitSetSize = (int) Utils.optimalBitCount(totalCount, errorProbability);
        long hashFunctionCount = Utils.hashFunctionCount(totalCount, bitSetSize);
        this.hashFunctions = generateHashFunctions(hashFunctionCount);
        this.counts = new int[bitSetSize];
        this.bitSet = new BitSet(bitSetSize);
    }

    private Set<Function<T, Integer>> generateHashFunctions(long count) {
        Set<Function<T, Integer>> functions = new HashSet<>();
        for (int i = 0; i < count; i++) {
            final int seed = i;
            functions.add(element -> {
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hashBytes = digest.digest((element.toString() + seed).getBytes());
                    int hash = 0;
                    for (int j = 0; j < 4; j++) {
                        hash <<= 8;
                        hash |= ((int) hashBytes[j]) & 0xFF;
                    }
                    return hash;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return functions;
    }

    @Override
    public void add(T element) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(element) % bitSetSize);
            bitSet.set(hash);
            counts[hash]++;
        }
    }

    @Override
    public void remove(T element) {
        if (!contains(element)) {
            return;
        }
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(element) % bitSetSize);
            if (counts[hash] > 0) {
                counts[hash]--;
            }
            if (counts[hash] == 0) {
                bitSet.clear(hash);
            }
        }
    }

    @Override
    public boolean contains(T element) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(element) % bitSetSize);
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < bitSetSize; i++) {
            sb.append(bitSet.get(i) ? 1 : 0);
            if (i < bitSetSize - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
