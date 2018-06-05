package com.example.apsilva.workshopunibh

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val CAMERA_REQUEST = 1888
const val PERMISSION_CODE = 100

class MainActivity : AppCompatActivity() {
    val pictureFileName by lazy { generatePictureFileName() }
    val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var shareActionProvider: ShareActionProvider? = null
    var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        takePicture.setOnClickListener { _ -> checkPermissions() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        val menuItem = menu?.findItem(R.id.action_share)

        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider

        return true
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
            val photo = data?.extras?.get("data") as Bitmap
            picture.setImageBitmap(photo)
            getShareIntent()
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
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    private fun arePermissionsGranted(): Boolean {
        permissions.iterator().forEach {
            if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
//    private fun sharePicture() {
//        Toast.makeText(this, R.string.share_picture, Toast.LENGTH_SHORT).show()
//    }

    private fun getShareIntent() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
//        myShareIntent.putExtra(Intent.EXTRA_STREAM, myImageUri)
        shareActionProvider?.setShareIntent(shareIntent)
    }


    private fun generatePictureFileName(): String {
        return "img_".plus(Calendar.getInstance().timeInMillis).plus(".jpg")
    }

    private fun showPermissionDeniedMessage() {

    }
}