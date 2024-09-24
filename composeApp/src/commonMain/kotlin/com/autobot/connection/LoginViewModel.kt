package com.autobot.connection

import androidx.lifecycle.viewModelScope
import com.autobot.connection.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(private val authService: AuthService): BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState  = _uiState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _currentUserId = MutableStateFlow<User?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    val isButtonEnabled : StateFlow<Boolean> = combine(uiState){ states ->
        val state = states.first()
        state.email.isNotBlank() && state.password.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),false
    )


    fun onEmailChange(newValue: String){
        _uiState.value = _uiState.value.copy(email = newValue)
        if(newValue.isNotEmpty()){
            _emailError.value = false
        }

    }
    fun onPasswordChange(newValue: String){
        _uiState.value = _uiState.value.copy(password = newValue)
        if(newValue.isNotEmpty()){
            _passwordError.value = false
        }
    }
    fun onSignOut(){
        launchWithCatchingException {
            authService.signOut()
        }
    }

    init {
        launchWithCatchingException {
        authService.currentUser.collect{
            _currentUserId.value = it
        }
        }


    }

    fun onSignInClick(){
        if(uiState.value.email.isEmpty()){
            _emailError.value = true
        }
        if(uiState.value.password.isEmpty()){
            _passwordError.value = true
        }
        launchWithCatchingException {
            _isProcessing.value = true
          val result =   authService.createUser(_uiState.value.email,_uiState.value.password)
//            authService.authenticate(_uiState.value.email,_uiState.value.password)
            _isProcessing.value = false
        }
    }


}
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)