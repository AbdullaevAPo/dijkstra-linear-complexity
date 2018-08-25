package com.github.aliabdullayev.dijkstra;

import com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

@Slf4j
public class BinarySearchPerformanceTest {
    public static int binarySearchQuick(int[] a, int key) {
        return binarySearchQuick2(a, 0, a.length, key);
    }

    // Like public version, but without range checks.
    private static int binarySearchQuick(int[] a, int fromIndex, int toIndex, int key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            int k1 = 1 - PerformanceUtils.getSign(key, midVal);
            int k2 = 1 - PerformanceUtils.getSign(midVal, key);
            low = fastMulti(k1, (mid + k1)) + fastMulti((1 ^ k1), low);
            high = fastMulti(k2, (mid - k2)) + fastMulti((1 ^ k2), high);

            if (midVal == key) {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found.
    }

    // Like public version, but without range checks.
    private static int binarySearchQuick2(int[] a, int fromIndex, int toIndex, int key) {
        int low = fromIndex;
        int high = toIndex;
        int log2 = PerformanceUtils.ceil(Math.log(a.length) / Math.log(2));

        for (int i = 0; i < log2; i++) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (key < midVal) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        if (a[high] == key) return high;
        else return -(low + 1);  // key not found.
    }


    /** binaryElem - 0 or 1, only for positive numbers */
    private static int fastMulti(int binaryElem, int a) {
        return PerformanceUtils.explodeSign(binaryElem) & a;
    }

    @Test
    public void test() {
        int[] arr = generateHugeArray();
        long t1 = System.nanoTime();
        int totalRes2 = 0;
        for (int i=0; i<1_000_000_000; i+=10) {
            int res = Arrays.binarySearch(arr, i);
            totalRes2 += res;
        }
        long t2 = System.nanoTime();
        log.info("{}", (t2 - t1)/1_000_000);

        t1 = System.nanoTime();
        int totalRes = 0;
        for (int i=0; i<1_000_000_000; i+=10) {
            int res = binarySearchQuick(arr, i);
            totalRes += res;
        }
        t2 = System.nanoTime();
        log.info("{}", (t2 - t1)/1_000_000);


        log.info("{} {}", totalRes, totalRes2);
        Assert.assertEquals(totalRes2, totalRes);
    }

    private int[] generateHugeArray() {
        int[] a = new int[10000];
        Random random = new Random();
        for (int i=0; i<a.length; i++) {
            a[i] = random.nextInt();
        }
        Arrays.sort(a);
        return a;
    }

    @Test
    public void qw() {
        System.out.printf("%d\n", PerformanceUtils.getRevertSign(10, 1));
        System.out.printf("%d\n", PerformanceUtils.getRevertSign(1, 10));
        System.out.printf("%d\n", PerformanceUtils.getRevertSign(10, 10));
        System.out.printf("%d %d\n", (~1 + 1) & Integer.MAX_VALUE , Integer.MAX_VALUE);
        System.out.printf("%d\n", (~0 + 1) & Integer.MAX_VALUE );

    }
}
