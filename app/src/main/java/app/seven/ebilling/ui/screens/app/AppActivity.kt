package app.seven.ebilling.ui.screens.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.outlined.Drafts
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.seven.ebilling.domain.utils.titlecase
import app.seven.ebilling.ui.core.components.ObserveAsEvents
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.home.HomeScreen
import app.seven.ebilling.ui.screens.app.invoice.CreateInvoiceScreen
import app.seven.ebilling.ui.screens.app.invoice.DraftInvoiceScreen
import app.seven.ebilling.ui.screens.app.invoice.InvoiceDetailsScreen
import app.seven.ebilling.ui.screens.app.payment.CreditCardScreen
import app.seven.ebilling.ui.screens.app.profile.CompleteProfileScreen
import app.seven.ebilling.ui.screens.app.profile.EditProfileScreen
import app.seven.ebilling.ui.screens.app.profile.ProfileScreen
import app.seven.ebilling.ui.screens.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EBillingTheme {
                AppView()
            }
        }
    }
}

sealed class AppScreens(open val route: String, open val title: String) {
    sealed class BottomNavScreen(
        override val route: String,
        override val title: String,
        val icon: ImageVector
    ) :
        AppScreens(route = "create-invoice", title = "Create Invoice")

    object Home :
        BottomNavScreen(route = "home", title = "Home", icon = Icons.Default.Dashboard)

    object Draft :
        BottomNavScreen(route = "draft", title = "Draft", icon = Icons.Outlined.Drafts)

    object Profile :
        BottomNavScreen(route = "profile", title = "Profile", icon = Icons.Outlined.Person)

    object Loading : AppScreens(route = "loading", title = "")
    object AccountSetup : AppScreens(route = "account-setup", title = "Account Setup")
    object EditProfile : AppScreens(route = "edit-profile", title = "Edit Profile")
    object ManageCard : AppScreens(route = "manage-card", title = "Manage Card")
    object CreateInvoice : AppScreens(route = "create-invoice", title = "Create Invoice")
    object EditInvoice : AppScreens(route = "edit-invoice", title = "Edit Invoice")
    object InvoiceDetails : AppScreens(route = "invoice-details", title = "Invoice Details")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                currentScreen
                    .split("-")
                    .joinToString(" ")
                    .titlecase()
            )
        },
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
fun AppView(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    bottomNavigationItems: List<AppScreens.BottomNavScreen> = listOf(
        AppScreens.Home,
        AppScreens.Draft,
        AppScreens.Profile,
    )
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isNavbarScreen = currentRoute in bottomNavigationItems.map { it.route }
    val isLoadingScreen =
        currentRoute in listOf(AppScreens.Loading.route, AppScreens.AccountSetup.route)

    val appViewModel: AppViewModel = hiltViewModel()

    val context = LocalContext.current
    ObserveAsEvents(events = appViewModel.events) { event ->
        when (event) {
            AppViewModel.AppUIEvent.Loading ->
                navController.navigate(AppScreens.Loading.route)

            AppViewModel.AppUIEvent.CompleteProfile ->
                navController.navigate(AppScreens.AccountSetup.route)

            AppViewModel.AppUIEvent.CreateInvoice ->
                navController.navigate(AppScreens.CreateInvoice.route)

            AppViewModel.AppUIEvent.Home ->
                navController.navigate(AppScreens.Home.route)

            AppViewModel.AppUIEvent.Logout -> {
                context.startActivity(Intent(context, AuthActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                })
            }

            is AppViewModel.AppUIEvent.ViewInvoice -> TODO()
            is AppViewModel.AppUIEvent.Message -> Toast.makeText(
                context,
                event.message,
                Toast.LENGTH_LONG
            ).show()

            AppViewModel.AppUIEvent.Back ->
                navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            MainAppBar(
                currentScreen = navBackStackEntry?.destination?.route ?: AppScreens.Loading.route,
                canNavigateBack = !isLoadingScreen && !isNavbarScreen,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            if (isNavbarScreen) {
                NavigationBar {
                    bottomNavigationItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == AppScreens.Home.route) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate(AppScreens.CreateInvoice.route)
                    }
                ) {
                    Icon(Icons.Default.AddCircle, "Add")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Invoice")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Loading.route,
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = AppScreens.Loading.route) {
                LaunchedEffect(Unit) {
                    appViewModel.loadUser() // Load data when the screen is composed
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            composable(route = AppScreens.AccountSetup.route) {
                CompleteProfileScreen(
                    appViewModel = appViewModel
                )
            }
            composable(route = AppScreens.Home.route) {
                HomeScreen(onItemSelected = { navController.navigate(AppScreens.InvoiceDetails.route) })
            }
            composable(route = AppScreens.Draft.route) {
                DraftInvoiceScreen()
            }
            composable(route = AppScreens.EditProfile.route) {
                EditProfileScreen(
                    appViewModel = appViewModel
                )
            }
            composable(route = AppScreens.ManageCard.route) {
                CreditCardScreen()
            }
            composable(route = AppScreens.Profile.route) {
                ProfileScreen(
                    appViewModel = appViewModel,
                    onEditProfile = {
                        navController.navigate(AppScreens.EditProfile.route)
                    },
                    onManageCard = {
                        navController.navigate(AppScreens.ManageCard.route)
                    },
                    onLogout = appViewModel::logout
                )
            }
            composable(route = AppScreens.CreateInvoice.route) {
                CreateInvoiceScreen(
                    showMessage = appViewModel::showMessage,
                    saveChanges = appViewModel::saveInvoice,
                    savingChanges = appViewModel.savingInvoice
                )
            }
            composable(route = AppScreens.EditInvoice.route) {
                Box { }
            }
            composable(route = AppScreens.InvoiceDetails.route) {
                InvoiceDetailsScreen()
            }
        }
    }
}
//
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    BottomNavigation {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//
//        BottomNavItem.values().forEach { item ->
//            BottomNavigationItem(
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationId)
//                        launchSingleTop = true
//                    }
//                },
//                icon = { Icon(item.icon, contentDescription = null) },
//                label = { Text(item.label) }
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EBillingTheme {
        AppView()
    }
}