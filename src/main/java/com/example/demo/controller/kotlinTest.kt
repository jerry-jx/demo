package com.example.demo.controller

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *
 * Created by jerry-jx on 2018/4/12.
 */
object kotlinTest {
    @JvmStatic
    fun main(args: Array<String>) {
       /* println(LocalDate.now().plusDays(1).atStartOfDay())

        println(conversionTime(null,LocalDate.now().withDayOfMonth(1).toString(),
                LocalDate.now().plusDays(1).toString()))*/

        val a = "把我的标点符号去掉吧，全科。"
        print(a.trimPunct())
    }


    /**
     * 删除所有的标点符号
     *
     * @param str 处理的字符串
     */
    fun String.trimPunct(): String {
        return if (this.isEmpty()) {
            ""
        } else this.replace("[\\pP\\p{Punct}]".toRegex(), "")
    }


    fun conversionTime(summaryQueryType: SummaryQueryType?, startTime: String?, endTime: String?): Pair<LocalDateTime, LocalDateTime> {

        check(summaryQueryType != null || (startTime != null && endTime != null))

        val beginTime: LocalDateTime
        val stopTime: LocalDateTime

        if (summaryQueryType != null) {
            when (summaryQueryType) {
                SummaryQueryType.Today -> {
                    beginTime = LocalDate.now().atStartOfDay()
                    stopTime = beginTime.plusDays(1)
                }
                SummaryQueryType.Yesterday -> {
                    beginTime = LocalDate.now().minusDays(1).atStartOfDay()
                    stopTime = beginTime.plusDays(1)
                }
                SummaryQueryType.ThisWeek -> {
                    beginTime = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()
                    stopTime = LocalDate.now().plusDays(1).atStartOfDay()
                }
                SummaryQueryType.LastWeek -> {
                    beginTime = LocalDate.now().with(DayOfWeek.MONDAY).minusWeeks(1).atStartOfDay()
                    stopTime = beginTime.plusWeeks(1)
                }
                SummaryQueryType.ThisMonth -> {
                    beginTime = LocalDate.now().withDayOfMonth(1).atStartOfDay()
                    stopTime = LocalDate.now().plusDays(1).atStartOfDay()
                }
                SummaryQueryType.LastMonth -> {
                    beginTime = LocalDate.now().withDayOfMonth(1).minusMonths(1).atStartOfDay()
                    stopTime = beginTime.plusMonths(1)
                }
            }
        } else {
            beginTime = LocalDate.parse(startTime).atStartOfDay()
            stopTime = LocalDate.parse(endTime).plusDays(1).atStartOfDay()//TODO 查询时间默认添加一天的查询
        }

        return Pair(beginTime, stopTime)
    }

    enum class SummaryQueryType{

        // 今天
        Today,

        // 昨天
        Yesterday,

        // 这周
        ThisWeek,

        // 上周
        LastWeek,

        // 这个月
        ThisMonth,

        // 上个月
        LastMonth

        // 半年
        //ThisHalfYear,

        // 今年
        //ThisYear

    }

}