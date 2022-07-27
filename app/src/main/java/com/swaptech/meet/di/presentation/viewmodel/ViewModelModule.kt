package com.swaptech.meet.di.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.swaptech.meet.presentation.screen.auth.AuthUserViewModel
import com.swaptech.meet.presentation.screen.auth.signup.SignUpViewModel
import com.swaptech.meet.presentation.screen.home.meetpoint.MeetPointScreenViewModel
import com.swaptech.meet.presentation.screen.home.user.UserScreenViewModel
import com.swaptech.meet.presentation.viewmodel.LocalUserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(MeetPointScreenViewModel::class)
    fun bindMeetPointScreenViewModel(viewModel: MeetPointScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(LocalUserViewModel::class)
    fun bindUserViewModel(viewModel: LocalUserViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(AuthUserViewModel::class)
    fun bindAuthUserViewModel(viewModel: AuthUserViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(UserScreenViewModel::class)
    fun bindMoreScreenViewModel(viewModel: UserScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @CreateWithViewModelFactory(SignUpViewModel::class)
    fun bindSignUpViewModel(viewModel: SignUpViewModel): ViewModel
}
