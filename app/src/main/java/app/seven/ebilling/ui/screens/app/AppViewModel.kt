package app.seven.ebilling.ui.screens.app

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.seven.ebilling.domain.models.Invoice
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {
    val userPhoneNumber: String
        get() = firebaseUser?.phoneNumber ?: ""

    private val db = Firebase.firestore
    private val INVOICES_COLLECTION = "invoices"

    private val _events = Channel<AppUIEvent>()
    val events = _events.receiveAsFlow()

    var selectedIndex by  mutableIntStateOf(0)

    private val _state = MutableStateFlow(InvoiceListState())
    val state = _state
        .onStart { loadInvoices() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            InvoiceListState(
                isLoading = true
            )
        )

    var firebaseUser: FirebaseUser? = null
    var savingUser by mutableStateOf(false)
    var savingInvoice by mutableStateOf(false)

    init {
        loadUser()
    }

    fun loadUser() {
        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser == null) {
            logout()
        } else if (firebaseUser?.displayName.isNullOrBlank()) {
            sendUiEvent(AppUIEvent.CompleteProfile)
        } else {
            sendUiEvent(AppUIEvent.Home)
        }
    }

    fun invoiceSelected(invoice: Invoice){
        _state.update {
            it.copy(selectedInvoice = invoice)
        }
        sendUiEvent(AppUIEvent.InvoiceDetails)
    }

    fun loadInvoices() {
        db.collection(INVOICES_COLLECTION)
            .where(
                Filter.or(
                    Filter.equalTo("senderPhone", userPhoneNumber),
                    Filter.equalTo("receiverPhone", userPhoneNumber),
                )
            )
            .addSnapshotListener { value, error ->

                Log.d(this::class.java.simpleName, "Invoice fetched -> ${value?.documents}")
                if (error != null) {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                } else {

                    val invoices = value?.documents?.map {
                        it.toObject(Invoice::class.java)
                    } ?: listOf()

                    _state.update {
                        it.copy(
                            isLoading = false,
                            sentInvoiceList = invoices.filterNotNull()
                                .filter { i -> i.senderPhone == userPhoneNumber },
                            receivedInvoiceList = invoices.filterNotNull()
                                .filter { i -> i.receiverPhone == userPhoneNumber }
                        )
                    }
                }
            }
    }

    fun saveInvoice(invoice: Invoice) {
        savingInvoice = true
        db.collection(INVOICES_COLLECTION).document(invoice.id)
            .set(
                invoice.copy(
                    senderPhone = firebaseUser?.phoneNumber
                )
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendUiEvent(AppUIEvent.Back)
                    sendUiEvent(AppUIEvent.Message("Changes saved"))
                } else {
                    sendUiEvent(AppUIEvent.Message("Error saving changes, check your internet and try again"))
                }
            }
    }

    fun saveProfile(firstName: String, lastName: String) {
        updateUser(firstName, lastName) {
            sendUiEvent(AppUIEvent.Back)
        }
    }

    fun completeAccount(firstName: String, lastName: String) {
        updateUser(firstName, lastName) {
            sendUiEvent(AppUIEvent.Loading)
        }
    }

    fun showMessage(message: String) {
        sendUiEvent(AppUIEvent.Message(message))
    }

    private fun updateUser(firstName: String, lastName: String, onSuccess: () -> Unit) {
        savingUser = true

        val request = UserProfileChangeRequest.Builder()
            .setDisplayName("$firstName $lastName")
            .build()

        firebaseUser?.updateProfile(request)?.addOnCompleteListener { task ->
            savingUser = false

            if (task.isSuccessful) {
                onSuccess()
            } else {
                sendUiEvent(AppUIEvent.Message("Error saving changes, check your internet and try again"))
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        sendUiEvent(AppUIEvent.Logout)
    }

    private fun sendUiEvent(event: AppUIEvent) = viewModelScope.launch {
        _events.send(event)
    }
}

@Immutable
data class InvoiceListState(
    val isLoading: Boolean = false,
    val receivedInvoiceList: List<Invoice> = emptyList(),
    val sentInvoiceList: List<Invoice> = emptyList(),
    val selectedInvoice: Invoice? = null
)

sealed interface AppUIEvent {
    data object Loading : AppUIEvent
    data object Logout : AppUIEvent
    data object Back : AppUIEvent
    data object CompleteProfile : AppUIEvent
    data object Home : AppUIEvent
    data object InvoiceDetails : AppUIEvent
    data object CreateInvoice : AppUIEvent
    data class ViewInvoice(val invoice: Invoice) : AppUIEvent
    data class Message(val message: String) : AppUIEvent
}
