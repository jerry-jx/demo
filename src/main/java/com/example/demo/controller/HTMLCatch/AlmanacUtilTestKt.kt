package com.example.demo.controller.HTMLCatch

/**
 *
 * Created by jerry-jx on 2018/4/5.
 */
object AlmanacUtilTestKt {
    @JvmStatic
    fun main(args: Array<String>) {
        val almanac = AlmanacUtilKt.almanac()
        println("公历时间：" + almanac.solar)
        println("农历时间：" + almanac.lunar)
        println("天干地支：" + almanac.chineseAra)
        println("宜：" + almanac.should)
        println("忌：" + almanac.avoid)
    }
}