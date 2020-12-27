package com.example.memex

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentImageurl :String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme(){
        // Instantiate the RequestQueue.
        load_progress.visibility = View.VISIBLE
        next_button.isEnabled =false
        share_button.isEnabled =false
        //val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener{ response ->
                // Display the first 500 characters of the response string.
                currentImageurl = response.getString("url")
                Glide.with(this).load(currentImageurl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        load_progress.visibility =View.GONE
                        next_button.isEnabled =true
                        share_button.isEnabled =true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        load_progress.visibility =View.GONE
                        next_button.isEnabled =true
                        share_button.isEnabled =true
                        return false
                    }
                }).into(meme_image)

            },
            Response.ErrorListener {
                Toast.makeText(this,"Try Again!!",Toast.LENGTH_SHORT).show()
            })
    // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    fun shareMeme(view: View) {
        val intent =Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this cool meme $currentImageurl")

        val chooser = Intent.createChooser(intent,"Share this meme using")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}