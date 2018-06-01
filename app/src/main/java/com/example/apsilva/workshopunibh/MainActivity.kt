package com.example.apsilva.workshopunibh

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        takePicture.setOnClickListener { _ -> takePicture() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> sharePicture()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun takePicture() {
        Toast.makeText(this, R.string.take_picture, Toast.LENGTH_SHORT).show()
    }

    private fun sharePicture() {
        Toast.makeText(this, R.string.share_picture, Toast.LENGTH_SHORT).show()
    }
}