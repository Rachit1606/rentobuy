package com.example.rentobuy.modules.products

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.model.WishListModel
import com.example.rentobuy.modules.cart.CartActivity
import com.example.rentobuy.modules.home.HomePageActivity
import com.example.rentobuy.modules.home.UserProfileActivity
import com.example.rentobuy.modules.seller_inventory.SellerInventoryActivity
import com.example.rentobuy.modules.wishlist.WishListActivity
import com.example.rentobuy.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProductUploadActivity : AppCompatActivity() {

    private lateinit var imageViewProduct: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSubmit: Button
    private lateinit var titleEditText: EditText
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var priceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var stockQuantityEditText: EditText

    private var selectedImageUri: Uri? = null

    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_upload)

        imageViewProduct = findViewById(R.id.imageViewProduct)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        titleEditText = findViewById(R.id.editTextTitle)
        priceEditText = findViewById(R.id.editTextPrice)
        bottomNavigationView = findViewById(R.id.bottomNavigationBar)

        descriptionEditText = findViewById(R.id.editTextDescription)
        stockQuantityEditText = findViewById(R.id.editTextStockQuantity)

        storageReference = FirebaseStorage.getInstance().reference

        buttonSelectImage.setOnClickListener {
            selectImage()
            //setHardcodedDrawableImage()

        }

        buttonSubmit.setOnClickListener {
            uploadProduct()
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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

//    private fun selectImage() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(intent, REQUEST_IMAGE_PICK)
//    }

//    private fun setHardcodedDrawableImage() {
//        // Set a hardcoded drawable image for testing
//        imageViewProduct.setImageResource(R.drawable.icon)
//        // Set selectedImageUri to null as we are not using a real image URI
//        //selectedImageUri = null
//    }

    private fun uploadProduct() {
        val title = titleEditText.text.toString().trim()
        val price = priceEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val stockQuantity = stockQuantityEditText.text.toString().trim()

        println("going to check upload image conditions 2...")

        // Check if all fields are filled
        if (title.isEmpty() || price.isEmpty() || description.isEmpty() || stockQuantity.isEmpty() || selectedImageUri == null) {
            Toast.makeText(
                this,
                "Please fill in all fields and select an image",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // URI of the uploaded image
                    val imageUrl = uri.toString()
                    // Update the product object with the image URL
                    val product = Product(
                        title = title,
                        price = price,
                        description = description,
                        stock_quantity = stockQuantity,
                        user_id = Database().getUserID(),
                        image = imageUrl
                    )

                    // Call Firestore upload method with the product
                    Database().uploadProductDetails(product) { isSuccess ->
                        if (isSuccess) {
                            println("success storing in Firestore...")
                            Toast.makeText(
                                this,
                                "Product uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Reset EditText fields and ImageView after successful upload
                            titleEditText.text.clear()
                            priceEditText.text.clear()
                            descriptionEditText.text.clear()
                            stockQuantityEditText.text.clear()
                            imageViewProduct.setImageResource(android.R.color.transparent)
                            selectedImageUri = null
                            finish()
                        } else {
                            println("failed to store data in Firestore...")
                            Toast.makeText(this, "Failed to upload product", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            // Display the selected image in the ImageView
            imageViewProduct.setImageURI(selectedImageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }

}
