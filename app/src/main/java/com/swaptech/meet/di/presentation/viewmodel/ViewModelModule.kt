package com.swaptech.meet.di.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointScreenViewModel
import com.swaptech.meet.presentation.screen.home.more.feedback.FeedbackScreenViewModel
import com.swaptech.meet.presentation.screen.home.user_screen.update.UserUpdateScreenViewModel
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import com.swaptech.meet.presentation.viewmodel.RemoteUserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(LocalUserViewModel::class)
    fun bindLocalUserViewModel(viewModel: LocalUserViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(RemoteUserViewModel::class)
    fun bindRemoteUserViewModel(viewModel: RemoteUserViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(MeetPointScreenViewModel::class)
    fun bindMeetPointScreenViewModel(viewModel: MeetPointScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(AuthUserViewModel::class)
    fun bindAuthUserViewModel(viewModel: AuthUserViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(UserUpdateScreenViewModel::class)
    fun bindUserUpdateScreenViewModel(viewModel: UserUpdateScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(FeedbackScreenViewModel::class)
    fun bindFeedbackScreenViewModel(viewModel: FeedbackScreenViewModel): ViewModel
}
