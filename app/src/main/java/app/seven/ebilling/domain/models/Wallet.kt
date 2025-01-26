package app.seven.ebilling.domain.models


data class Wallet(
    val balance: Int = 0
) {

    val formattedBalance: String
        get() = "₦$balance"
}
