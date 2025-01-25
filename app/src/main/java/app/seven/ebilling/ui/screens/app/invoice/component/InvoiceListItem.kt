package app.seven.ebilling.ui.screens.app.invoice.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InvoiceListItem(modifier: Modifier = Modifier, onItemSelected: () -> Unit) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = {
            onItemSelected()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.LightGray, CircleShape)
                    .padding(12.dp)
            ) {
                Text(text = "JD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "John Doe",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NGN1,500.00", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Shoes",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("Due: 20-Jan-2025")
                }
            }
        }
    }
}

@Preview
@Composable
private fun InvoiceListItemPreview() {
    InvoiceListItem(onItemSelected = {})
}