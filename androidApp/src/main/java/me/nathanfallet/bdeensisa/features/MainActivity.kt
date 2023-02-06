package me.nathanfallet.bdeensisa.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import me.nathanfallet.bdeensisa.R
import me.nathanfallet.bdeensisa.features.account.AccountView
import me.nathanfallet.bdeensisa.features.feed.FeedView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BDEApp()
        }
    }
}

enum class NavigationItem(var route: String, var icon: Int, var title: String) {

    FEED("feed", R.drawable.ic_baseline_newspaper_24, "Actualité"),
    ACCOUNT("account", R.drawable.ic_baseline_person_24, "Mon compte"),
    //MANAGE("manage", R.drawable.ic_baseline_app_settings_alt_24, "Gestion")

}

@Composable
fun BDEApp() {
    BDETheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val viewModel: MainViewModel = viewModel<MainViewModel>().load()

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val currentRoute = navBackStackEntry?.destination?.route
                    NavigationItem.values().forEach { item ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painterResource(id = item.icon),
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(text = item.title) },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(0.4f),
                            alwaysShowLabel = true,
                            selected = currentRoute?.startsWith(item.route) ?: false,
                            onClick = {
                                navController.navigate(item.route) {
                                    navController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = "feed") {
                composable("feed") {
                    FeedView(
                        modifier = Modifier.padding(padding),
                        navigate = navController::navigate
                    )
                }
                composable("account") {
                    AccountView(
                        modifier = Modifier.padding(padding),
                        mainViewModel = viewModel
                    )
                }
                composable(
                    "account/code",
                    arguments = listOf(
                        navArgument("code") { type = NavType.StringType }
                    ),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "bdeensisa://authorize?{code}"
                        }
                    )
                ) { backStackEntry ->
                    AccountView(
                        modifier = Modifier.padding(padding),
                        code = backStackEntry.arguments?.getString("code"),
                        mainViewModel = viewModel
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    BDEApp()
}