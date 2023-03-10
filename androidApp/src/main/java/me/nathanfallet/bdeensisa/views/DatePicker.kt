package me.nathanfallet.bdeensisa.views

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import me.nathanfallet.bdeensisa.extensions.renderedDate
import java.util.*

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selected: LocalDate?,
    placeholder: String = "",
    onSelected: (LocalDate) -> Unit
) {

    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        leadingIcon = {
            Text(
                text = selected?.renderedDate ?: placeholder,
                style = TextStyle(
                    fontSize = 18.sp,
                    color = if (selected != null) MaterialTheme.colors.onSurface
                    else Color.LightGray
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                onSelected(LocalDate(year, month + 1, dayOfMonth))
                                showDialog = false
                            },
                            selected?.year ?: Calendar
                                .getInstance()
                                .get(Calendar.YEAR),
                            (selected?.monthNumber ?: (Calendar
                                .getInstance()
                                .get(Calendar.MONTH) + 1)) - 1,
                            selected?.dayOfMonth ?: Calendar
                                .getInstance()
                                .get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
            )
        },
        value = "",
        onValueChange = {},
        modifier = modifier
    )

}
