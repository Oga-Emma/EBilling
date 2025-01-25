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
import androidx.hilt.navigation.compose.hiltViewModel
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.AppViewModel

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),

) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        EditProfileForm(
            userInfo = appViewModel.firebaseUser ?: TestUser(),
            savingChanges = appViewModel.savingUser,
            onSaveChanges = { firstName, lastName ->
                appViewModel.saveProfile(firstName, lastName)
            }
        )
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    EBillingTheme {
        Scaffold { padding ->
            EditProfileScreen(
                modifier = Modifier.padding(padding)
            )
        }
    }
}
