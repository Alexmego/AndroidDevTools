package com.hm.tools.demo;
public class Sort {

    public static void main(String[] args) {

//        sortByMaoPao();
//        sortBySelect();
        int[] array = {60, 90, 5, 3, 89, 4, 1, 2, 0};
        sort(array, 0, 8);
        print(array);
    }

    //选择排序
    private static void sortBySelect() {
        int[] array = {60, 90, 5, 3, 89, 4, 1, 2, 0};
        for (int i = 0; i < array.length - 1; i++) {
            int temp = i;
            for (int j = temp + 1; j < array.length; j++) {
                if (array[j] > array[temp]) {
                    temp = j;
                }
            }
            if (temp != i) {
                int i1 = array[temp];
                array[temp] = array[i];
                array[i] = i1;
            }
        }

        print(array);
    }

    //冒泡排序
    private static void sortByMaoPao() {
        int[] array = {60, 89, 5, 3, 90, 4, 1, 2, 0};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; ++j) {
                if (array[j] < array[j + 1]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }
        print(array);
    }


    //快速排序

    public static int partition(int[] array, int lo, int hi) {
        //固定的切分方式
        int key = array[lo];
        while (lo < hi) {
            while (array[hi] >= key && hi > lo) {//从后半部分向前扫描
                hi--;
            }
            array[lo] = array[hi];
            while (array[lo] <= key && hi > lo) {//从前半部分向后扫描
                lo++;
            }
            array[hi] = array[lo];
        }
        array[hi] = key;
        return hi;
    }

    public static void sort(int[] array, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int index = partition(array, lo, hi);
        sort(array, lo, index - 1);
        sort(array, index + 1, hi);
    }

    private static void print(int... a) {
        int length = 0;
        for (int b : a) {
            length++;
            System.out.print(b);
            if (length != a.length) {

                System.out.print(',');
            }
        }
    }
}
