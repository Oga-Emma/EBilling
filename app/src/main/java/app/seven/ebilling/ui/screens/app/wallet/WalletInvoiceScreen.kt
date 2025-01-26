package app.seven.ebilling.ui.screens.app.wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.seven.ebilling.domain.models.Wallet
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun WalletInvoiceScreen(modifier: Modifier = Modifier, wallet: Wallet) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        WalletBalanceArea(
            modifier = Modifier,
            wallet = wallet
        )

        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = "TRANSACTIONS",
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Filled.Tune,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No transactions", textAlign = TextAlign.Center)
            Text(
                "Credit and debit transactions will appear here",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

//
//        item {
//            WalletBalanceArea(
//                modifier = Modifier,
//                wallet = wallet
//            )
//        }
//        item {
//            Spacer(modifier = Modifier.height(16.dp))
//        }

//        item {
//            Row(
//                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = modifier.weight(1f),
//                    text = "TRANSACTIONS",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 14.sp
//                )
//                Icon(
//                    imageVector = Icons.Filled.Tune,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.primaryContainer
//                )
//            }
//            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
//        }

//        items(4) {
//                InvoiceListItem(
//                    onItemSelected = {},
//                    invoice = it
//                )
//        }


    }
}

@Composable
fun WalletBalanceArea(modifier: Modifier = Modifier, wallet: Wallet) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray.copy(alpha = 0.2f)
            ),
            border = BorderStroke(width = 1.dp, color = Color.Gray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "0", fontWeight = FontWeight.Bold, fontSize = 40.sp)
                Text(text = "Available Balance", color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            BalanceArea(
                modifier = Modifier.weight(1f),
                amount = 0,
                label = "Credits",
                backgroundColor = Color.Blue
            )
            Spacer(modifier = Modifier.width(12.dp))
            BalanceArea(
                modifier = Modifier.weight(1f),
                amount = 0,
                label = "Debits",
                backgroundColor = Color.Red
            )
        }
    }
}

@Composable
fun BalanceArea(modifier: Modifier = Modifier, amount: Int, label: String, backgroundColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.2f)
        ),
        border = BorderStroke(width = 1.dp, color = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "$amount", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = Color.Gray)
        }
    }
}


@Preview
@Composable
private fun WalletInvoiceScreen() {
    EBillingTheme {
        Scaffold { it ->
            WalletInvoiceScreen(
                modifier = Modifier.padding(it),
                wallet = Wallet(),
            )
        }
    }
}