package app.seven.ebilling.ui.screens.app.invoice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.domain.models.Invoice
import app.seven.ebilling.ui.screens.app.invoice.component.InvoiceListItem


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InvoiceList(
    modifier: Modifier = Modifier,
    invoiceList: List<Invoice>,
    onItemSelected: (Invoice) -> Unit
) {

    var unpaid = invoiceList.filter { !it.isPaid }
    var paid = invoiceList.filter { it.isPaid }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        stickyHeader { HeaderItem("UNPAID") }
        if (unpaid.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        text = "No Invoice, unpaid invoices will appear here",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        items(unpaid) { it ->
            InvoiceListItem(
                invoice = it,
                onItemSelected = { onItemSelected(it) })
        }
        stickyHeader { HeaderItem("PAID") }
        if (paid.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        text = "No Invoice, paid invoices will appear here",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        items(paid) { it ->
            InvoiceListItem(
                invoice = it,
                onItemSelected = { onItemSelected(it) })
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun HeaderItem(label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .background(Color.White)
    ) {
        HorizontalDivider()
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            text = label,
            textAlign = TextAlign.End
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun InvoiceListPreview() {
    InvoiceList(
        onItemSelected = {},
        invoiceList = listOf(
            Invoice.create().copy(
                amountPaid = 1,
                items = listOf(
                    Invoice.InvoiceItem.create(

                    )
                )
            ),
            Invoice.create().copy(
                amountPaid = 0,
                items = listOf(
                    Invoice.InvoiceItem.create(

                    )
                )
            )
        )
    )
}