package com.example.rentobuy

import com.example.rentobuy.modules.auth.ForgotPasswordActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ForgotPasswordActivityTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<Void>

    private lateinit var forgotPasswordActivity: ForgotPasswordActivity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        forgotPasswordActivity = ForgotPasswordActivity()
        forgotPasswordActivity.firebaseAuth = mockFirebaseAuth
    }

    @Test
    fun resetPassword_success() {
        val email = "test@example.com"

        // Mock send password reset email task
        `when`(mockFirebaseAuth.sendPasswordResetEmail(email)).thenReturn(mockTask)

        // Call the method to be tested
        forgotPasswordActivity.resetPassword(email)

        // Verify that FirebaseAuth sendPasswordResetEmail method is called
        verify(mockFirebaseAuth).sendPasswordResetEmail(email)
    }

    @Test
    fun resetPassword_failure() {
        val email = "test@example.com"

        // Mock send password reset email task failure
        `when`(mockFirebaseAuth.sendPasswordResetEmail(email)).thenReturn(mockTask)
        `when`(mockTask.isSuccessful).thenReturn(false)

        // Call the method to be tested
        forgotPasswordActivity.resetPassword(email)

        // Verify that FirebaseAuth sendPasswordResetEmail method is called
        verify(mockFirebaseAuth).sendPasswordResetEmail(email)
    }
}
