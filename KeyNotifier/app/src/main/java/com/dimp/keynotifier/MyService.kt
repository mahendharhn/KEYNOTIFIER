package com.dimp.keynotifier


import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.dimp.keynotifier.constraints.CHANNEL_ID
import com.dimp.keynotifier.constraints.MUSIC_NOTIFICATION


class MyService: Service() {

    init {
        instance=this
    }
    companion object{
        fun stopService() {
            println("HELLO")
            isRunning = false

            instance.stopSelf()
            instance.stopForeground(true)


        }
        lateinit var run : Runnable
        var hand = Handler()
        private lateinit var instance : MyService
        var isRunning= false

    }

    private lateinit var musicplayer : MediaPlayer
    override fun onBind(intent: Intent?) = null

    override fun onCreate(){
        super.onCreate()
        initMusic()
        val wmgr = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        var run = object : Runnable {
            override fun run() {



                if(isRunning == false){
                    hand.removeCallbacks(this)

                }else{

                    var wifiInfo = wmgr.connectionInfo
                    var rssi = wifiInfo.rssi

                    var ssid=wifiInfo.macAddress
                    println(ssid)
                    println(rssi)
                    Log.i("hello", rssi.toString())
                    var count=2000
                    if (rssi < -50 && rssi > -60 && count%2000==0) {
                        count++
                        musicplayer.start()

                    }
                    hand.postDelayed(this, 1500)
                }}

        }

        hand.postDelayed(run,0)
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()




        isRunning=true



        return START_NOT_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification() {
        val notificationIntent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,0,notificationIntent,0
        )
        val notification = Notification
            .Builder(this,CHANNEL_ID)
            .setContentText("KEY NOTIFIER IS ON")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(MUSIC_NOTIFICATION,notification)

    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            val servicechannel = NotificationChannel(
                CHANNEL_ID,"My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(servicechannel)
        }
    }

    private fun initMusic(){

        musicplayer= MediaPlayer.create(this ,this.resources.getIdentifier("voice","raw",this.packageName))
        musicplayer.setVolume(100F,100F)


    }
    override fun onDestroy() {
        println("Killed")
        super.onDestroy()
    }
}