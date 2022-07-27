package com.swaptech.meet.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.swaptech.meet.di.presentation.viewmodel.ViewModelFactory
import com.swaptech.meet.presentation.MeetPointApplication
import com.swaptech.meet.presentation.screen.root.RootScreen
import com.swaptech.meet.presentation.theme.MeetTheme
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import org.osmdroid.config.Configuration
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MeetPointApplication).androidInjector().inject(this)
        Configuration.getInstance()
            .load(applicationContext, PreferenceManager.getDefaultSharedPreferences(this))
        setContent {
            MeetTheme {
                RootScreen(
                    viewModelFactory = viewModelFactory,
                    viewModel = viewModel(
                        modelClass = LocalUserViewModel::class.java,
                        factory = viewModelFactory
                    )
                )
            }
        }
    }
}
