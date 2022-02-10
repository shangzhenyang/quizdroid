package edu.uw.quizdroid

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            initApp()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        }

        val filePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/questions.json"
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(
                applicationContext,
                getString(R.string.file_not_exist, filePath),
                Toast.LENGTH_LONG
            ).show()
            return
        }
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
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initApp()
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.no_permission_read_file),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initApp() {
        val listTopic = findViewById<ListView>(R.id.list_topic)
        val topics = Topics().getAll()
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