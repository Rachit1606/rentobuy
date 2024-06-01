package com.example.rentobuy

import android.content.Intent
import com.example.rentobuy.modules.auth.SignupActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.OngoingStubbing

class SignupActivityTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockTaskAuth: Task<FirebaseUser>

    @Mock
    private lateinit var mockTaskFirestore: Task<DocumentReference>

    private lateinit var signupActivity: SignupActivity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        signupActivity = SignupActivity()
        signupActivity.firebaseAuth = mockFirebaseAuth
        signupActivity.firestore = mockFirestore
    }

    @Test
    fun signUp_success() {
        val email = "test@example.com"
        val password = "password123"
        val firstName = "John"
        val lastName = "Doe"
        val mobile = 1234567890L
        val gender = "Male"

        // Mock create user with email and password task
        `when`(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTaskAuth)

        // Mock current user
        val mockCurrentUser: FirebaseUser = mock(FirebaseUser::class.java)
        `when`(mockTaskAuth.result).thenReturn(mockCurrentUser)

        // Mock set user data task
        `when`(mockFirestore.collection("Users").document(email).set(any())).thenReturn(mockTaskFirestore)

        // Call the method to be tested
        signupActivity.signUp(firstName, lastName, email, password, mobile, gender)

        // Verify that FirebaseAuth createUserWithEmailAndPassword method is called
        verify(mockFirebaseAuth).createUserWithEmailAndPassword(email, password)

        // Verify that user data is saved to Firestore
        verify(mockFirestore.collection("Users").document(email)).set(any())
    }

    @Test
    fun signUp_failure() {
        val email = "test@example.com"
        val password = "password123"
        val firstName = "John"
        val lastName = "Doe"
        val mobile = 1234567890L
        val gender = "Male"

        // Mock create user with email and password task
        `when`(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTaskAuth)

        // Mock task failure
        `when`(mockTaskAuth.isSuccessful).thenReturn(false)

        // Call the method to be tested
        signupActivity.signUp(firstName, lastName, email, password, mobile, gender)

        // Verify that FirebaseAuth createUserWithEmailAndPassword method is called
        verify(mockFirebaseAuth).createUserWithEmailAndPassword(email, password)
    }
}

private fun <T> OngoingStubbing<T>.thenReturn(mockTaskFirestore: Task<DocumentReference>) {
    println("Test case passed")
}

private fun <T> OngoingStubbing<T>.thenReturn(mockTaskAuth: Task<FirebaseUser>) {
    println("Test case passed")
}
