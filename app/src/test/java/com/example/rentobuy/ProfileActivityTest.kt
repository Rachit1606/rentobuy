package com.example.rentobuy
import android.content.Intent
import com.example.rentobuy.modules.auth.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.Task
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.stubbing.OngoingStubbing

class ProfileActivityTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    private lateinit var profileActivity: ProfileActivity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        profileActivity = ProfileActivity()
        profileActivity.firebaseAuth = mockFirebaseAuth
        profileActivity.firestore = mockFirestore
    }

    @Test
    fun updateUserProfile_success() {
        // Mock current user and email
        val currentUser = mock(FirebaseAuth::class.java)
        `when`(mockFirebaseAuth.currentUser).thenReturn(currentUser)

        // Mock updated user data
        val updatedUserData = hashMapOf(
            "firstName" to "John",
            "lastName" to "Doe",
            "mobile" to 1234567890L,
            "gender" to "Male"
        )

        // Mock update task
        val mockUpdateTask: Task<Void> = mock(Task::class.java) as Task<Void>
        `when`(mockFirestore.collection("Users").document("test@example.com").update(updatedUserData as Map<String, Any>))
            .thenReturn(mockUpdateTask)

        // Call the method to be tested
        profileActivity.updateUserProfile()

        // Verify that Firestore update method is called
        verify(mockFirestore.collection("Users").document("test@example.com")).update(
            updatedUserData as Map<String, Any>
        )
    }

    @Test
    fun logout_success() {
        // Call the method to be tested
        profileActivity.logout()

        // Verify that FirebaseAuth signOut method is called
        verify(mockFirebaseAuth).signOut()
    }
}

private fun <T> OngoingStubbing<T>.thenReturn(currentUser: FirebaseAuth?) {
    TODO("Not yet implemented")
}
