package edu.uw.quizdroid

import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.File

class MainActivity : AppCompatActivity() {
    private val broadcastId = "uw.edu.quizdroid.download"
    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.getStringExtra("action")) {
                    "downloadFail" -> {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle(getString(R.string.download_failed))
                            .setMessage(getString(R.string.failed_to_download_questions))
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(getString(R.string.retry), DialogInterface.OnClickListener { _, _ ->
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(getString(R.string.tip))
                                    .setMessage(getString(R.string.will_open_preferences))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { _,_ ->
                                        startActivity(
                                            Intent(this@MainActivity, PreferencesActivity::class.java)
                                        )
                                    })
                                    .show()
                            })
                            .setNegativeButton(getString(R.string.quit), DialogInterface.OnClickListener { _, _ ->
                                finish()
                            })
                            .show()
                    }
                    "downloadSuccess" -> {
                        initApp()
                    }
                }
            }
        }

        val isAirplaneMode = Settings.System.getInt(applicationContext.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        if (isAirplaneMode) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_internet))
                .setMessage(getString(R.string.airplane_mode_on))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        } else {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
            if (isConnected) {
                val intent = Intent(applicationContext, DownloadService::class.java)
                startForegroundService(intent)
            } else {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.no_internet))
                    .setMessage(getString(R.string.failed_connect_internet))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(broadcastId))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(applicationContext, DownloadService::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.preferences) {
            startActivity(
                Intent(this, PreferencesActivity::class.java)
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initApp()
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_permission))
                .setMessage(getString(R.string.no_permission_read_file))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { _, _ ->
                    finish()
                })
                .show()
        }
    }

    private fun initApp() {
        val filePath = applicationContext.filesDir
        val file = File(filePath, "questions.json")
        if (!file.exists()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.file_not_exist, filePath),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val listTopic = findViewById<ListView>(R.id.list_topic)
        val topics = Topics(applicationContext).getAll()
        val listItems = ArrayList<Map<String, Any>>()
        for (item in topics) {
            val map = HashMap<String, Any>()
            map["title"] = item.title
            map["description"] = item.shortDescription
            map["icon"] = item.icon
            listItems.add(map)
        }
        val from = arrayOf("title", "description", "icon")
        val to = arrayOf(
            R.id.label_quiz_title,
            R.id.label_quiz_description,
            R.id.icon_quiz
        ).toIntArray()
        listTopic.adapter = SimpleAdapter(this, listItems, R.layout.list_item, from, to)
        listTopic.setOnItemClickListener {_, _, position, _ ->
            startActivity(
                Intent(this, TopicOverviewActivity::class.java).apply {
                    putExtra("topic", topics.elementAt(position).title)
                }
            )
        }
    }
}
