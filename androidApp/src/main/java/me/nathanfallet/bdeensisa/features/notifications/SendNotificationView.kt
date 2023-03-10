package me.nathanfallet.bdeensisa.features.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.views.Picker

@Composable
fun SendNotificationView(
    modifier: Modifier,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current

    val viewModel: SendNotificationViewModel = viewModel()

    val topic by viewModel.getTopic().observeAsState()
    val title by viewModel.getTitle().observeAsState()
    val body by viewModel.getBody().observeAsState()
    val sent by viewModel.getSent().observeAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TopAppBar(
            title = {
                Text("Envoi de notification")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Picker(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            placeholder = "Sujet",
            items = mapOf(
                "broadcast" to "Général",
                "cotisants" to "Cotisants",
                "events" to "Evènements"
            ),
            selected = topic ?: "",
            onSelected = viewModel::setTopic,
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = title ?: "",
            onValueChange = viewModel::setTitle,
            placeholder = {
                Text(
                    text = "Titre",
                    color = Color.LightGray
                )
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            value = body ?: "",
            onValueChange = viewModel::setBody,
            placeholder = {
                Text(
                    text = "Contenu",
                    color = Color.LightGray
                )
            }
        )
        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            enabled = title?.isNotEmpty() ?: false && body?.isNotEmpty() ?: false,
            onClick = {
                viewModel.send(mainViewModel.getToken().value)
            }
        ) {
            Text(text = "Envoyer")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (sent == true) {
            AlertDialog(
                onDismissRequest = viewModel::dismissSent,
                title = { Text("Notification envoyée") },
                confirmButton = {
                    Button(onClick = viewModel::dismissSent) {
                        Text("OK")
                    }
                }
            )
        }
    }

}
