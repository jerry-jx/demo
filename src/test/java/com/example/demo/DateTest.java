package com.example.demo;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class DateTest {

    public static void main(String[] args) {

        //1
        LocalDate today = LocalDate.now();
        System.out.println("Today : " + today);
        LocalDate birthDate = LocalDate.of(1993, Month.OCTOBER, 19);
        System.out.println("BirthDate : " + birthDate);

        Period p = Period.between(birthDate, today);
        System.out.printf("年龄 : %d 年 %d 月 %d 日", p.getYears(), p.getMonths(), p.getDays());

        //2
        Instant inst1 = Instant.now();
        System.out.println("Inst1 : " + inst1);
        Instant inst2 = inst1.plus(Duration.ofSeconds(10));
        System.out.println("Inst2 : " + inst2);

        System.out.println("Difference in milliseconds : " + Duration.between(inst1, inst2).toMillis());

        System.out.println("Difference in seconds : " + Duration.between(inst1, inst2).getSeconds());

        //3+
        LocalDate startDate = LocalDate.of(1993, Month.OCTOBER, 19);
        System.out.println("开始时间  : " + startDate);

        LocalDate endDate = LocalDate.of(2017, Month.JUNE, 16);
        System.out.println("结束时间 : " + endDate);

        long daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
        System.out.println("两天之间的差在天数   : " + daysDiff);

    }

    private int getTimeIntervalByLastTime(String lastDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // LocalDate formatted = LocalDate.parse(date, DateTimeFormatter.ISO_ORDINAL_DATE);
        LocalDate lastTime = LocalDate.parse(lastDate, formatter);
        LocalDate today = LocalDate.now();
        Period periodTime = Period.between(lastTime, today);
        return periodTime.getDays();
    }

    private int getTimeIntervalByLastTime(Date lastDate) {
        Instant instant = lastDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate lastLocalDate = localDateTime.toLocalDate();
        LocalDate today = LocalDate.now();
        Period periodTime = Period.between(lastLocalDate, today);
        return periodTime.getDays();
    }

}
