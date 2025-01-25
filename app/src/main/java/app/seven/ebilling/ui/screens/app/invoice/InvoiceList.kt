package app.seven.ebilling.ui.screens.app.invoice

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.ui.screens.app.invoice.component.InvoiceListItem


@Composable
fun InvoiceList(modifier: Modifier = Modifier, onItemSelected: () -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item { HeaderItem("UNPAID") }
        items(4) {
            InvoiceListItem(onItemSelected = { onItemSelected() })
        }
        item { HeaderItem("PAID") }
        items(10) {
            InvoiceListItem(onItemSelected = { onItemSelected()})
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
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = label,
            textAlign = TextAlign.End
        )
    }
}

@Preview
@Composable
private fun InvoiceListPreview() {
    InvoiceList(onItemSelected = {})
}