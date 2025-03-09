package com.example.diary_final

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
fun main(){
    val date = Date() // 获取当前时间
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val formattedDate = sdf.format(date)
    println(formattedDate)



    //以上是得到本机时间，以下是解析日期，即将字符串解析回Date类型

    val dateString = "2025-03-09 14:45:30"
    val sdfp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val datep = sdf.parse(dateString)
    println(datep)  // 输出: Sun Mar 09 14:45:30 GMT 2025

}