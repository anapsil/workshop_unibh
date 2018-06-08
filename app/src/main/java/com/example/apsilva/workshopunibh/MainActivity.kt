package com.example.apsilva.workshopunibh

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

const val CAMERA_REQUEST = 1888
const val PERMISSION_CODE = 100
const val APP_TAG = "Workshop_unibh"

class MainActivity : AppCompatActivity() {
    private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        sharePicture()
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                takePicture()
            } else {
                showPermissionDeniedMessage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(imagePath?.absolutePath)
            picture.setImageBitmap(takenImage)
        }
    }

    private fun checkPermissions() {
        if (!arePermissionsGranted()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_CODE)
        } else {
            takePicture()
        }
    }

    private fun takePicture() {
        imagePath = getPhotoFileUri()
        Log.i(APP_TAG, imagePath.toString())
        imageUri = FileProvider.getUriForFile(this, "com.example.apsilva.workshopunibh.fileprovider", imagePath!!)
        Log.i(APP_TAG, imageUri.toString())
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    private fun getPhotoFileUri(): File {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        return File(mediaStorageDir.path + File.separator + generatePictureFileName())
    }

    private fun arePermissionsGranted(): Boolean {
        permissions.iterator().forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun sharePicture() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_photo)))
    }


    private fun generatePictureFileName(): String {
        return "img_".plus(Calendar.getInstance().timeInMillis).plus(".jpg")
    }

    private fun showPermissionDeniedMessage() {

    }
}