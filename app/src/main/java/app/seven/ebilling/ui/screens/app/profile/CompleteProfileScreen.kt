package app.seven.ebilling.ui.screens.app.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.AppViewModel

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("Complete Account Setup", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Please enter your details bellow to complete your account setup.")
        Spacer(modifier = Modifier.height(24.dp))
        EditProfileForm(
            userInfo = appViewModel.firebaseUser ?: TestUser(),
            savingChanges = appViewModel.savingUser,
            onSaveChanges = { firstName, lastName ->
                appViewModel.completeAccount(firstName, lastName)
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                appViewModel.logout()
            }
        ) {
            Text("LOGOUT")
        }
    }
}

@Preview
@Composable
private fun CompleteProfileScreenPreview() {
    EBillingTheme {
        Scaffold { padding ->
            CompleteProfileScreen(
                modifier = Modifier.padding(padding)
            )
        }
    }
}
