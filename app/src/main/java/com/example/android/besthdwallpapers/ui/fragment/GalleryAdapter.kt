package com.example.android.besthdwallpapers.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.databinding.ItemPictureBinding
import com.example.android.besthdwallpapers.model.remote.response.CommonPic
import com.example.android.besthdwallpapers.util.loadImage

class GalleryAdapter(
    private val clickListener: (CommonPic?) -> Unit
) : PagingDataAdapter<CommonPic, RecyclerView.ViewHolder>(DiffCallback) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val picture = getItem(position)
        (holder as GalleryViewHolder).apply {
            picture?.url?.let {
                itemView.context.loadImage(it, itemBinding.image, null, R.color.gray)
            }
            itemView.setOnClickListener { clickListener(picture) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        GalleryViewHolder(
            ItemPictureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class GalleryViewHolder(val itemBinding: ItemPictureBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    companion object {
        object DiffCallback : DiffUtil.ItemCallback<CommonPic>() {
            override fun areItemsTheSame(oldItem: CommonPic, newItem: CommonPic) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CommonPic, newItem: CommonPic) =
                oldItem.id == newItem.id
        }
    }
}