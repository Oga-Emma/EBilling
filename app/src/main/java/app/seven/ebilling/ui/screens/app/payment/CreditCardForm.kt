package app.seven.ebilling.ui.screens.app.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.ui.core.components.ETextField
import app.seven.ebilling.ui.core.theme.EBillingTheme
import com.steliospapamichail.creditcardmasker.viewtransformations.CardNumberMask
import com.steliospapamichail.creditcardmasker.viewtransformations.ExpirationDateMask

@Composable
fun CreditCardForm(modifier: Modifier = Modifier) {
    var firstName by remember { mutableStateOf( "") }
    var firstNameError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = firstName,
            visualTransformation = CardNumberMask("-"),
            onValueChange = {
                firstName = it
                firstNameError = false
            },
            label = "Card Number",
            hasError = firstNameError,
            errorMessage = "Enter a valid card number"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            ETextField(
                modifier = Modifier.weight(1f),
                textFieldModifier = Modifier.fillMaxWidth(),
                visualTransformation = ExpirationDateMask(),
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = false
                },
                label = "Expiry",
                hasError = firstNameError,
                errorMessage = "Enter expiry date"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ETextField(
                modifier = Modifier.weight(1f),
                textFieldModifier = Modifier.fillMaxWidth(),
                visualTransformation = ExpirationDateMask(),
                value = firstName,
                onValueChange = {
                    firstName = it
                    firstNameError = false
                },
                label = "CVC",
                hasError = firstNameError,
                errorMessage = "Enter valid cvc"
            )
        }
    }
}

@Preview
@Composable
private fun CreditCardFormPreview() {
    EBillingTheme {
        Scaffold { it ->
            CreditCardForm(
                modifier = Modifier.padding(it)
            )
        }
    }
}
