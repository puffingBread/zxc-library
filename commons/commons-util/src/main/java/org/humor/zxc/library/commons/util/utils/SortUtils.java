package org.humor.zxc.library.commons.util.utils;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author xuzz
 */
public class SortUtils {

    /**
     * top k 算法，获取前k个值
     *
     * @param array 源数组
     * @param n     获取靠前的个数
     * @return 前n个值的集合
     */
    private static TreeSet<Integer> topK(int[] array, int n) {

        TreeSet<Integer> set = new TreeSet<>();
        for (int value : array) {
            if (set.size() < n) {
                set.add(value);
            } else {
                Iterator<Integer> it = set.iterator();
                int setMax = it.next();
                if (setMax > value) {
                    it.remove();
                    set.add(value);
                }
            }
        }
        return set;

    }
}
