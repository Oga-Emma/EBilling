package app.seven.ebilling.ui.screens.app.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.seven.ebilling.ui.core.theme.EBillingTheme

@Composable
fun InvoiceDetailsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "RECIPIENT INFORMATION"
        )
        InvoiceReceiverInfo(modifier = Modifier.padding(vertical = 16.dp))
        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        InvoiceDateArea(modifier = Modifier.padding(vertical = 16.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "INVOICE ITEMS DETAILS"
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        InvoiceItemsDetails(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MoneySummaryArea()
        Spacer(modifier = Modifier.height(24.dp))
        ButtonArea()
    }
}

@Composable
fun ButtonArea(modifier: Modifier = Modifier) {
    Column {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = { }
        ) {
            Text("Make Full Payment")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = { }
        ) {
            Text("Pay Next Installment")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = { }
            ) {
                Text("Save as Draft")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = { }
            ) {
                Text("Send Invoice")
            }
        }
    }
}

@Composable
fun MoneySummaryArea(modifier: Modifier = Modifier) {
    Column {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "TOTAL SUMMARY",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        SummaryItem(label = "Subtotal", value = "NGN 1,000")
        Spacer(modifier = Modifier.height(8.dp))
        SummaryItem(label = "Tax", value = "NGN 0")
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        SummaryItem(label = "Total", value = "NGN 1,000")
    }
}

@Composable
fun SummaryItem(label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            label,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InvoiceDateArea(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Invoice Date")
            Text(
                modifier = Modifier,
                text = "24 Jan, 2025",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .padding(end = 16.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text("Due Date")
            Text(
                modifier = Modifier,
                text = "24 Apr, 2025",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun InvoiceReceiverInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.LightGray, CircleShape)
                .padding(12.dp)
        ) {
            Text(text = "JD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier,
                text = "John Doe",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("12345678")
        }
    }
}

@Composable
fun InvoiceItemsDetails(modifier: Modifier = Modifier) {
    Column {
        listOf(1, 2, 3).map {
            Column {
                InvoiceItem()
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun InvoiceItem(modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(vertical = 4.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier,
                text = "John Doe",
                fontWeight = FontWeight.Bold
            )
            Text("1 X 10000", color = Color.Gray)
        }
        Text(
            modifier = Modifier,
            text = "NGN 10,000",
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
private fun InvoiceDetailsScreenPreview() {
    EBillingTheme {
        Scaffold { it -> InvoiceDetailsScreen(modifier = Modifier.padding(it)) }
    }
}
