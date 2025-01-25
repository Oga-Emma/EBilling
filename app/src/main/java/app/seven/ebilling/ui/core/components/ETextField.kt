package app.seven.ebilling.ui.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun ETextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    label: String,
    placeholder: String? = null,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    hasError: Boolean = false,
    enabled: Boolean = true,
    keyboardType: KeyboardType? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { if (placeholder != null) Text(placeholder) },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            modifier = textFieldModifier,
//            maxLength = maxLength,
            enabled = enabled,
            keyboardOptions = if (keyboardType == null) KeyboardOptions.Default else KeyboardOptions(
                keyboardType = keyboardType
            ),
            keyboardActions = keyboardActions
        )
        if (hasError)
            Text(errorMessage, color = Color.Red)
    }
}