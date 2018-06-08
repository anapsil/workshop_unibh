package com.example.apsilva.workshopunibh

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), PERMISSION_CODE)
        } else {
            takePicture()
        }
    }

    private fun takePicture() {
        imagePath = getPhotoFileUri()
        imageUri = FileProvider.getUriForFile(this,
                "com.example.apsilva.workshopunibh.fileprovider", imagePath!!)
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

    private fun sharePicture() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$imagePath"))
        } else {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_photo)))

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