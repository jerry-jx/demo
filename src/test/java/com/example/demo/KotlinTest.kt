package com.example.demo

/**
 * Created by jerry-jx on 2017/12/13.
 */
class KotlinTest (
        private val kotlin : Kotlin
){

    fun main(args: Array<String>) {

       var a= kotlin?.judge()?: "123"
        println(a)
        kotlin?.loops()


        start()
    }

    fun start (){
        val list = listOf("a","b","c")
        list.forEach {
            println("开始")
            /* if(it .equals("b") ){
                 println("==forEach")
               return@forEach
             }*/
            println(it)
            var size=0
            while (true){
                println("while $size")
                if(size >= 2){
                    println("==forEach")
                    return@forEach
                }
                size ++
            }

        }
    }

}