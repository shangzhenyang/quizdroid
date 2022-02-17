package edu.uw.quizdroid

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DownloadService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var context: Context
    private lateinit var timer: Timer
    private lateinit var url: String
    private var interval = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel(): String {
        val channelId = "quiz_droid_download_service"
        val channelName = "Quiz Droid Download Service"
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.lightColor = Color.BLUE
        channel.importance = NotificationManager.IMPORTANCE_NONE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    override fun onCreate() {
        super.onCreate()
        val notificationBuilder = NotificationCompat.Builder(
            this,
            createNotificationChannel()
        )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSubText(getString(R.string.download_service))
            .setContentText(getString(R.string.will_download_questions))
            .build()
        startForeground(101, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        context = this
        val sharedPref = getSharedPreferences("QuizDroid", MODE_PRIVATE)
        url = sharedPref
            .getString("serverUrl", "http://tednewardsandbox.site44.com/questions.json")
            .toString()
        interval = sharedPref.getString("downloadInterval", "10").toString().toInt()
        timer = Timer()
        timer.scheduleAtFixedRate(
            DownloadTask(this, handler, url),
            0,
            (interval * 60000).toLong()
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, getString(R.string.service_ended), Toast.LENGTH_SHORT).show()
    }
}

private class DownloadTask(
    val context: Context,
    val handler: Handler,
    val url: String
) : TimerTask() {
    override fun run() {
        handler.post(Runnable {
            download()
        })
    }

    private fun download() {
        Toast.makeText(context, "Downloading Questions Attempted", Toast.LENGTH_SHORT).show()
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show()
//            val filePath = context.filesDir
//            val file = File(filePath, "questions.json")
//            file.createNewFile()
//            val stream = FileOutputStream(file, false)
//            stream.use { stream ->
//                stream.write(response.toByteArray())
//            }
            Log.i("res", response)
        }, {
            AlertDialog.Builder(context)
                .setTitle("Download Failed")
                .setMessage("Failed to download the questions. Do you want to retry or quit the app?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Retry", DialogInterface.OnClickListener { _, _ ->
                    download()
                })
                .setNegativeButton("Quit", DialogInterface.OnClickListener { _, _ ->

                })
                .show()
        })
        queue.add(stringRequest)
    }
}