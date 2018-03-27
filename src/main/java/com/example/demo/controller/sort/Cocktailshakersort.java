package com.example.demo.controller.sort;


import java.util.Random;


/**
 * 鸡尾酒排序
 */
public class Cocktailshakersort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random rd = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(30);
        }
        output(arr);
        cocktail_sort(arr);
    }

    public static void cocktail_sort(int[] arr) {
        int left = 0, right = arr.length - 1;
        int tempb, tempa;

        while (left < right) {
            for (int i = left; i < right; i++) {
                if (arr[i] > arr[i + 1]) {
                    tempb = arr[i];
                    tempa = arr[i + 1];
                    arr[i] = tempa;
                    arr[i + 1] = tempb;
                }
            }

            right--;
            output(arr);
            for (int i = right; i > left; i--) {
                if (arr[i - 1] > arr[i]) {
                    tempb = arr[i - 1];
                    tempa = arr[i];
                    arr[i - 1] = tempa;
                    arr[i] = tempb;
                }
            }

            left++;
            output(arr);
        }
    }

    public static void output(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "   ");
        }
        System.out.println();
    }

}
