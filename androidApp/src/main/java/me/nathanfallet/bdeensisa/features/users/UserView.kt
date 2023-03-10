package me.nathanfallet.bdeensisa.features.users

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.extensions.fiveYears
import me.nathanfallet.bdeensisa.extensions.oneYear
import me.nathanfallet.bdeensisa.extensions.renderedDate
import me.nathanfallet.bdeensisa.features.MainViewModel
import me.nathanfallet.bdeensisa.views.DatePicker
import me.nathanfallet.bdeensisa.views.Picker

@Composable
fun UserView(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel,
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current

    val user by viewModel.getUser().observeAsState()
    val editing by viewModel.isEditing().observeAsState(false)

    val image by viewModel.getImage().observeAsState()

    val firstName by viewModel.getFirstName().observeAsState()
    val lastName by viewModel.getLastName().observeAsState()
    val option by viewModel.getOption().observeAsState()
    val year by viewModel.getYear().observeAsState()
    val expiration by viewModel.getExpiration().observeAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            viewModel.updateImage(mainViewModel.getToken().value, uri, context)
        }
    )

    Column(modifier) {
        TopAppBar(
            title = { Text(text = "Utilisateur") },
            actions = {
                if (viewModel.editable) {
                    Text(
                        text = if (editing) "Termin??" else "Modifier",
                        modifier = Modifier
                            .clickable(onClick = viewModel::toggleEdit)
                            .padding(16.dp)
                    )
                }
            }
        )
        if (editing) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Photo d'identit??",
                style = MaterialTheme.typography.h6
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                image?.let {
                    Image(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Photo d'identit??",
                        contentScale = ContentScale.Crop
                    )
                }
                Button(
                    onClick = {
                        imagePickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text(text = if (image != null) "Modifier la photo" else "Ajouter une photo")
                }
            }
        }
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Informations",
            style = MaterialTheme.typography.h6
        )
        if (editing) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                value = firstName ?: "",
                onValueChange = viewModel::setFirstName,
                placeholder = {
                    Text(
                        text = "Pr??nom",
                        color = Color.LightGray
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                value = lastName ?: "",
                onValueChange = viewModel::setLastName,
                placeholder = {
                    Text(
                        text = "Nom",
                        color = Color.LightGray
                    )
                }
            )
            Picker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Ann??e",
                items = mapOf(
                    "1A" to "1A",
                    "2A" to "2A",
                    "3A" to "3A",
                    "other" to "4A et plus",
                    "CPB" to "CPB"
                ),
                selected = year ?: "",
                onSelected = viewModel::setYear,
            )
            Picker(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                placeholder = "Option",
                items = mapOf(
                    "ir" to "Informatique et R??seaux",
                    "ase" to "Automatique et Syst??mes embarqu??s",
                    "meca" to "M??canique",
                    "tf" to "Textile et Fibres",
                    "gi" to "G??nie Industriel"
                ),
                selected = option ?: "",
                onSelected = viewModel::setOption,
            )
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.updateInfo(mainViewModel.getToken().value) {
                        mainViewModel.setUser(it)
                    }
                }
            ) {
                Text(text = "Enregistrer")
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                image?.let {
                    Image(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape),
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Photo d'identit??",
                        contentScale = ContentScale.Crop
                    )
                }
                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "${user?.firstName} ${user?.lastName}"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = user?.description ?: ""
                    )
                }
            }
        }
        if (!viewModel.isMyAccount) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "Cotisation",
                style = MaterialTheme.typography.h6
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                text = if (user?.cotisant != null) "Cotisant" else "Non cotisant",
                color = if (user?.cotisant != null) Color.Green else Color.Red
            )
            if (user?.cotisant != null && !editing) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    text = "Expire : ${user?.cotisant?.expiration?.renderedDate}"
                )
            }
            if (editing) {
                DatePicker(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    placeholder = "Expire",
                    selected = expiration,
                    onSelected = viewModel::setExpiration,
                )
                Button(
                    onClick = {
                        viewModel.setExpiration(oneYear)
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "1 an")
                }
                Button(
                    onClick = {
                        viewModel.setExpiration(fiveYears)
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                    colors = ButtonDefaults.outlinedButtonColors(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Scolarit??")
                }
                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.updateExpiration(mainViewModel.getToken().value)
                    }
                ) {
                    Text(text = "Enregistrer")
                }
            }
        }
    }

}
