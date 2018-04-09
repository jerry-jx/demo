package com.example.demo

/**
 *
 * Created by jerry-jx on 2017/12/13.
 */

    fun main(args: Array<String>) {

        judge()
        loops()
    }


    fun judge(): String {
        // 测试值 x = 0, -1, 1, 2, 3, 6, 10
        var x = 10
        when (x) {
        //常量
            2 -> println("等于2")
        //数值表达式
            if (x > 0) 1 else -1 -> println("大于0并等于1，或小于0并等于-1")
        //Boolean类型表达式
            in 1..5 -> println("范围匹配1-5")
            !in 6..9 -> println("不是6-9")
            is Int -> println("类型判断")
            else -> println("else")
        }
// 代替if...else if
        when {
            x > 6 && x <= 10 -> println("大于6小于等于10")
            x < 6 -> println("小于6")
            else -> println("else")
        }
        return "success"
    }

    fun loops() {
        var list: ArrayList<String> = arrayListOf("aa", "bb", "cc", "dd", "ee", "ff")
        var i = 0
        while (i < list.size)
            println(list[i++])


        for (i in list.indices) {
            println(list.get(i));
        }
        for (i in 1..list.size - 1) {
            println("递增" + list.get(i))
        }
        for (i in list.size - 1 downTo 0) {
            println("递减" + list.get(i))
        }
    }


