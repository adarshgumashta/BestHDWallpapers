package com.example.android.besthdwallpapers.ui.activity

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.android.besthdwallpapers.databinding.ActivitySearchBinding
import com.example.android.besthdwallpapers.ui.activity.detail.DetailActivity
import com.example.android.besthdwallpapers.ui.fragment.GalleryAdapter
import com.example.android.besthdwallpapers.ui.fragment.GalleryViewModel
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import com.example.android.besthdwallpapers.util.startActivity
import com.example.android.besthdwallpapers.util.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity() {

    private val binding by viewBinding(ActivitySearchBinding::inflate)
    private val viewModel: GalleryViewModel by viewModel()
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener { onBackPressed() }

        galleryAdapter = GalleryAdapter { pic ->
            startActivity<DetailActivity> { putExtra(PIC_EXTRA, pic) }
        }

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            adapter = galleryAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getPictures(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                getPictures(newText)
                return true
            }
        })

        binding.searchView.onActionViewExpanded()
    }

    private fun getPictures(query: String) {
        lifecycleScope.launch {
            viewModel.getPicListStream(query).collectLatest {
                galleryAdapter.submitData(it)
            }
        }
    }
}