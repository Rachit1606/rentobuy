package com.example.rentobuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.products.ProductDetailsActivity
import com.bumptech.glide.Glide

class RecentProductAdapter(private val context: Context, private val productList: List<Product>) :
    RecyclerView.Adapter<RecentProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_recylerview, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val buttonFavorite: ImageButton = itemView.findViewById(R.id.buttonFavorite)

        fun bind(product: Product) {
            // Set product details to views
            textViewTitle.text = product.title
            textViewPrice.text = "Price: ${product.price}"
            Glide.with(context)
                .load(product.image) // Assuming product.image is the URL stored in Firestore
                .placeholder(R.drawable.icon) // Placeholder image while loading
                .into(imageViewProduct)
            // Set OnClickListener to navigate to ProductDetailsActivity
            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(ProductDetailsActivity.EXTRA_PRODUCT, product)
                context.startActivity(intent)
            }
        }
    }
}