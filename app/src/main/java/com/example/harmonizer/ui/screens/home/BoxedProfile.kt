package com.example.harmonizer.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.harmonizer.ui.dictionary.ScreenName
import com.example.harmonizer.ui.viewmodels.UserViewModel


@Composable
fun BoxedProfile(
    userViewModel: UserViewModel,
    navController: NavController,
    updateIsUserAuthorized: (Boolean) -> Unit
) {
    val userState = userViewModel.user.observeAsState()
    var isEditingFirstName by remember { mutableStateOf(false) }
    var isEditingLastName by remember { mutableStateOf(false) }
    var userFirstName by remember { mutableStateOf(userState.value!!.firstName) }
    var userLastName by remember { mutableStateOf(userState.value!!.lastName) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Profil",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),

            ) {

            Text(
                text = "Imię",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 8.dp),
            )

            if (isEditingFirstName) {

                TextField(
                    modifier = Modifier.weight(4f),
                    value = userFirstName,
                    onValueChange = { userFirstName = it },
                    textStyle = MaterialTheme.typography.titleSmall,
                    singleLine = true
                )
            } else {
                Text(
                    modifier = Modifier.weight(4f),
                    text = userFirstName ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(modifier = if (isEditingFirstName) Modifier.weight(1f) else Modifier,
                onClick = {
                    isEditingFirstName = !isEditingFirstName

                    if (!isEditingFirstName) {
                        userViewModel.updateUserName(
                            userFirstName, userLastName
                        )
                    }
                }) {
                Icon(
                    imageVector = if (isEditingFirstName) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditingFirstName) "Zatwierdź edycję" else "Edytuj pole"
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nazwisko",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(2f)
                    .padding(horizontal = 8.dp),
            )

            if (isEditingLastName) {
                TextField(
                    modifier = Modifier.weight(4f),
                    value = userLastName,
                    onValueChange = { userLastName = it },
                    textStyle = MaterialTheme.typography.titleSmall,
                    singleLine = true
                )
            } else {
                Text(
                    modifier = Modifier.weight(4f),
                    text = userLastName,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            IconButton(modifier = if (isEditingLastName) Modifier.weight(1f) else Modifier,
                onClick = {
                    isEditingLastName = !isEditingLastName

                    if (!isEditingLastName) {
                        userViewModel.updateUserName(
                            userFirstName, userLastName
                        )
                    }
                }) {
                Icon(
                    imageVector = if (isEditingLastName) Icons.Default.Check else Icons.Default.Edit,
                    contentDescription = if (isEditingLastName) "Zatwierdź edycję" else "Edytuj pole"
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    userViewModel.logout()
                    updateIsUserAuthorized(false)
                    navController.navigate(ScreenName.Auth) {
                        popUpTo(ScreenName.Login) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Wyloguj się",
                    tint = Color.White
                )
                Text(
                    text = "Wyloguj się",
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}
