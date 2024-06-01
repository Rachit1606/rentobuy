package com.example.rentobuy.modules.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rentobuy.R
import com.example.rentobuy.database.Database
import com.example.rentobuy.model.Cart
import com.example.rentobuy.model.Product
import com.example.rentobuy.modules.orders.MyOrdersActivity

class PaymentActivity : AppCompatActivity() {
    lateinit var payNowBtn : Button
    lateinit var cardNum : EditText
    lateinit var expDate : EditText
    lateinit var cardHolderName : EditText
    lateinit var cvv : EditText
    lateinit var progressBar : ProgressBar
    lateinit var totalCost : TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        payNowBtn = findViewById(R.id.pay_button)

        totalCost = findViewById(R.id.totalCost)

        val cartList: ArrayList<Cart>? = intent.getParcelableArrayListExtra(EXTRA_PRODUCT)
        var totalCartCost = 0
        if (cartList != null) {
            for(item in cartList){
                Log.d("Cart List Items", "title ${item.title}, price ${item.price}")
                totalCartCost += (item.price.toInt() * item.cart_quantity.toInt())
            }
            var deliveryCharge = 0.08 * totalCartCost // 8% delivery charge
            val tax = 0.05 * totalCartCost // 5% tax

            deliveryCharge = if (deliveryCharge > 20) 20.0 else deliveryCharge
val totalAmount = deliveryCharge+tax+totalCartCost;
            totalCost.text = "Total Cost: $${totalAmount}"
        }


        payNowBtn.setOnClickListener(){

            cardNum = findViewById(R.id.editTextCardNumber)
            expDate = findViewById(R.id.editTextExpirationDate)
            cardHolderName = findViewById(R.id.editTextCardName)
            cvv = findViewById(R.id.editTextCVV)
            progressBar = findViewById(R.id.progressBar)


            //for demo purposes, only check if card holder name is equal to jane doe.
            //as this is a payment simulation, we cannot do proper verification checks with api's provided by google pay
            if(cardHolderName.text.toString() != "Jane Doe"){
                Toast.makeText(this, "Invalid Card Holder name", Toast.LENGTH_SHORT).show()
            }else{
                //simulate processing payment
                progressBar.visibility = View.VISIBLE

                //Logically, this is where you would call the backend API's to validate card details
                android.os.Handler().postDelayed({
                    Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show()

                    progressBar.visibility = View.GONE
                    //empty cart, send data to order history, navigate to order history

                    val orderHistoryIntent = Intent(this, MyOrdersActivity::class.java)
                    orderHistoryIntent.putParcelableArrayListExtra(EXTRA_PRODUCT, cartList)
                    Database().clearCart()
                    startActivity(orderHistoryIntent)
                }, 5000)

            }
        }

    }
    companion object{
        const val EXTRA_PRODUCT = "extra_product"
    }
}