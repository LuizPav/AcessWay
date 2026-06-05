package com.example.accessway.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {

    var textSearch by remember {
        mutableStateOf("")
    }

    var showMenu by remember {
        mutableStateOf(false)
    }

    fun onSearchSubmit(txt: String) {
        println(txt)
    }

    Row(
        modifier = modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        TextField(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(24.dp)
                )
                .fillMaxWidth()
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(24.dp)
                ),

            value = textSearch,

            onValueChange = {
                textSearch = it
            },

            shape = RoundedCornerShape(24.dp),

            placeholder = {
                Text("Buscar parada ou endereço")
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.DarkGray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),

            leadingIcon = {

                Box {

                    IconButton(
                        onClick = {
                            showMenu = true
                            onMenuClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir menu",
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        },
                        containerColor = Color.White,
                        modifier = Modifier
                    ) {

                        DropdownMenuItem(
                            text = { Text("Paradas próximas") },
                            onClick = {
                                showMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Reportar problema") },
                            onClick = {
                                showMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Avaliações") },
                            onClick = {
                                showMenu = false
                            }
                        )
                    }
                }
            },

            trailingIcon = {
                IconButton(
                    onClick = {
                        onSearchSubmit(textSearch)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Black
                    )
                }
            },

            singleLine = true
        )
    }
}