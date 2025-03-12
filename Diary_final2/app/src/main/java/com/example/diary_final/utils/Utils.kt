package com.example.diary_final.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    // 日期相关方法
    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

    private fun getDateFormat(pattern: String): SimpleDateFormat =
        SimpleDateFormat(pattern, Locale.getDefault())

    fun formatDate(date: Date, pattern: String = DEFAULT_DATE_FORMAT): String =
        getDateFormat(pattern).format(date)

    fun getCurrentDate(pattern: String = DEFAULT_DATE_FORMAT): String =
        formatDate(Date(), pattern)

    // 位置相关方法
    class LocationHelper(private val context: Context) {
        private val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        private var locationListener: LocationListener? = null

        /*
         * 获取位置：
         * 1. 检查位置权限；
         * 2. 尝试获取最后已知的位置；
         * 3. 注册位置更新监听器，获取最新位置后调用回调并取消更新。
         */
        @SuppressLint("MissingPermission")
        fun getLocation(onLocationReceived: (Location) -> Unit) {
            // 检查位置权限
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("LocationHelper", "位置权限未授予")
                return
            }

            // 尝试获取最后已知位置（优先GPS，其次网络）
            val lastKnownLocation: Location? = try {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } catch (e: SecurityException) {
                Log.e("LocationHelper", "获取最后位置异常: ${e.message}", e)
                null
            }
            lastKnownLocation?.let { onLocationReceived(it) }

            // 注册位置更新监听器
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    onLocationReceived(location)
                    removeLocationUpdates()
                }
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            // 请求GPS位置更新
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000L, // 最小时间间隔（毫秒）
                    10f,   // 最小距离（米）
                    locationListener!!
                )
            } catch (e: SecurityException) {
                Log.e("LocationHelper", "请求GPS位置更新异常: ${e.message}", e)
            }
            // 请求网络位置更新
            try {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000L,
                    10f,
                    locationListener!!
                )
            } catch (e: SecurityException) {
                Log.e("LocationHelper", "请求网络位置更新异常: ${e.message}", e)
            }
        }

        /**
         * 移除位置更新
         */
        fun removeLocationUpdates() {
            locationListener?.let {
                locationManager.removeUpdates(it)
                locationListener = null
            }
        }
    }
}
