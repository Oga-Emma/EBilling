package app.seven.ebilling.ui.screens.app.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.domain.utils.validatePhoneNumber
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun CreditCardScreen(modifier: Modifier = Modifier) {
    Column {
        CreditCardForm()
        Button(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
//                phoneValidationError = validatePhoneNumber(phone)
//
//                if (phoneValidationError.isBlank()) {
//                    onSendOTP(phone)
//                }
            }
        ) {
            if (false) CircularProgressIndicator() else Text("Continue".uppercase())
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun CreditCardScreen() {
    EBillingTheme {
        Scaffold { it ->
            CreditCardScreen(
                modifier = Modifier.padding(it)
            )
        }
    }
}