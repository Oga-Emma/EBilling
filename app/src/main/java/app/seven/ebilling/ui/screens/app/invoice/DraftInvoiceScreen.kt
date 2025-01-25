package app.seven.ebilling.ui.screens.app.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.ui.core.theme.EBillingTheme
import app.seven.ebilling.ui.screens.app.invoice.component.InvoiceListItem

@Composable
fun DraftInvoiceScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.weight(1f).padding(horizontal = 8.dp)
        ) {
            items(4) {
                InvoiceListItem(
                    onItemSelected = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun DraftInvoiceScreen() {
    EBillingTheme {
        Scaffold { it ->
            DraftInvoiceScreen( modifier = Modifier.padding(it))
        }
    }
}