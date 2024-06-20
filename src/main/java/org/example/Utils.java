package org.example;

public class Utils {
    //Оптимальное кол-во бит
    public static int optimalBitCount(int totalCount, double errorProbability) {
        return (int) Math.ceil(
                -totalCount * Math.log(errorProbability) / Math.pow(Math.log(2), 2));
    }

    //Расчет кол-ва хэш-функций
    public static int hashFunctionCount(int totalCount, int bitArraySize) {
        return (int) Math.ceil(Math.log(2) * ((double) bitArraySize / totalCount));
    }

}
