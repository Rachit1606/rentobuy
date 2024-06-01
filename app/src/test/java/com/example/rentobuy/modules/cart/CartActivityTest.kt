package com.example.rentobuy.modules.cart

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

class CartActivityTest {

        @Test
        fun testCalculateTax() {
            // Given
            val subTotal = 100.0

            // When
            val tax = CartActivity.calculateTax(subTotal)

            // Then
            assertEquals(5.0, tax, 0.0)
        }

        @Test
        fun testCalculateTotalAmount() {
            // Given
            val subTotal = 100.0

            // When
            val totalAmount = CartActivity.calculateTotalAmount(subTotal)

            // Then
            assertEquals(113.0, totalAmount, 0.0)
        }

        @Test
        fun testCalculateDeliveryCharge() {
            // Given
            val subTotal = 100.0

            // When
            val deliveryCharge = CartActivity.calculateDeliveryCharge(subTotal)

            // Then
            assertEquals(8.0, deliveryCharge, 0.0)
        }

}
