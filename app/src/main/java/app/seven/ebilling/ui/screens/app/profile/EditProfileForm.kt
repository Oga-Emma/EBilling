package app.seven.ebilling.ui.screens.app.profile

import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.ui.core.components.ETextField
import app.seven.ebilling.ui.core.theme.EBillingTheme
import com.google.firebase.auth.UserInfo


@Composable
fun EditProfileForm(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    savingChanges: Boolean,
    onSaveChanges: (firstName: String, lastName: String) -> Unit,
) {
    val nameSplit = (userInfo.displayName ?: "").split(" ")

    var firstName by remember { mutableStateOf(nameSplit.firstOrNull() ?: "") }
    var firstNameError by remember { mutableStateOf(false) }

    var lastName by remember { mutableStateOf(if (nameSplit.size > 1) nameSplit[1] else "") }
    var lastNameError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = firstName,
            onValueChange = {
                firstName = it
                firstNameError = false
            },
            label = "First name",
            hasError = firstNameError,
            errorMessage = "Please enter a valid first name"
        )
        Spacer(modifier = Modifier.height(16.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = lastName,
            onValueChange = {
                lastName = it
                lastNameError = false
            },
            label = "Last name",
            hasError = lastNameError,
            errorMessage = "Enter a valid last name"
        )

        Spacer(modifier = Modifier.height(16.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = userInfo.phoneNumber ?: "",
            enabled = false,
            onValueChange = { },
            errorMessage = "",
            label = "Phone Number",
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                if (firstName.isBlank() || lastName.isBlank()) {
                    if (firstName.isBlank()) {
                        firstNameError = true
                    }

                    if (lastName.isBlank()) {
                        lastNameError = true
                    }
                } else {
                    onSaveChanges(firstName, lastName)
                }
            }
        ) {
            if (savingChanges) CircularProgressIndicator() else Text("SAVE CHANGES")
        }
    }
}

fun isValidEmail(target: CharSequence): Boolean {
    return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches())
}

@Preview
@Composable
private fun EditProfileFormPreview() {
    EBillingTheme {
        EditProfileForm(
            userInfo = TestUser(),
            onSaveChanges = { email, name -> },
            savingChanges = false,
        )
    }
}

class TestUser : UserInfo {
    override fun getPhotoUrl(): Uri? {
        return null
    }

    override fun getDisplayName(): String? {
        return null
    }

    override fun getEmail(): String? {
        return null
    }

    override fun getPhoneNumber(): String {
        return ""
    }

    override fun getProviderId(): String {
        return ""
    }

    override fun getUid(): String {
        return ""
    }

    override fun isEmailVerified(): Boolean {
        return true
    }
}
