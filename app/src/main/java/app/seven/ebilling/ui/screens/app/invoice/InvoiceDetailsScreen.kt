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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.seven.ebilling.domain.models.Invoice
import app.seven.ebilling.domain.utils.convertMillisToDate
import app.seven.ebilling.domain.utils.removeCountryCode
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.payment.CreditCardForm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailsScreen(modifier: Modifier = Modifier, invoice: Invoice, myPhoneNumber: String) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Amount: ${invoice.formattedPrice}",
                    fontWeight = FontWeight.Bold
                )
                CreditCardForm()
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                ) {
                    Text("Pay")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RecipientArea(invoice = invoice)
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        InvoiceDateArea(modifier = Modifier.padding(vertical = 16.dp), invoice = invoice)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "ITEMS (${invoice.items.size})", fontWeight = FontWeight.Bold)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        InvoiceItemsDetails(
            modifier = Modifier.padding(vertical = 8.dp),
            items = invoice.items
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "PAYMENT SUMMARY",
            fontWeight = FontWeight.Bold
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Paid 3 of 4 Installments",
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MoneySummaryArea(invoice = invoice)
        Spacer(modifier = Modifier.height(24.dp))

        if (myPhoneNumber != invoice.senderPhone)
            ButtonArea(
                onMakePayment = {
                    showBottomSheet = true
                }
            )
    }
}

@Composable
fun ButtonArea(modifier: Modifier = Modifier, onMakePayment: () -> Unit) {
    Column {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                onMakePayment()
            }
        ) {
            Text("Make Full Payment")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                onMakePayment()
            }
        ) {
            Text("Pay Next Installment")
        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Row {
//            OutlinedButton(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .height(52.dp),
//                shape = RoundedCornerShape(4.dp),
//                onClick = { }
//            ) {
//                Text("Save as Draft")
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .height(52.dp),
//                shape = RoundedCornerShape(4.dp),
//                onClick = { }
//            ) {
//                Text("Send Invoice")
//            }
//        }
    }
}

@Composable
fun MoneySummaryArea(modifier: Modifier = Modifier, invoice: Invoice) {
    Column {
        SummaryItem(label = "Total", value = "${invoice.total}")
        Spacer(modifier = Modifier.height(8.dp))
        SummaryItem(label = "Amount Paid", value = "${invoice.amountPaid}")
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.LightGray.copy(alpha = 0.5f)
        )
        SummaryItem(label = "Balance", value = invoice.formattedBalance)
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
fun RecipientArea(modifier: Modifier = Modifier, invoice: Invoice) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Sender")
            Text(
                modifier = Modifier,
                text = removeCountryCode(invoice.senderPhone ?: ""),
                color = Color.Gray,
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
            Text("Recipient")
            Text(
                modifier = Modifier,
                text = removeCountryCode(invoice.senderPhone ?: ""),
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun InvoiceDateArea(modifier: Modifier = Modifier, invoice: Invoice) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Invoice Date")
            Text(
                modifier = Modifier,
                text = convertMillisToDate(invoice.issueDate),
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
                text = convertMillisToDate(invoice.dueDate),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,

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
fun InvoiceItemsDetails(modifier: Modifier = Modifier, items: List<Invoice.InvoiceItem>) {
    Column(
        modifier = modifier
    ) {
        items.map { item ->
            Column {
                Row(modifier = modifier.padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier,
                            text = item.description
                        )
                        Text("X${item.quantity}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Text(
                        modifier = Modifier,
                        text = item.formattedPrice,
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun InvoiceDetailsScreenPreview() {
    EBillingTheme {
        Scaffold { it ->
            InvoiceDetailsScreen(
                modifier = Modifier.padding(it), invoice = Invoice.create().copy(
                    senderPhone = "07012446202",
                    receiverPhone = "08115056400"
                ), myPhoneNumber = ""
            )
        }
    }
}
