package app.seven.ebilling.ui.screens.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {
    private val db = Firebase.firestore
    private val INVOICES_COLLECTION = "invoices"

    private val _events = Channel<AppUIEvent>()
    val events = _events.receiveAsFlow()

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

    fun loadInvoices() {
        db.collection(INVOICES_COLLECTION)
            .where(
                Filter.or(
                    Filter.equalTo("sender_phone", firebaseUser!!.phoneNumber),
                    Filter.equalTo("receiver_phone", firebaseUser!!.phoneNumber),
                )
            )
            .addSnapshotListener { value, error ->

                if (error != null) {
                    value?.documents?.map {

                    }
                } else {

                }
            }
    }

    fun saveInvoice(invoice: Invoice) {
        savingInvoice = true
        db.collection(INVOICES_COLLECTION).document(invoice.id.toString())
            .set(invoice.copy(
                senderPhone = firebaseUser?.phoneNumber
            ))
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

    fun showMessage(message: String){
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


    sealed interface AppUIEvent {
        data object Loading : AppUIEvent
        data object Logout : AppUIEvent
        data object Back : AppUIEvent
        data object CompleteProfile : AppUIEvent
        data object Home : AppUIEvent
        data object CreateInvoice : AppUIEvent
        data class ViewInvoice(val invoice: Invoice) : AppUIEvent
        data class Message(val message: String) : AppUIEvent
    }
}
