package com.example.android.besthdwallpapers.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.concurrent.TimeUnit

inline fun <reified T : Activity> Activity.startActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
}

fun Activity.getImageUri(pic: CommonPic?): Uri {
    val bitmap = getBitmap(pic)
    val bytes = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
    return Uri.parse(path)
}

fun Activity.getBitmap(pic: CommonPic?): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        bitmap = Glide.with(this)
            .asBitmap()
            .load(pic?.imageURL)
            .submit()
            .get()
    } catch (e: IOException) {
    }
    return bitmap
}

fun Context.isNetworkAvailable() =
    (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetworkInfo?.isConnectedOrConnecting
        ?: false

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.loadImage(url: String, view: ImageView, progressBar: ProgressBar?, color: Int) =
    Glide.with(this).load(url).placeholder(color).thumbnail(.1F).listener(object :
        RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
            return false
        }
    }).into(view)

fun Context.share(text: String?) {
    val intent = Intent().apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
    }
    try {
        startActivity(Intent.createChooser(intent, getString(R.string.choose_share)))
    } catch (e: Exception) {
        shortToast(getString(R.string.cant_share))
    }
}


fun Context.shortToast(text: String?) = makeText(this, text, Toast.LENGTH_SHORT)

fun Activity.saveImage(url: String, progressBar: ProgressBar) = Glide.with(this)
    .asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        resource.saveImageQ(this@saveImage, progressBar)
                    } else {
                        resource.saveImage(this@saveImage, progressBar)
                    }
                }
            }
        }

        override fun onLoadCleared(placeholder: Drawable?) {
        }
    })

private fun Bitmap.saveImage(activity: Activity, progressBar: ProgressBar) {
    val root =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
    val myDir = File("$root/Wallpapers")
    myDir.mkdirs()
    val randomInt = (0..10000).random()
    val fileName = "Image-$randomInt.jpg"
    val file = File(myDir, fileName)
    if (file.exists()) file.delete()
    try {
        val outputStream = FileOutputStream(file)
        compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: java.lang.Exception) {
    }
    MediaScannerConnection.scanFile(activity, arrayOf(file.toString()), null) { _, _ ->
    }
    activity.runOnUiThread {
        progressBar.gone()
        activity.shortToast(activity.getString(R.string.down_complete))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Bitmap.saveImageQ(activity: Activity, progressBar: ProgressBar) {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/" + "Wallpapers")
        put(MediaStore.MediaColumns.IS_PENDING, true)
    }

    val uri: Uri? =
        activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    if (uri != null) {
        saveImageToStream(activity.contentResolver.openOutputStream(uri))
        values.put(MediaStore.MediaColumns.IS_PENDING, false)
        activity.contentResolver.update(uri, values, null, null)
    }
    activity.runOnUiThread {
        progressBar.gone()
        activity.shortToast(activity.getString(R.string.down_complete))
    }
}

private fun Bitmap.saveImageToStream(outputStream: OutputStream?) {
    outputStream?.let {
        try {
            compress(Bitmap.CompressFormat.PNG, 100, it)
            it.close()
        } catch (e: Exception) {
        }
    }
}

fun Long.runDelayed(action: () -> Unit) =
    Handler(Looper.getMainLooper()).postDelayed(action, TimeUnit.MILLISECONDS.toMillis(this))

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) { bindingInflater.invoke(layoutInflater) }

fun SharedPreferences.putAny(key: String, any: Any) {
    when (any) {
        is String -> edit().putString(key, any).apply()
        is Int -> edit().putInt(key, any).apply()
    }
}

fun SharedPreferences.getAny(type: Any, key: String): Any {
    return when (type) {
        is String -> getString(key, "") as Any
        else -> getInt(key, 0)
    }
}