package app.seven.ebilling.ui.screens.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.seven.ebilling.ui.core.components.ObserveAsEvents
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.AppActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EBillingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Auth(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * enum values that represent the screens in the app
 */
enum class AuthScreens(val title: String) {
    Auth(title = "Welcome"),
    OTP(title = "Otp"),
    Signup(title = "Signup"),
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthAppBar(
    currentScreen: AuthScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        }
    )
}

@Composable
fun Auth(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Get the name of the current screen
    val currentScreen = AuthScreens.valueOf(
        backStackEntry?.destination?.route ?: AuthScreens.Auth.name
    )

    val currentContext = LocalContext.current

    fun signup() = navController.navigate(AuthScreens.Signup.name)
    fun home() = currentContext.startActivity(Intent(currentContext, AppActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    })

    val authViewModel: AuthViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        authViewModel.authenticateUser() // Load data when the screen is composed
    }


//    val state by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(events = authViewModel.events) { event ->
        when (event) {
//            is CoinListEvent.Error -> {
//                Toast.makeText(
//                    context,
//                    event.error.toString(context),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
            is AuthScreenEvent.Home -> home()
            is AuthScreenEvent.VerifyPhone -> navController.navigate(AuthScreens.OTP.name)
            is AuthScreenEvent.Message -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            AuthAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AuthScreens.Auth.name,
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = AuthScreens.Auth.name) {
                AuthScreen(
                    authViewModel = authViewModel
                )
            }
            composable(route = AuthScreens.OTP.name) {
                VerifyPhoneScreen(
                    authViewModel = authViewModel
                )
            }
            composable(route = AuthScreens.Signup.name) {
                SignupScreen(signup = {
                    home()
                })
            }
        }
    }

}
