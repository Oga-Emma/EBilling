package app.seven.ebilling.domain.models

import app.seven.ebilling.domain.utils.titlecase
import java.util.UUID

data class Invoice(
    val id: String = UUID.randomUUID().toString(),
    val status: Status = Status.PENDING,
    val issueDate: Long? = null,
    val dueDate: Long? = null,
    val items: List<InvoiceItem> = emptyList(),
    val paymentMode: PaymentMode? = null,
    val senderPhone: String? = null,
    val receiverPhone: String? = null,
    val amountPaid: Int = 0
) {
    companion object {
        fun create() = Invoice()
    }

    val total: Int
        get() = items.sumOf { it.totalPrice }

    val isPaid: Boolean
        get() = amountPaid >= total

    val formattedPrice: String
        get() = "₦$total"

    val formattedBalance: String
        get() = "₦${total - amountPaid}"

    data class InvoiceItem(
        val id: String = UUID.randomUUID().toString(),
        val description: String = "",
        val quantity: Int = 1,
        val price: Int = 1
    ) {
        val totalPrice: Int
            get() = quantity * price

        val formattedPrice: String
            get() = "₦$totalPrice"

        companion object {
            fun create() = InvoiceItem()
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
