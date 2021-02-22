package com.tubianto.torchflashlight

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var mCameraManager: CameraManager
    private lateinit var mCameraId:String
    private lateinit var toggleButton: ToggleButton
    private lateinit var imageview: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFlashAvailable = applicationContext.packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (!isFlashAvailable)
        {
            showNoFlashError()
        }

        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try
        {
            mCameraId = mCameraManager.cameraIdList[0]
        }
        catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        imageview = findViewById(R.id.imageView)
        toggleButton = findViewById(R.id.toggleButton)
        toggleButton.setOnCheckedChangeListener { _, isChecked -> switchFlashLight(isChecked) }
    }

    private fun showNoFlashError() {
        val alert = AlertDialog.Builder(this)
                .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> finish() }
        alert.show()
    }

    private fun switchFlashLight(status:Boolean) {
        try
        {
            mCameraManager.setTorchMode(mCameraId, status)
            if (status){
                imageview.setImageResource(R.drawable.trans_on)
            } else {
                imageview.setImageResource(R.drawable.trans_off)
            }
        }
        catch (e:CameraAccessException) {
            e.printStackTrace()
        }
    }
}
