package org.example;

public class Main {
    public static void main(String[] args) {
        int totalCount = 10; // Примерное количество элементов
        double errorProbability = 0.01; // Вероятность ошибки

        CountingBloomFilter<String> bloomFilter = new CountingBloomFilterImpl<>(totalCount, errorProbability);

        String element = "example";
        bloomFilter.add(element);
        System.out.println("Contains 'example': " + bloomFilter.contains(element));
        System.out.println("BitSet: " + bloomFilter.toString());

        bloomFilter.remove(element);
        System.out.println("Contains 'example' after removal: " + bloomFilter.contains(element));

        // Проверка битового массива
        System.out.println("BitSet: " + bloomFilter.toString());
    }
}
