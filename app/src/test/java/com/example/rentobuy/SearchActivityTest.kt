import android.content.Intent
import com.example.rentobuy.modules.home.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.lang.reflect.Method

@RunWith(MockitoJUnitRunner::class)
class SearchActivityTest {

    @Mock
    private lateinit var mockIntent: Intent

    @Mock
    private lateinit var mockBottomNavigationView: BottomNavigationView

    private lateinit var searchActivity: SearchActivity

    @Before
    fun setUp() {
        searchActivity = SearchActivity()
        searchActivity.bottomNavigationView = mockBottomNavigationView
    }

    @Test
    fun testRedirectToProfile() {
        val method: Method = SearchActivity::class.java.getDeclaredMethod("redirectToProfile")
        method.isAccessible = true

        method.invoke(searchActivity)

        // Verify that the intent starts UserProfileActivity
        verify(mockBottomNavigationView.context).startActivity(any(Intent::class.java))
    }

    @Test
    fun testRedirectToWishlist() {
        val method: Method = SearchActivity::class.java.getDeclaredMethod("redirectToWishlist")
        method.isAccessible = true

        method.invoke(searchActivity)

        // Verify that the intent starts WishListActivity
        verify(mockBottomNavigationView.context).startActivity(any(Intent::class.java))
    }

    @Test
    fun testRedirectToCart() {
        val method: Method = SearchActivity::class.java.getDeclaredMethod("redirectToCart")
        method.isAccessible = true

        method.invoke(searchActivity)

        // Verify that the intent starts CartActivity
        verify(mockBottomNavigationView.context).startActivity(any(Intent::class.java))
    }

    @Test
    fun testRedirectToHome() {
        val method: Method = SearchActivity::class.java.getDeclaredMethod("redirectToHome")
        method.isAccessible = true

        method.invoke(searchActivity)

        // Verify that the intent starts HomePageActivity
        verify(mockBottomNavigationView.context).startActivity(any(Intent::class.java))
    }
}
