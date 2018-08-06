package com.github.aliabdullaev.dijkstra.util;

import com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO LIST научить его работать с диапозоном от min до max
 */
public @Data class BitArray {
    byte[] bytes = null;
    int size = 0;
    int setCnt = 0;

    private void relocate() {
        bytes = new byte[size /8 + 1];
        setCnt = 0;
    }

    private void copyBytes(byte[] source) {
        bytes = Arrays.copyOf(source, source.length);
    }

    public BitArray(int cnt) {
        this.size = cnt;
        relocate();
    }

    public BitArray(BitArray copy) {
        this.bytes = null;
        this.size = copy.size;
        relocate();
        this.setCnt = copy.setCnt;
        copyBytes(copy.bytes);
    }

    public BitArray(Collection<Integer> numbers) {
        this(Collections.max(numbers));
        numbers.forEach(this::set);
    }

    public BitArray() {
        relocate();
    }

    public void union(BitArray arr) {
        int newSize = Math.max(arr.getSize(), this.size);
        increaseSize(newSize);
        for (int i=0; i < arr.size; i++) {
            if (arr.get(i) != 0) {
                set(i);
            }
        }
    }

    public byte get(int pos) {
        if (pos >= size)
            return -1;
        return (byte) ((bytes[pos / 8] >> (pos % 8)) & 1);
    }

    public void set(int pos) {
        setCnt += 1 - PerformanceUtils.getNotEquals(get(pos), 0);
        bytes[pos / 8] |= 1 << (pos % 8);
    }

    public void reset(int pos) {
        setCnt -= PerformanceUtils.getNotEquals(get(pos), 0);
        bytes[pos / 8] &= ~(1 << pos % 8);
    }

    public void increaseSize(int newSize) {
        if (size >= newSize) {
            return;
        }
        this.size = newSize;
        bytes = Arrays.copyOf(bytes, size/8+1);
    }

    public int getLastNotNull(int firstPos, int lastPos) {
        int res = -1;
        for (int i=lastPos; i>=firstPos; i--) {
            if (bytes[i /8] == 0) {
                i -= 8;
                continue;
            } else if (((bytes[i / 8] >> (i % 8)) & 1) != 0) {
                return i;
            }
        }
        return res;
    }




    public int getFirstNotNull(int firstPos, int lastPos) {
        int res = -1;
        for (int i=firstPos; i<lastPos; i++) {
            if (bytes[i /8] == 0) {
                i += 8;
                continue;
            } else if (((bytes[i / 8] >> (i % 8)) & 1) != 0) {
                return i;
            }
        }
        return res;
    }

    public int getFirstNotNull(int firstPos) {
        return getFirstNotNull(firstPos, size);
    }

    public int[] getAllSetPos() {
        int[] res = new int[setCnt];
        int k = 0;
        for (int i=0; i<size && k < res.length; i++) {
            byte curByte = bytes[i /8];
            int bit = ((curByte >> (i % 8)) & 1);
            if (bit != 0) {
                res[k++] = i;
            } else if (curByte == 0) {
                i += 7;
            }
        }
        return res;
    }

    public int getUnsetCnt() { return size - setCnt; }
}

