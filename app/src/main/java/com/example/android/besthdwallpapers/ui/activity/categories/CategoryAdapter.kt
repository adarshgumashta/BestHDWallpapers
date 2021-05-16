package com.example.android.besthdwallpapers.ui.activity.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.besthdwallpapers.R
import com.example.android.besthdwallpapers.model.remote.response.Category
import com.example.android.besthdwallpapers.util.loadImage

class CategoryAdapter(
    private val categories: List<Category>,
    private val clickListener: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_category, parent, false
        )
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        with(holder)
        {
            categoryName.text = category.categoryName
            itemView.context.loadImage(category.categoryUrl, categoryImage, null, R.color.gray)
            itemView.setOnClickListener { clickListener(category) }
        }
    }

    override fun getItemCount() = categories.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImage: ImageView = itemView.findViewById(R.id.categoryImageView)
        var categoryName: TextView = itemView.findViewById(R.id.categoryTitle)
    }

}