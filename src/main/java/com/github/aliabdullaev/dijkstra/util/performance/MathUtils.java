package com.github.aliabdullaev.dijkstra.util.performance;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class MathUtils {
    private static double[] powOneAndHalfTable;
    private static double[] powOneAndHalfReverseTable;
    private static double[] powTwoTable;
    private static double[] powThreeTable;
    private static double[] powTwoReverseTable;

    private static int powerTableSize = 1000;

    private static double[] buildPowTable(double maxVal, double powValue) {
        int topBorder = (int) (maxVal * powerTableSize);
        return IntStream
                .range(0, topBorder + 1)
                .mapToDouble(i -> Math.pow((i + 0.0) / powerTableSize, powValue))
                .toArray();
    }
    static {
        powTwoTable = buildPowTable(1.0, 2);
        powTwoReverseTable = buildPowTable(1.0, 0.5);
        powThreeTable = buildPowTable(1.0, 3);
    }

    public static double powOneAndHalf(double val) {
        return powOneAndHalfTable[(int)(val * powerTableSize)];
    }

    public static double powOneAndHalfReverse(double val) {
        return powOneAndHalfReverseTable[(int)(val * powerTableSize)];
    }

    public static double pow2(double val) {
        return powTwoTable[(int)(val * powerTableSize)];
    }

    public static double pow3(double val) {
        return powThreeTable[(int)(val * powerTableSize)];
    }

    public static double sqrt(double val) {
        return powTwoReverseTable[(int)(val * powerTableSize)];
    }

    public static double mean(double[] arr) {
        return DoubleStream.of(arr).average().orElse(0);
    }

    public static double nonlinearNormalization(double val, double maxVal, double minVal) {
        double average = (maxVal + minVal) / 2;
        return sigmoid((val - average) / average);
    }


    public static double nonlinearDoubleNormalization(double val, double average, double step) {
        return doubleSigmoid(val,  average,  step * 2);
    }

    // N(0, 1)
    public static double nonlinearNormalization(double val) {
        return (Math.tanh(val) + 1) * 0.5;
    }

    public static double sigmoid(double val) {
        return (Math.tanh(val/2) + 1) / 2;
    }

    public static double doubleSigmoid(double x, double d, double s) {
        return Math.signum(x-d) * (1-exp(-pow((x-d)/s, 2)));
    }

    public static double standardDeviation(double[] arr) {
        double meanVal = mean(arr);
        double res = 0;
        for (int i = 0; i < arr.length; i++) {
            res += Math.pow(arr[i] - meanVal, 2);
        }
        res /= (arr.length - 1);
        return Math.sqrt(res);
    }

    public static double[] buildArr(double... arr) {
        return arr;
    }

    /** Средне квадратичное значение */
    public static double rootMeanSquare(double... arr) {
        double res = 0;
        for (int i = 0; i < arr.length; i++) {
            res += Math.pow(arr[i], 2);
        }
        res /= arr.length;
        return Math.sqrt(res);
    }

    public static double rootMeanSquare(double[] arr, double[] weights) {
        double res = 0;
        for (int i = 0; i < arr.length; i++) {
            res += weights[i] * Math.pow(arr[i], 2);
        }
        res /= Arrays.stream(weights).sum();
        return Math.sqrt(res);
    }

    public static double[] standardScaling(double[] arr) {
        double mean = mean(arr);
        double stddev = standardDeviation(arr);
        return DoubleStream.of(arr).map(i -> (i - mean) / stddev).toArray();
    }

    /**
     * Коэффициент [0, 1] может менять величину только на некоторые максимальный процент, т.е. если коэф 0.5, а процент 20%,
     * то итоговый коэф будет равен 0.9
     */
    public static double percentInfluence(double k, double influencePercent) {
        // каноническо должно произости val / k, но это может повлиять на val больше чем определенный процент
        return ((1-influencePercent) + k * influencePercent);
    }

    public static double percentInfluenceAbsolute(double k, double influencePercent) {
        return fromRelativeToAbsoluteKoef(percentInfluence(fromAbsoluteToRelativeKoef(k), influencePercent));
    }

    /**
     * от абсолютного коэффициента [1, +inf] к относительному [1, 0]
     * @param k
     * @return
     */
    public static double fromAbsoluteToRelativeKoef(double k) {
        return 1 / k;
    }

    public static double fromRelativeToAbsoluteKoef(double k) {
        return 1 / k;
    }
}
