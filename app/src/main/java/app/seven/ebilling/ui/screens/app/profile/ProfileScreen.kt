package app.seven.ebilling.ui.screens.app.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EditRoad
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.AppViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),
    onEditProfile: () -> Unit,
    onManageCard: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier.size(120.dp),
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "",
            tint = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            appViewModel.firebaseUser?.displayName ?: "",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            ProfileButton(
                label = "Edit Profile",
                leading = Icons.Default.EditRoad,
                onClick = { onEditProfile() }
            )
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
            ProfileButton(
                label = "Manage Card",
                leading = Icons.Default.CreditCard,
                onClick = { onManageCard() }
            )
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
            ProfileButton(
                label = "Logout",
                iconColor = Color.Red,
                textColor = Color.Red,
                leading = Icons.AutoMirrored.Default.Logout,
                trailing = null,
                onClick = { onLogout() }
            )
        }
    }
}

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    label: String,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.Gray,
    leading: ImageVector,
    trailing: ImageVector? = Icons.Default.ChevronRight,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onClick() }.padding(24.dp)
    ) {
        Icon(leading, "", tint = iconColor)
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            fontSize = 16.sp,
            color = textColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        if (trailing != null)
            Icon(trailing, "", tint = iconColor)
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    EBillingTheme {
        Scaffold { it ->
            ProfileScreen(
                modifier = Modifier.padding(it),
                onEditProfile = {},
                onManageCard = {  },
                onLogout = {}
            )
        }
    }
}