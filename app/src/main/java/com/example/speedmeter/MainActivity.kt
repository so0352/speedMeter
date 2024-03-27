package com.example.speedmeter

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), LocationListener {

    private var locationManager: LocationManager? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // LocationManagerのインスタンスを取得
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        // アプリが必要な権限を持っているかチェック
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 権限が付与されていない場合、それをリクエスト
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // 権限が既に付与されている場合、位置情報の更新をリクエスト
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        // 位置情報の更新をリクエスト
        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)//LocationManager.NETWORK_PROVIDER
            println("success!!")
        } catch (ex: SecurityException) {
            // パーミッションがない場合の処理
            println("no!!!!!")
        }
    }

    override fun onLocationChanged(location: Location) {
        // 位置情報が更新されたときに呼び出される
        // ここで位置情報を使用する
        val latitude = location.latitude
        val longitude = location.longitude

        // 移動速度
        val getSpeed = location.speed
        val speedKmH = getSpeed * 3.6 //変換している 3.6かけるとKm/H

        val lat = findViewById<TextView>(R.id.latitude)
        val longi = findViewById<TextView>(R.id.longitude)
        val getTime = findViewById<TextView>(R.id.getTime)
        val speed = findViewById<TextView>(R.id.speed)
        lat.text = "緯度\n" + latitude.toString()
        longi.text = "経度\n" +longitude.toString()
        getTime.text = getTime()
        speed.text = speedKmH.roundToInt().toString() + "Km/h"
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    fun getTime(): String{
        val time = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        val dateNow = time.format(Date())
        return "更新時刻\n" + dateNow.toString()
    }
}