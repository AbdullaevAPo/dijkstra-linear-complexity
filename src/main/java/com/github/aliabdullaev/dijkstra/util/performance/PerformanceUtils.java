package com.github.aliabdullaev.dijkstra.util.performance;

public class PerformanceUtils {

    public static int getMin(int a, int b) {
        int c = a - b;
        int k = (c >> 31) & 0x1;
//                 return b + k * c;
        return b + c * k;
    }

    public static int getMax(int a, int b) {
        int c = a - b;
        int k = (c >> 31) & 0x1;
//                 return b + k * c;
        return a - c * k;
    }

    /** 0 - если равны, 1 - иначе*/
    public static int getNotEquals(int a, int b) {
        return getMin(1, a ^ b);
    }

    /** 1111... - if equals, 0000... - otherwise */
    public static int getEquals(int a, int b) {
        return 1 - getNotEquals(a, b);
    }

    /**
     *  @return 0, т.е. a >= b иначе 1, т.е. a < b
     */
    public static int getSign(int a, int b) {
        int c = a - b;
        int k = (c >> 31) & 0x1;
        return k;
    }

    // a < b => 0, else 1
    public static int getRevertSign(int a, int b) {
        return 1 - getSign(a, b);
    }

    // if 0 => 0, if 1 => Integer.MAX_VALUE
    public static int explodeSign(int a) {
        return (~a + 1) & Integer.MAX_VALUE;
    }

    public static int round(double x){
        return (int)(x-0.5)+1;
    }

    public static int ceil(double x) { return (int)(x - 1e-5) + 1; }
}
