package app.seven.ebilling.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.seven.ebilling.ui.core.components.ETextField
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun VerifyPhoneScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var otp by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("Verify phone number", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Please verify your phone number with the code sent to ${authViewModel.phoneNumberState}.")
        Spacer(modifier = Modifier.height(48.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = otp,
            onValueChange = { otp = it },
            label = "OTP",
            keyboardType = KeyboardType.Number,
            errorMessage = ""
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !authViewModel.verifyingOTPState,
            shape = RoundedCornerShape(4.dp),
            onClick = { authViewModel.verifyOTP(otp) }
        ) {
            if (authViewModel.verifyingOTPState) CircularProgressIndicator() else Text("Verify Phone Number")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Didn't get a code?")
            TextButton(
                onClick = {
                    authViewModel.requestOTP(authViewModel.phoneNumberState)
                },
                enabled = !(authViewModel.verifyingOTPState || authViewModel.sendingOTPState)
            ) {
                if (authViewModel.sendingOTPState) CircularProgressIndicator()
                else Text("Request a new one here")
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    EBillingTheme {
        Scaffold { padding ->
            VerifyPhoneScreen(
                modifier = Modifier.padding(padding)
            )
        }
    }
}
