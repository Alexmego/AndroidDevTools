# Tools
# Android 7.0 实现状态栏完全透明
---
1,Activity Theme 设置为下面的style。
~~~
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="android:windowTranslucentStatus">true</item>
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>
 ~~~
 2,在Activity 的OnCreate（）加入如下code.
 ~~~
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                @SuppressLint("PrivateApi")
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);
            } catch (Exception ignored) {
            }
        }
 ~~~
 
 # 排序  [图解排序算法(三)之堆排序](https://www.cnblogs.com/chengxiao/p/6129630.html)
 
 
 
~~~
package sortdemo;

import java.util.Arrays;

/**
 * Created by chengxiao on 2016/12/17.
 * 堆排序demo
 */
public class HeapSort {
    public static void main(String []args){
        int []arr = {9,8,7,6,5,4,3,2,1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void sort(int []arr){
        //1.构建大顶堆
        for(int i=arr.length/2-1;i>=0;i--){
            //从第一个非叶子结点从下至上，从右至左调整结构
            adjustHeap(arr,i,arr.length);
        }
        //2.调整堆结构+交换堆顶元素与末尾元素
        for(int j=arr.length-1;j>0;j--){
            swap(arr,0,j);//将堆顶元素与末尾元素进行交换
            adjustHeap(arr,0,j);//重新对堆进行调整
        }

    }

    /**
     * 调整大顶堆（仅是调整过程，建立在大顶堆已构建的基础上）
     * @param arr
     * @param i
     * @param length
     */
    public static void adjustHeap(int []arr,int i,int length){
        int temp = arr[i];//先取出当前元素i
        for(int k=i*2+1;k<length;k=k*2+1){//从i结点的左子结点开始，也就是2i+1处开始
            if(k+1<length && arr[k]<arr[k+1]){//如果左子结点小于右子结点，k指向右子结点
                k++;
            }
            if(arr[k] >temp){//如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
                arr[i] = arr[k];
                i = k;
            }else{
                break;
            }
        }
        arr[i] = temp;//将temp值放到最终的位置
    }

    /**
     * 交换元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int []arr,int a ,int b){
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}

~~~
# 冒泡排序和选择排序
~~~
public class myClass {

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

~~~
