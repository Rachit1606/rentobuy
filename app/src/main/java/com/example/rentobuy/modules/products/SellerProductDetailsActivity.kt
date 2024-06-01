package com.example.rentobuy.modules.products


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.example.rentobuy.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView


class SellerProductDetailsActivity : AppCompatActivity() {
    private lateinit var productImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var descriptionTextView: TextView
    private lateinit var stockQuantityTextView: TextView
    private lateinit var modifyButton: Button
    private lateinit var removeButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_product_details)

        // Initialize views
        titleTextView = findViewById(R.id.sellerTitleTextView)
        priceTextView = findViewById(R.id.sellerPriceTextView)
        descriptionTextView = findViewById(R.id.sellerDescriptionTextView)
        stockQuantityTextView = findViewById(R.id.sellerStockQuantityTextView)
        modifyButton = findViewById(R.id.sellerModifyButton)
        removeButton = findViewById(R.id.sellerRemoveButton)

        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        val productImageView = findViewById<ImageView>(R.id.sellerProductImageView)


        // Get product details from intent
        val product = intent.getParcelableExtra<Product>(EXTRA_PRODUCT)
        if (product != null) {
            // Set product details to views
            titleTextView.text = product.title
            priceTextView.text = "Price: $${product.price}"
            descriptionTextView.text = "Description: ${product.description}"
            stockQuantityTextView.text = "Stock Quantity: ${product.stock_quantity}"

            Glide.with(this)
                .load(product.image) // Replace product.imageUrl with the actual URL of the image
                .placeholder(R.drawable.icon) // Placeholder image while loading (optional)
                .into(productImageView)

            modifyButton.setOnClickListener {
                val modifyProductIntent = Intent(this, ModifySellerProductsActivity::class.java)
                modifyProductIntent.putExtra( EXTRA_PRODUCT, product)
                startActivity(modifyProductIntent)
                finish()
            }

            // Chat Button click Listener
            removeButton.setOnClickListener {
                Database().removeProductFromDB(product.product_id)
                Toast.makeText(this, "Product: ${product.title} successfully removed", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Failed to retrieve product details", Toast.LENGTH_SHORT).show()
            finish() // Finish activity if product details are not available
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> redirectToProfile()
                R.id.navigation_wishlist -> redirectToWishlist()
                R.id.navigation_cart -> redirectToCart()
                R.id.navigation_home -> redirectToHome()
            }
            true
        }
    }

    private fun addToWishList(item : Product){
        val wishListModel = WishListModel(
            Database().getUserID(),
            item.user_id,
            item.product_id,
            item.title,
            item.price,
            item.image
        )
        Database().addItemInWishList(wishListModel,response = {
            if (it) {
                Toast.makeText(this, "Wish List updated.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Wish List error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToCart(item: Product) {
        val userID = Database().getUserID() // Assuming this method retrieves the user ID
        val cartItem = Cart(
            userID,
            item.user_id,
            item.product_id,
            item.title,
            item.price,
            item.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        Database().addItemToCart(cartItem) { success ->
            if (success) {
                runOnUiThread {
                    Toast.makeText(this, "Cart Item updated.", Toast.LENGTH_SHORT).show()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Cart Item error.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun redirectToProfile() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToWishlist() {
        val intent = Intent(this, WishListActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToHome(){
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
        private const val TAG = "ProductDetailsActivity"

    }
}