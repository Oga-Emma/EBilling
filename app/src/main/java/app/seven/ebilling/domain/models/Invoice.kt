package app.seven.ebilling.domain.models

import app.seven.ebilling.domain.utils.titlecase
import java.time.LocalDate
import java.util.UUID

data class Invoice(
    val id: UUID = UUID.randomUUID(),
    val status: Status = Status.PENDING,
    val issueDate: Long? = null,
    val dueDate: Long? = null,
    val items: List<InvoiceItem> = emptyList(),
    val paymentMode: PaymentMode? = null,
    val senderPhone: String? = null,
    val receiverPhone: String? = null,
) {
    companion object {
        fun create() = Invoice()
    }

    data class InvoiceItem(
        val id: UUID = UUID.randomUUID(),
        val description: String,
        val quantity: Int,
        val price: Int
    ) {
        val totalPrice: Int
            get() = quantity * price

        companion object {
            fun create() = InvoiceItem(
                description = "",
                quantity = 1,
                price = 1
            )
        }
    }

    enum class Status {
        PENDING,
        PAID,
        CANCELED
    }

    enum class PaymentMode {
        ONE_INSTALLMENT,
        TWO_INSTALLMENT,
        THREE_INSTALLMENT,
        FOUR_INSTALLMENT
    }
}

fun Invoice.PaymentMode.formattedString(): String{
    return this.toString().split("_").joinToString(" ").titlecase()
}
