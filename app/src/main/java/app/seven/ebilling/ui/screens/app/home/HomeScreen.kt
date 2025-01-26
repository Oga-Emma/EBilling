package app.seven.ebilling.ui.screens.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.domain.models.Invoice
import app.seven.ebilling.ui.screens.app.InvoiceListState
import app.seven.ebilling.ui.screens.app.invoice.InvoiceList

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onItemSelected: (Invoice) -> Unit,
    invoiceState: InvoiceListState,
    onIndexSelected: (Int) -> Unit,
    selectedIndex: Int,
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InvoiceTabs(
            selectedIndex = selectedIndex,
            onSelectTab = onIndexSelected
        )
        InvoiceList(
            modifier = Modifier
                .weight(1f),
            invoiceList = if (selectedIndex == 0) {
                invoiceState.receivedInvoiceList
            } else invoiceState.sentInvoiceList,
            onItemSelected = onItemSelected
        )
    }
}

@Composable
fun InvoiceTabs(selectedIndex: Int, onSelectTab: (Int) -> Unit) {

    val list = listOf("INVOICES RECEIVED", "INVOICES SENT")

    TabRow(selectedTabIndex = selectedIndex,
//        backgroundColor = Color(0xff1E76DA),

        modifier = Modifier
            .padding(4.dp)
//            .border(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
//                shape = RoundedCornerShape(4.dp)
//            )
            .clip(RoundedCornerShape(4.dp)),
//        indicator = { Box {} },
        divider = { Box {} }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
//                modifier = if (!selected) Modifier
//                else Modifier
//                    .background(MaterialTheme.colorScheme.primaryContainer),
                selected = selected,
                onClick = {
                    if (selectedIndex != index) {
                        onSelectTab(index)
                    }
                },
                text = { Text(text = text, fontWeight = FontWeight.Bold) }
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onItemSelected = {},
        invoiceState = InvoiceListState(
            isLoading = false,
            selectedInvoice = null,
            sentInvoiceList = listOf(),
            receivedInvoiceList = listOf()
        ),
        onIndexSelected = {

        },
        selectedIndex = 1,
    )
}
