package com.nwuensche.booknotes.view

import com.nwuensche.booknotes.R
import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*

class BarCodeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val REQUEST_CODE = 1
    private var alreadyAskedPermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
//        askPermissionCameraOrClose()
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView).apply {

            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE

            decodeCallback = DecodeCallback {
                val d = Intent()
                d.putExtra("value", it.text)
                setResult(RESULT_OK, d)
                finish()
            }

        }


        //No toast in here, because this will be always shown in permission dialog, which is annoying
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            //Asking for permission
            if (!alreadyAskedPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE
                ) // Permission Dialog
                alreadyAskedPermission = true
            } else { // Permission not granted or touched outside of dialog, thus this lambda called again
                val d = Intent()
                d.putExtra(
                    "value",
                    ""
                ) //There is no empty QR Code, so this is enough to show permission denied
                setResult(RESULT_OK, d)
                finish()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}