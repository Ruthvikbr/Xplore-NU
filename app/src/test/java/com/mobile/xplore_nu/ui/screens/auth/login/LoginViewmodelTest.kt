import com.mobile.domain.models.AuthenticationResponse
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.User
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.screens.auth.login.LoginViewModel
import com.mobile.xplore_nu.ui.uistates.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel
    private val userRepository = mock<UserRepository>() // Create mock UserRepository
    private val loginUserUseCase = LoginUserUseCase(userRepository) // Pass it to LoginUserUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        loginViewModel = LoginViewModel(loginUserUseCase)
    }

    @Test
    fun `loginUser success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val loginState = LoginState(email = email, password = password, isEmailValid = true, isPasswordValid = true)
        loginViewModel.updateEmailForLogin(email)
        loginViewModel.updatePasswordForLogin(password)

        val expectedResponse = Resource.success(
            AuthenticationResponse(
                "Login successful",
                "token",
                "refreshToken",
                User("1", "John", "Doe", email, "user")
            )
        )
        `when`(userRepository.loginUser(LoginRequest(email, password))).thenReturn(expectedResponse)

        // When
        loginViewModel.loginUser(loginState)

        // Then
        val actualStatus = loginViewModel.loginStatus.take(2).first {
            when (it.status) {
                Status.SUCCESS -> true
                Status.ERROR -> true
                Status.LOADING -> false
            }
        }
        assertEquals(expectedResponse.status, actualStatus.status)
        assertEquals(expectedResponse.data, actualStatus.data)
    }

    @Test
    fun `loginUser failure`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val loginState = LoginState(email = email, password = password, isEmailValid = true, isPasswordValid = true)
        loginViewModel.updateEmailForLogin(email)
        loginViewModel.updatePasswordForLogin(password)

        val expectedResponse = Resource.error<AuthenticationResponse>("Invalid credentials", null)
        `when`(userRepository.loginUser(LoginRequest(email, password))).thenReturn(expectedResponse)

        // When
        loginViewModel.loginUser(loginState)

        // Then
        val actualStatus = loginViewModel.loginStatus.take(2).first {
            when (it.status) {
                Status.SUCCESS -> true
                Status.ERROR -> true
                Status.LOADING -> false
            }
        }
        assertEquals(expectedResponse.status, actualStatus.status)
        assertEquals(expectedResponse.message, actualStatus.message)
    }
}