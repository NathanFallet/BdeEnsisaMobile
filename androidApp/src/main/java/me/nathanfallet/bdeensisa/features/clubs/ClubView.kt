package me.nathanfallet.bdeensisa.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.nathanfallet.bdeensisa.features.MainViewModel

@Composable
fun ClubView(
    modifier: Modifier = Modifier,
    viewModel: ClubViewModel,
    mainViewModel: MainViewModel
) {

    val user by mainViewModel.getUser().observeAsState()

    val members by viewModel.getMembers().observeAsState()

    LazyColumn(modifier) {
        item {
            TopAppBar(
                title = { Text(text = viewModel.club.name) },
                actions = {
                    if (members?.any { it.userId == user?.id } == true) {
                        Text(
                            text = "Quitter",
                            modifier = Modifier
                                .clickable {
                                    viewModel.leave(mainViewModel.getToken().value)
                                }
                                .padding(16.dp)
                        )
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            ClubCard(
                club = viewModel.club,
                badgeText = if (user?.cotisant != null && members?.none { it.userId == user?.id } == true) "REJOINDRE" else null,
                badgeColor = MaterialTheme.colors.primary,
                action = {
                    viewModel.join(mainViewModel.getToken().value)
                },
                detailsEnabled = false
            )
        }
        item {
            Text(
                text = "Membres",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            )
        }
        items(members ?: listOf()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 4.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = "${it.user?.firstName} ${it.user?.lastName}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.user?.description ?: "")
                    }
                    Text(
                        text = if (it.role == "admin") "ADMIN" else "MEMBRE",
                        style = MaterialTheme.typography.caption,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                if (it.role == "admin") Color.Black
                                else Color(0xFF0BDA51),
                                MaterialTheme.shapes.small
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

}
