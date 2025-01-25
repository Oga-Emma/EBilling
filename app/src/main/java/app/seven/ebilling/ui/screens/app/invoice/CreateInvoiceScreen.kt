package app.seven.ebilling.ui.screens.app.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import app.seven.ebilling.domain.models.Invoice
import app.seven.ebilling.domain.models.formattedString
import app.seven.ebilling.domain.utils.addCountryCode
import app.seven.ebilling.domain.utils.isValidPhoneNumber
import app.seven.ebilling.domain.utils.removeCountryCode
import app.seven.ebilling.ui.core.components.ETextField
import app.seven.ebilling.ui.core.theme.EBillingTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateInvoiceScreen(
    modifier: Modifier = Modifier,
    invoiceDetails: Invoice = Invoice.create(),
    savingChanges: Boolean,
    showMessage: (String) -> Unit,
    saveChanges: (Invoice) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }
    var paymentMode by remember { mutableStateOf<Invoice.PaymentMode?>(invoiceDetails.paymentMode) }
    var paymentModeError by remember { mutableStateOf(false) }

    var phone by remember { mutableStateOf(removeCountryCode(invoiceDetails.senderPhone)) }
    var phoneError by remember { mutableStateOf(false) }

    var issuedAt by remember { mutableStateOf<Long?>(System.currentTimeMillis()) }
    var issuedAtError by remember { mutableStateOf(false) }

    var dueAt by remember { mutableStateOf<Long?>(null) }
    var dueAtError by remember { mutableStateOf(false) }

    var invoiceItems by remember { mutableStateOf(invoiceDetails.items) }
    var selectedItem by remember { mutableStateOf<Invoice.InvoiceItem?>(null) }

    if (selectedItem != null) {
        ModalBottomSheet(
            onDismissRequest = {
                selectedItem = null
            },
            sheetState = sheetState
        ) {
            EditInvoiceItem(
                invoiceItem = selectedItem!!,
                close = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            selectedItem = null
                        }
                    }
                },
                saveChanges = { currItem ->
                    val items = invoiceItems.toMutableList()

                    val index = items.indexOfFirst { it.id == currItem.id }
                    if (index == -1) {
                        items.add(currItem)
                    } else {
                        items[index] = currItem
                    }

                    invoiceItems = items
                    selectedItem = null
                }
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CreateInvoiceHeaderItem("Recipient") {
                TextButton(onClick = {}) {
//                    Icon(Icons.Default.Search, "")
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Search")
                }
            }
            ETextField(
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.fillMaxWidth(),
                value = phone,
                onValueChange = {
                    phone = it
                    phoneError = false
                },
                label = "Phone Number",
                hasError = phoneError,
                errorMessage = "Enter a recipient phone number",
            )

            Spacer(modifier = Modifier.height(8.dp))
            CreateInvoiceHeaderItem("Invoice Items") {
                TextButton(onClick = {
                    selectedItem = Invoice.InvoiceItem.create()
                }) {
                    Icon(Icons.Default.AddCircle, "")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Item")
                }
            }

            if (invoiceItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                        text = "You haven't added any item",
                        textAlign = TextAlign.Center,
                    )
                }
            }

            invoiceItems.map {
                Column {
                    InvoiceItemWidget(
                        item = it,
                        onEdit = { selectedItem = it },
                        onDelete = { dIt ->
                            invoiceItems = invoiceItems.filter { it.id == dIt.id }
                        },
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.LightGray
                    )
                }
            }

            CreateInvoiceHeaderItem("Invoice Details") {
                Spacer(modifier = Modifier.height(44.dp))
            }
            Row {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    DatePickerFieldToModal(
                        value = issuedAt,
                        label = "Issued date",
                        hasError = issuedAtError,
                        errorMessage = "Select issued date",
                        onDateSelected = {
                            issuedAt = it
                            issuedAtError = false
                        }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    DatePickerFieldToModal(
                        value = dueAt,
                        label = "Due date",
                        hasError = dueAtError,
                        errorMessage = "Select due date",
                        onDateSelected = {
                            dueAt = it
                            dueAtError = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    // The `menuAnchor` modifier must be passed to the text field for correctness.
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = paymentMode?.formattedString() ?: "",
                    onValueChange = {
                        paymentModeError = false
                    },
                    label = { Text("Payment Mode") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                DropdownMenu(
                    modifier = Modifier
                        .background(Color.White)
                        .exposedDropdownSize(true),
                    properties = PopupProperties(focusable = false),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Invoice.PaymentMode.entries.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.formattedString()) },
                            onClick = {
                                paymentMode = selectionOption
                                paymentModeError = false
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            if (paymentModeError)
                Text("Enter payment mode", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .fillMaxWidth()
                .height(52.dp),
            enabled = !savingChanges,
            shape = RoundedCornerShape(4.dp),
            onClick = {
                var hasError = false

                if (!isValidPhoneNumber(phone)) {
                    hasError = true
                    phoneError = true
                }

                if (dueAt == null) {
                    hasError = true
                    dueAtError = true
                }

                if (paymentMode == null) {
                    hasError = true
                    paymentModeError = true
                }

                if (!hasError && invoiceItems.isEmpty()) {
                    hasError = true
                    showMessage("Please add at least one item")
                }

                if (!hasError) {
                    saveChanges(
                        invoiceDetails.copy(
                            receiverPhone = addCountryCode(phone),
                            paymentMode = paymentMode,
                            dueDate = dueAt,
                            issueDate = issuedAt,
                            items = invoiceItems
                        )
                    )
                }
            }
        ) {
            if(savingChanges) CircularProgressIndicator() else Text("CONTINUE")
        }
    }
}

@Composable
fun InvoiceItemWidget(
    modifier: Modifier = Modifier,
    item: Invoice.InvoiceItem,
    onEdit: (Invoice.InvoiceItem) -> Unit,
    onDelete: (Invoice.InvoiceItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.clickable {
                onDelete(item)
            },
            imageVector = Icons.Default.Delete, contentDescription = "delete item"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = item.description,
                    fontWeight = FontWeight.Bold,
                )
                Text("NGN ${item.totalPrice}", fontWeight = FontWeight.Bold)
            }
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${item.quantity} x ${item.price}",
                    fontSize = 12.sp, color = Color.Gray
                )
                Row(
                    modifier = Modifier.clickable { onEdit(item) }
                ) {
                    Text("Edit", fontSize = 12.sp, color = Color.Gray)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    label: String,
    value: Long?,
    onDateSelected: (Long) -> Unit,
    hasError: Boolean,
    errorMessage: String
) {
    var selectedDate by remember { mutableStateOf<Long?>(value) }
    var showModal by remember { mutableStateOf(false) }

    Column(
    ) {
        ETextField(
            value = selectedDate?.let { convertMillisToDate(it) } ?: "",
            onValueChange = { },
            label = label,
            placeholder = "MM/DD/YYYY",
            hasError = hasError,
            errorMessage = errorMessage,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            textFieldModifier = modifier
                .fillMaxWidth()
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                        // in the Initial pass to observe events before the text field consumes them
                        // in the Main pass.
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showModal = true
                        }
                    }
                }
        )
    }

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
                if (it != null) {
                    onDateSelected(it)
                }
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun CreateInvoiceHeaderItem(
    label: String,
    modifier: Modifier = Modifier,
    leading: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                textAlign = TextAlign.End
            )
            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .weight(1f)
            )
            leading()
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun EditInvoiceItem(
    invoiceItem: Invoice.InvoiceItem,
    modifier: Modifier = Modifier,
    close: () -> Unit,
    saveChanges: (Invoice.InvoiceItem) -> Unit
) {
    var item by remember { mutableStateOf(invoiceItem) }
    var descriptionError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Invoice Item", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(24.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = item.description,
            onValueChange = {
                item = item.copy(description = it)
                descriptionError = false
            },
            label = "Description",
            errorMessage = "Enter description",
            hasError = descriptionError,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = item.quantity.toString(),
            onValueChange = {
                item = item.copy(quantity = it.toIntOrNull() ?: 1)
                quantityError = false
            },
            label = "Quantity",
            errorMessage = "Enter quantity",
            hasError = quantityError,
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ETextField(
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.fillMaxWidth(),
            value = item.price.toString(),
            onValueChange = {
                item = item.copy(price = it.toIntOrNull() ?: 1)
                priceError = false
            },
            label = "Price (per item)",
            errorMessage = "Enter a price greater than 1",
            hasError = priceError,
            keyboardType = KeyboardType.Number,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = { close(); }
            ) {
                Text("CANCEL")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    var hasError = false

                    if (item.description.isBlank()) {
                        hasError = true
                        descriptionError = true
                    }

                    if (item.price < 1) {
                        hasError = true
                        priceError = true
                    }

                    if (item.quantity < 1) {
                        item = item.copy(quantity = 1)
                    }

                    if (!hasError) {
                        saveChanges(item)
                    }
                }
            ) {
                Text("CONTINUE")
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

//        Row(
//            modifier = Modifier.padding(horizontal = 16.dp)
//        ) {
//            OutlinedButton(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(52.dp),
//                shape = RoundedCornerShape(4.dp),
//                onClick = { }
//            ) {
//                Text("SAVE AS DRAFT")
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(52.dp),
//                shape = RoundedCornerShape(4.dp),
//                onClick = { }
//            ) {
//                Text("CONTINUE")
//            }
//        }

//
//data class InvoiceValidator(
//    val phone: String  = "",
//    val issueAt: String  = "",
//    val dueAt: String  = "",
//    val paymentMode: String  = "",
//) {
//    fun isInvalid() = phone.isBlank() && issueAt.isBlank() && dueAt.isBlank()
//}


@Preview
@Composable
private fun InvoiceItemWidgetPreview() {
    EBillingTheme {
        InvoiceItemWidget(
            item = Invoice.InvoiceItem(
                description = "Test",
                quantity = 1,
                price = 100
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}
//
//@Preview
//@Composable
//private fun EditInvoiceItemPreview() {
//    EBillingTheme {
//        Scaffold { it ->
//            EditInvoiceItem(
//                invoiceItem = Invoice.InvoiceItem(
//                    description = "",
//                    quantity = 0,
//                    price = 0,
//                ),
//                modifier = Modifier.padding(it),
//                saveChanges = {},
//                close = {}
//            )
//        }
//    }
//}


@Preview
@Composable
private fun CreateInvoiceScreenPreview() {
    EBillingTheme {
        Scaffold { it ->
            CreateInvoiceScreen(
                savingChanges = false,
                invoiceDetails = Invoice.create().copy(
                    items = listOf(
                        Invoice.InvoiceItem(
                            description = "Test",
                            quantity = 1,
                            price = 100
                        ),
                        Invoice.InvoiceItem(
                            description = "Test",
                            quantity = 1,
                            price = 100
                        )
                    )
                ),
                modifier = Modifier.padding(it),
                showMessage = {},
                saveChanges = {}
            )
        }
    }
}
