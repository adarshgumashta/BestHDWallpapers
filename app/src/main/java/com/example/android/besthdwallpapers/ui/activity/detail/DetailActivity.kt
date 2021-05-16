package com.example.android.besthdwallpapers.ui.activity.detail

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ablanco.zoomy.Zoomy
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.databinding.ActivityDetailBinding
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import com.example.android.besthdwallpapers.ui.activity.BaseActivity
import com.example.android.besthdwallpapers.util.*
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseActivity() {
    private val binding by viewBinding(ActivityDetailBinding::inflate)
    private val viewModel: DetailViewModel by viewModel()
    private var pic: CommonPic? = null
    private var permissionCheck = 0
    private var isSave = false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        pic = intent.getParcelableExtra(PIC_EXTRA) as CommonPic?

        pic?.imageURL?.let {
            loadImage(it, binding.image, binding.progressBar, R.color.gray)
        }?:run{
            pic?.url?.let { loadImage(it, binding.image, binding.progressBar, R.color.gray) }
        }

        Zoomy.Builder(this).doubleTapListener { viewModel.addorRemoveFromFavourites(pic) }
            .target(binding.image).apply { register() }

        viewModel.isFavourite(pic?.url)

        viewModel.isFavourite.observe(this) {
            binding.bottomAppBar.menu.findItem(R.id.action_add_to_fav).setIcon(
                if (it) R.drawable.ic_star_red_24dp else R.drawable.ic_star_border
            )
        }
        binding.bottomAppBar.setNavigationOnClickListener { onBackPressed() }
        binding.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> share(pic?.imageURL)
                R.id.action_download -> {
                    isSave = true
                    checkPermission()
                }
                R.id.action_add_to_fav -> viewModel.addorRemoveFromFavourites(pic)
            }
            true
        }

        binding.fab.setOnClickListener {
            isSave = false
            checkPermission()
        }
    }

    override fun onDestroy() {
        Zoomy.unregister(binding.image)
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (isSave) {
                true -> saveImage()
                false -> setAsWallpaper()
            }
        } else {
            shortToast(getString(R.string.you_need_perm_toast))
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun checkPermission() {
        permissionCheck = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            when (isSave) {
                true -> saveImage()
                false -> setAsWallpaper()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        try {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                val requestCode = 102
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    requestCode
                )
            }
        } catch (e: Exception) {
            shortToast(e.printStackTrace().toString())
        }
    }

    private fun saveImage() {
        if (!isNetworkAvailable()) {
            shortToast(getString(R.string.no_internet))
            return
        }
        pic?.let { saveImage(it.imageURL ?: "", binding.progressBar) }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setAsWallpaper() {
        val manager = WallpaperManager.getInstance(applicationContext)
        lifecycleScope.launch(Dispatchers.IO)
        {
            try {
                startActivity(Intent(manager.getCropAndSetWallpaperIntent(getImageUri(pic))))
            } catch (e: Exception) {
                val bitmap = getBitmap(pic)
                withContext(Dispatchers.Main)
                {
                    manager.setBitmap(bitmap)
                }
            }
        }
    }
}