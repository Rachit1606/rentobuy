package com.example.rentobuy.modules.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rentobuy.R
import com.example.rentobuy.adapter.SearchAdapter
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    val productList: ArrayList<Product> = ArrayList()
    private lateinit var btnBack: ImageButton
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recyclerView)

        btnBack = findViewById(R.id.btnBack)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        // Initialize RecyclerView and adapter
        adapter = SearchAdapter(productList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Perform search based on query
        val query = intent.getStringExtra("query") ?: ""
        performSearch(query)

        btnBack.setOnClickListener{
            onBack()
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

    private fun performSearch(query: String) {
        Database().searchProducts(query) { products ->
            productList.clear()
            productList.addAll(products)
            adapter.notifyDataSetChanged()
        }
    }

    fun onBack() {
        onBackPressed()
    }
    override fun onBackPressed() {
        // Navigate to the previously opened activity
        super.onBackPressed()
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
}
