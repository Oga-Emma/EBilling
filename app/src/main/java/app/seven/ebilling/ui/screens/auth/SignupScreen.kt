package app.seven.ebilling.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun SignupScreen(modifier: Modifier = Modifier, signup: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("Create a new account", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Please enter your details below to get started")
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { phone = it },
            label = { Text("First Name") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Last Name") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Confirm Password") }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("By clicking signup, you've accept the terms of use and privacy policy")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = { signup() }
        ) {
            Text("Create Account")
        }
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    EBillingTheme {
        Scaffold { padding ->
            SignupScreen(
                modifier = Modifier.padding(padding),
                signup = {})
        }
    }
}
