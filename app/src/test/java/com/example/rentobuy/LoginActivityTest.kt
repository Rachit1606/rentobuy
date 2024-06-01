import com.example.rentobuy.modules.auth.LoginActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

class LoginActivityTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    private lateinit var loginActivity: LoginActivity

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginActivity = LoginActivity()
        loginActivity.firebaseAuth = mockFirebaseAuth
    }

    @Test
    fun login_successful() {
        val email = "test@example.com"
        val password = "password123"
        val expectedResult = mockTask.apply { `when`(isSuccessful).thenReturn(true) }
        `when`(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(expectedResult)

        val result = loginActivity.login(email, password)

        assertEquals(expectedResult, result)
    }

    @Test
    fun login_failed() {
        val email = "test@example.com"
        val password = "password123"
        val expectedResult = mockTask.apply { `when`(isSuccessful).thenReturn(false) }
        `when`(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(expectedResult)

        val result = loginActivity.login(email, password)

        assertEquals(expectedResult, result)
    }
}
