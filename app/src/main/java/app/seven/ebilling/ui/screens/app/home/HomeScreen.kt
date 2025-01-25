package app.seven.ebilling.ui.screens.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.seven.ebilling.ui.screens.app.invoice.InvoiceList

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onItemSelected: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        InvoiceTabs()
        InvoiceList(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            onItemSelected = { onItemSelected() }
        )
    }
}

@Composable
fun InvoiceTabs() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val list = listOf("INVOICES SENT", "INVOICES RECEIVED")

    TabRow(selectedTabIndex = selectedIndex,
//        backgroundColor = Color(0xff1E76DA),

        modifier = Modifier
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp)
            ).clip(RoundedCornerShape(4.dp)),
        indicator = { Box {} },
        divider = { Box {} }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (!selected) Modifier
                else Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer),
                selected = selected,
                onClick = { selectedIndex = index },
                text = { Text(text = text) }
            )
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(onItemSelected = {})
}