package com.example.android.besthdwallpapers.ui.activity.favourites

import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.android.besthdwallpapers.ui.activity.BaseActivity
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.databinding.ActivityFavoritesBinding
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import com.example.android.besthdwallpapers.ui.activity.detail.DetailActivity
import com.example.android.besthdwallpapers.util.Constants.PIC_EXTRA
import com.example.android.besthdwallpapers.util.startActivity
import com.example.android.besthdwallpapers.util.viewBinding
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteActivity : BaseActivity() {
    private val binding by viewBinding(ActivityFavoritesBinding::inflate)
    private val viewModel: FavouriteViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.favorites_toolbar)
        }

        viewModel.favourites.observe(this) {
            binding.picturesRecycler.apply {
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
                adapter = FavouritesAdapter(it) {
                    val hitJson = it.hit
                    val pic = Gson().fromJson(hitJson, CommonPic::class.java)
                    startActivity<DetailActivity> {
                        putExtra(
                            PIC_EXTRA,
                            CommonPic(
                                url = pic.url,
                                width = pic.width,
                                height = pic.height,
                                tags = pic.tags,
                                imageURL = pic.imageURL,
                                fullHDURL = pic.fullHDURL,
                                id = pic.id,
                                videoId = pic.videoId
                            )
                        )
                    }
                }
            }
        }
    }
}