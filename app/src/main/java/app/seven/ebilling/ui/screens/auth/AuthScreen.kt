package app.seven.ebilling.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.seven.ebilling.domain.utils.validatePhoneNumber
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val authState = authViewModel.authState.collectAsState()

    AuthView(
        modifier = modifier,
        state = authState.value,
        sendingOtp = authViewModel.sendingOTPState,
        onRetry = {
            authViewModel.authenticateUser()
        },
        onSendOTP = {
            authViewModel.requestOTP(it)
        }
    )
}

@Composable
fun AuthView(
    modifier: Modifier = Modifier,
    state: AuthState,
    onRetry: () -> Unit,
    onSendOTP: (String) -> Unit,
    sendingOtp: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize()
    ) {
        Text(
            "E-BILLING",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            ErrorArea(onRetry = onRetry)
        } else {
            Text("Send and receive payments faster than ever before with our hassle-free invoicing system.")
            Spacer(modifier = Modifier.height(48.dp))
            PhoneNumberArea(onSendOTP = onSendOTP, sendingOtp = sendingOtp)
        }
    }
}

@Composable
fun PhoneNumberArea(
    modifier: Modifier = Modifier,
    onSendOTP: (String) -> Unit,
    sendingOtp: Boolean
) {
    var phone by remember { mutableStateOf("") }
    var phoneValidationError by remember { mutableStateOf("") }

    Column(
        modifier
    ) {
        Text("Enter your phone number to get started.")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = {
                phone = it
                phoneValidationError = ""
            },
            label = { Text("Phone Number") },
        )
        if (phoneValidationError.isNotBlank())
            Text(phoneValidationError, color = Color.Red)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !sendingOtp,
            shape = RoundedCornerShape(4.dp),
            onClick = {
                phoneValidationError = validatePhoneNumber(phone)

                if (phoneValidationError.isBlank()) {
                    onSendOTP(phone)
                }
            }
        ) {
            if (sendingOtp) CircularProgressIndicator() else Text("Continue".uppercase())
        }
    }
}

@Composable
fun ErrorArea(modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column {
        Text("Error loading data, please check your network and try again")
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            modifier = Modifier
                .height(52.dp),
            onClick = { onRetry() }
        ) {
            Text("Retry".uppercase())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview1() {
    EBillingTheme {
        AuthView(
            state = AuthState(isLoading = false, error = null),
            onRetry = {},
            onSendOTP = {

            },
            sendingOtp = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview2() {
    EBillingTheme {
        AuthView(
            state = AuthState(isLoading = true, error = "Network error"),
            onRetry = {},
            onSendOTP = {

            },
            sendingOtp = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview3() {
    EBillingTheme {
        AuthView(
            state = AuthState(isLoading = false, error = "Network error"),
            onRetry = {},
            onSendOTP = {

            },
            sendingOtp = false
        )
    }
}