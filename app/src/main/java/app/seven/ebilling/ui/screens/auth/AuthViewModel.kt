package app.seven.ebilling.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seven.ebilling.domain.utils.addCountryCode
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var storedVerificationId: String? = null
    lateinit var auth: FirebaseAuth

    var phoneNumberState by mutableStateOf("")
    var sendingOTPState by mutableStateOf(false)
    var verifyingOTPState by mutableStateOf(false)

    private val _authState = MutableStateFlow<AuthState>(AuthState(isLoading = true))
    val authState: StateFlow<AuthState> = _authState

    private val _events = Channel<AuthScreenEvent>()
    val events = _events.receiveAsFlow()

//    fun onAction(action: CoinListAction) {
//        when (action) {
//            is CoinListAction.OnCoinClick -> {
//                selectCoin(action.coinUi)
//            }
//        }
//    }

    fun authenticateUser() {
        auth = FirebaseAuth.getInstance()
        viewModelScope.launch {
            _authState.value = authState.value.copy(isLoading = true, error = null)

            if (auth.currentUser == null) {
                _authState.value = authState.value.copy(isLoading = false, error = null)
            } else {
                _events.send(AuthScreenEvent.Home)
            }
        }
    }

    fun requestOTP(phoneNumber: String) {
        sendingOTPState = true
        phoneNumberState = addCountryCode(phoneNumber)

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumberState) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verification without
                    //     user action.
//                        Log.d(TAG, "onVerificationCompleted:$credential")
//                        signInWithPhoneAuthCredential(credential)
                    sendUiEvent(AuthScreenEvent.Message("Phone number verified."))
                    sendUiEvent(AuthScreenEvent.Home)
                    sendingOTPState = false
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
//                        Log.w(TAG, "onVerificationFailed", e)

                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                        // reCAPTCHA verification attempted with null Activity
                    }

                    sendingOTPState = false
                    sendUiEvent(AuthScreenEvent.Message("Error verifying phone, please try again."))
                    // Show a message and update the UI
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.
//                        Log.d(TAG, "onCodeSent:$verificationId")

                    // Save verification ID and resending token so we can use them later
                    storedVerificationId = verificationId
                    resendToken = token

                    sendingOTPState = false
                    sendUiEvent(AuthScreenEvent.VerifyPhone(phoneNumber = phoneNumber))
                }
            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(otp: String) {
        verifyingOTPState = true
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                verifyingOTPState = false
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    sendUiEvent(AuthScreenEvent.Home)
                    phoneNumberState = ""
                    verifyingOTPState = false
                } else {
                    // Sign in failed, display a message and update the UI
//                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        sendUiEvent(AuthScreenEvent.Message("Invalid verification code."))
                    }else {
                        sendUiEvent(AuthScreenEvent.Message("Error verifying code, please try again."))
                    }

                    // Update UI
                    verifyingOTPState = false
                }
            }
    }

    fun sendUiEvent(event: AuthScreenEvent) = viewModelScope.launch {
        _events.send(event)
    }
}

data class AuthState(
    val isLoading: Boolean,
    val error: String? = null,
)

sealed interface AuthScreenEvent {
    data class VerifyPhone(val phoneNumber: String) : AuthScreenEvent
    data class Message(val message: String) : AuthScreenEvent
    data object Home : AuthScreenEvent
}
