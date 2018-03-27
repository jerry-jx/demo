package com.example.demo.controller.sort;


import java.util.Random;

public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random rd = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(30);
        }
        output(arr);
        bubble_sort(arr);

    }

    private static void bubble_sort(int[] arr) {
        int temp;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            System.out.println(">>>>>>>>>>>");
            output(arr);
        }
        System.out.println("<<<<<<<<<<<<<<");
        output(arr);
    }

    private static void output(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "   ");
        }
        System.out.println();

    }

}
