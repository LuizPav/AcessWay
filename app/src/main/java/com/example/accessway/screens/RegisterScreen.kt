package com.example.accessway.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accessway.R
import com.example.accessway.ui.components.TextField

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(),
    goBack: () -> Unit,
    submit: () -> Unit,
) {
    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val confirmPassword = viewModel.confirmPassword
    val errorMessage = viewModel.errorMessage

    val emailError = viewModel.emailError
    val passwordError = viewModel.passwordError
    val passwordMismatchError = viewModel.passwordMismatchError

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { goBack() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_completo),
            contentDescription = "Logo App",
            modifier = Modifier.height(140.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = name,
            label = "Insira seu username",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = false,
            errorMessage = "Nome não pode ser Vazio",
            onValueChange = { viewModel.onNameChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            label = "Insira seu email",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = emailError,
            keyboardType = KeyboardType.Email,
            errorMessage = "Email Inválido",
            onValueChange = { viewModel.onEmailChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            label = "Insira sua senha",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = passwordError,
            isPassword = true,
            errorMessage = "A senha deve ter no mínimo 6 caracteres",
            onValueChange = { viewModel.onPasswordChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword,
            label = "Confirme sua senha",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = passwordMismatchError,
            isPassword = true,
            errorMessage = "As senhas não conferem",
            onValueChange = { viewModel.onConfirmPasswordChange(it) }
        )

        if (passwordMismatchError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "As senhas não conferem.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.handleRegister(submit) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White,

                disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.38f),
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = "Cadastrar",
                fontSize = 18.sp
            )
        }
    }
}