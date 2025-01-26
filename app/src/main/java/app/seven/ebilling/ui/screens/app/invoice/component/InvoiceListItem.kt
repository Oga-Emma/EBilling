package app.seven.ebilling.ui.screens.app.invoice.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import app.seven.ebilling.domain.models.Invoice
import app.seven.ebilling.domain.utils.convertMillisToDate
import app.seven.ebilling.domain.utils.removeCountryCode

@Composable
fun InvoiceListItem(modifier: Modifier = Modifier, onItemSelected: () -> Unit, invoice: Invoice) {
    Card(
        modifier = modifier.padding(vertical = 4.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray.copy(0.5f)),
        shape = RoundedCornerShape(8.dp),
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
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.Top) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column {
                            Text(text = "FROM", fontSize = 10.sp)
                            Text(
                                text = removeCountryCode(invoice.senderPhone),
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                        Icon(
                            modifier = Modifier
                                .size(28.dp)
                                .padding(horizontal = 8.dp),
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = ""
                        )
                        Column {
                            Text(text = "TO", fontSize = 10.sp)
                            Text(
                                text = removeCountryCode(invoice.receiverPhone),
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    InvoiceStatus(
                        invoice = invoice
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = invoice.formattedPrice,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (invoice.items.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = invoice.items.map { "${it.description} (x${it.quantity})" }
                                    .joinToString(", "),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Gray,
                            )
                        }
                    }
                    Text("Due: ${convertMillisToDate(invoice.dueDate!!)}", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun InvoiceStatus(modifier: Modifier = Modifier, invoice: Invoice) {
    val color = if (invoice.isPaid) Color.Blue else Color.Gray
    Text(
        modifier = modifier
            .background(
                color.copy(0.1f), shape = CircleShape
            )
            .padding(horizontal = 8.dp, vertical = 2.dp),
        text = if (invoice.isPaid) "Paid" else "Pending", fontSize = 11.sp, color = color
    )
}

@Preview
@Composable
private fun InvoiceListItemPreview() {
    InvoiceListItem(
        onItemSelected = {}, invoice = Invoice.create().copy(
            senderPhone = "07012446202",
            receiverPhone = "08115056400"
        )
    )
}