package com.example.demo.controller.HTMLCatch;

/**
 * Created by jerry-jx on 2018/3/24.
 */
public class Almanac {
    private String solar;        /* 阳历 e.g.2016年 4月11日 星期一 */
    private String lunar;        /* 阴历 e.g. 猴年 三月初五*/
    private String chineseAra;    /* 天干地支纪年法 e.g.丙申年 壬辰月 癸亥日*/
    private String should;        /* 宜e.g. 求子 祈福 开光 祭祀 安床*/
    private String avoid;        /* 忌 e.g. 玉堂（黄道）危日，忌出行*/

    public String getSolar() {
        return solar;
    }

    public void setSolar(String date) {
        this.solar = date;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getChineseAra() {
        return chineseAra;
    }

    public void setChineseAra(String chineseAra) {
        this.chineseAra = chineseAra;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }

    public String getShould() {
        return should;
    }

    public void setShould(String should) {
        this.should = should;
    }

    public Almanac(String solar, String lunar, String chineseAra, String should,
        String avoid) {
        this.solar = solar;
        this.lunar = lunar;
        this.chineseAra = chineseAra;
        this.should = should;
        this.avoid = avoid;
    }
}
