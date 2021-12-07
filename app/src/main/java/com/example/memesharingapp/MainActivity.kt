package com.example.memesharingapp

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var customProgressDialog: Dialog? = null
    var imageurl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apicall()
        next.setOnClickListener {
            onNext()
        }
        share.setOnClickListener {
            shareMeme()
        }
    }

    private fun apicall() {

        val url = "https://meme-api.herokuapp.com/gimme/"

        //progress bar
        showProgressDialog()
        // Request a string response from the provided URL.


        try {


            val jsonrequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    imageurl = response.getString("url")
                    Glide.with(this).load(imageurl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            cancelProgressDialog()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean,
                        ): Boolean {
                            cancelProgressDialog()
                            return false
                        }

                    }).into(meme_image)
                    text_tv.text = response.getString("title")
                },
                //error Listener
                { Snackbar.make(meme_image, "Something went wrong", Snackbar.LENGTH_SHORT).show() })

// Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonrequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun shareMeme() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, checkout this cool meme from reddit: $imageurl")
        intent.type = "text/plain"
        val choser = Intent.createChooser(intent, "Share")
        startActivity(choser)

    }

    private fun onNext() {
        apicall()
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this@MainActivity)


        customProgressDialog!!.setContentView(R.layout.customdialog)


        //Start the dialog and display it on screen.
        customProgressDialog!!.show()
    }

    private fun cancelProgressDialog() {
        if (customProgressDialog != null) {
            customProgressDialog!!.dismiss()
            customProgressDialog = null
        }
    }
}