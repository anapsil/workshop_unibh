package com.example.apsilva.workshopunibh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


const val CAMERA_REQUEST = 1888
const val PERMISSION_CODE = 100
const val APP_TAG = "Workshop_unibh"

class MainActivity : AppCompatActivity() {
    private var imagePath: File? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        takePictureButton.setOnClickListener { _ -> checkPermissions() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (isPictureCaptured()) {
            sharePicture()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // adicione aqui o código
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // adicione aqui o código
    }

    private fun checkPermissions() {
        // adicione aqui o código
    }

    private fun takePicture() {
        // adicione aqui o código
    }

    private fun sharePicture() {
        // adicione aqui o código
    }

    private fun getPhotoFileUri(): File {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        return File(mediaStorageDir.path + File.separator + generatePictureFileName())
    }

    private fun isPictureCaptured(): Boolean {
        if (imagePath != null) {
            return true
        }
        Toast.makeText(this, getString(R.string.take_photo_required),
                Toast.LENGTH_LONG).show()
        return false
    }

    private fun generatePictureFileName(): String {
        return "img_".plus(Calendar.getInstance().timeInMillis).plus(".jpg")
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(this, getString(R.string.permission_denied),
                Toast.LENGTH_LONG).show()
    }
}