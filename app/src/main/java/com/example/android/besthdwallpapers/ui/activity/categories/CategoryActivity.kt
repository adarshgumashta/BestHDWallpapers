package com.example.android.besthdwallpapers.ui.activity.categories

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.besthdwallpapers.databinding.ActivityCategoriesBinding
import com.example.android.besthdwallpapers.ui.activity.BaseActivity
import com.example.android.besthdwallpapers.ui.activity.GalleryActivity
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import com.example.android.besthdwallpapers.util.startActivity
import com.example.android.besthdwallpapers.util.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryActivity : BaseActivity() {
    private val binding by viewBinding(ActivityCategoriesBinding::inflate)
    private val viewModel: CategoryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.categories.observe(this) {
            binding.recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(this@CategoryActivity, 2)
                adapter = CategoryAdapter(it) {
                    startActivity<GalleryActivity> { putExtra(PIC_EXTRA, it.categoryName) }
                }
            }
        }

        viewModel.progressIsVisible.observe(this) {
            binding.progressBar.isVisible = it
        }
    }
}