package com.example.android.besthdwallpapers.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.besthdwallpapers.databinding.FragmentGalleryBinding
import com.example.android.besthdwallpapers.ui.activity.detail.DetailActivity
import com.example.android.besthdwallpapers.ui.fragment.GalleryAdapter
import com.example.android.besthdwallpapers.ui.fragment.GalleryViewModel
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import com.example.android.besthdwallpapers.util.NetworkUtils.getNetworkLiveData
import com.example.android.besthdwallpapers.util.startActivity
import com.example.android.besthdwallpapers.util.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : BaseActivity() {
    private val binding by viewBinding(FragmentGalleryBinding::inflate)
    private val viewModel: GalleryViewModel by viewModel()
    private val query by lazy { intent.getStringExtra(PIC_EXTRA ?: "") }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = query
        }

        val galleryAdapter = GalleryAdapter { pic ->
            startActivity<DetailActivity> { putExtra(PIC_EXTRA, pic) }
        }
        galleryAdapter.addLoadStateListener {
            binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
        }

        binding.picturesRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = galleryAdapter
        }

        lifecycleScope.launch {
            query?.let { s ->
                viewModel.getPicListStream(s).collectLatest {
                    galleryAdapter.submitData(it)
                }
            }
        }
        getNetworkLiveData(applicationContext).observe(this) {
            binding.noInternetWarning.isVisible = !it
        }
    }
}