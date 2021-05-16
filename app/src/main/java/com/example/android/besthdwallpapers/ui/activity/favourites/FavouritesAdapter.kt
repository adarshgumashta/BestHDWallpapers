package com.example.android.besthdwallpapers.ui.activity.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.model.local.Favourite
import com.example.android.besthdwallpapers.util.loadImage

class FavouritesAdapter(
    private val favourites: List<Favourite>,
    private val clickListener: (Favourite) -> Unit
) : RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {
    class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavouritesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
    )

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val favorite = favourites[position]
        with(holder) {
            itemView.context.loadImage(favorite.url, image, null, R.color.gray)
            itemView.setOnClickListener { clickListener(favorite) }
        }
    }

    override fun getItemCount() = favourites.size

}