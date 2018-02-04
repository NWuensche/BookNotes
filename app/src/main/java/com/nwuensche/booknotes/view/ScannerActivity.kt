package com.nwuensche.booknotes.view

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
import com.google.android.gms.vision.barcode.Barcode
import com.nwuensche.booknotes.R

import kotlinx.android.synthetic.main.activity_scanner.*
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever
import android.content.Intent
import android.view.View


class ScannerActivity : AppCompatActivity(), BarcodeRetriever {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE;
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        this.getWindow().getDecorView().setSystemUiVisibility(flags)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val barcodeCapture = supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture
        barcodeCapture.setRetrieval(this)

    }

    override fun onPermissionRequestDenied() {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@ScannerActivity)
                    .setTitle("Keine Berechtigung")
                    .setMessage("Keine Berechtigung")
            builder.show()
        }
    }

    override fun onRetrieved(p0: Barcode?) {
        val intent = Intent()
        intent.putExtra("ISBN", p0!!.displayValue)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onRetrievedMultiple(closetToClick: Barcode, barcodeGraphics: List<BarcodeGraphic>) {
        runOnUiThread {
            var message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                    "codes in frame include : \n"
            for (index in barcodeGraphics.indices) {
                val barcode = barcodeGraphics[index].barcode
                message += (index + 1).toString() + ". " + barcode.displayValue + "\n"
            }
            val builder = AlertDialog.Builder(this@ScannerActivity)
                    .setTitle("code retrieved")
                    .setMessage(message)
            builder.show()
        }

    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>) {
        // when image is scanned and processed
    }

    override fun onRetrievedFailed(reason: String) {
        // in case of failure
    }

}
