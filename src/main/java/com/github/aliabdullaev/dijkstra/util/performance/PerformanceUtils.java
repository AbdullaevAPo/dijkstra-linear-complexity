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

    public static int getMin(int a, int b, int c) {
        int p1 = a - b;
        int k1 = (p1 >> 31) & 0x1;
        int minab = b + k1 * p1;

        int p2 = minab - c;
        int k2 = (p2 >> 31) & 0x1;
        return c + k2 * p2;
    }

    public static int round(double x){
        return (int)(x-0.5)+1;
    }
}
