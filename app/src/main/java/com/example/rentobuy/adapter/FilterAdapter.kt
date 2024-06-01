package com.example.rentobuy.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentobuy.R
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.products.ProductDetailsActivity

class FilterAdapter(private val context: Context, private val productList: MutableList<Product>) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProducts(newList: List<Product>) {
        productList.clear()
        productList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewProduct: ImageView = itemView.findViewById(R.id.imageViewProduct)
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)

        fun bind(product: Product) {
            itemView.apply {
                titleTextView.text = product.title
                priceTextView.text = product.price
                descriptionTextView.text = product.description
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
}
