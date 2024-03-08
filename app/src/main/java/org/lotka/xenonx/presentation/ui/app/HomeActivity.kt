package org.lotka.xenonx.presentation.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import org.lotka.xenonx.di.DataStoreManager
import org.lotka.xenonx.presentation.util.CustomUpdateManager
import org.lotka.xenonx.util.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.lotka.xenonx.presentation.ui.screens.plp.PlpViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private val viewModel by viewModels<MainViewModel>()
    private val plpViewModel by viewModels<PlpViewModel>()




    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var dataStoreManager: DataStoreManager
    @Inject
    lateinit var customUpdateManager: CustomUpdateManager


    val snackbarHostState = androidx.compose.material.SnackbarHostState()
    lateinit var keyboardController: SoftwareKeyboardController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()
            val keyboardController = LocalSoftwareKeyboardController.current
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                if (keyboardController != null) {
                    HomeApp(
                        activity = this@HomeActivity,
                        viewModel = viewModel,
                        navController = navController,
                        settingsDataStore = settingsDataStore,
                        plpviewModel = plpViewModel,
                        onNavigateToRecipeDetailScreen = { },
                        isDarkTheme = false,
                        onToggleTheme = { },
                        snackbarHostState = snackbarHostState,
                        keyboardController = keyboardController

                    )
                }
            }
        }


        lifecycleScope.launch {
            customUpdateManager.updateStatus.collectLatest{
                Timber.tag("updateLogger").d("updateStatus in main actvity: %s", it.toString())
                when(it){
                    CustomUpdateManager.UpdateType.FORCE_UPDATE -> {

                        val intent =  Intent(this@HomeActivity , UpdateActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("type", "FORCE")
                        startActivity(intent)
                        finishAffinity()
                    }
                    CustomUpdateManager.UpdateType.OPTIONAL_UPDATE -> {
                        val postponedDate = dataStoreManager.postponeUpdateFlow.first()
                        if(postponedDate != null){
                            if (dataStoreManager.postponeUpdateFlow.
                                first()!! <= System.currentTimeMillis()) {
                                val intent =  Intent(this@HomeActivity , UpdateActivity::class.java)
                                intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("type", "OPTIONAL")
                                startActivity(intent)
                                finishAffinity()
                            }
                        }
                    }
                    else -> {}
                }
            }
        }


    }


}
