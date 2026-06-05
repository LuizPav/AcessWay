package com.example.accessway.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accessway.R
import com.example.accessway.ui.components.TextField

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accessway.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val email = viewModel.email
    val senha = viewModel.senha
    val isLoginEnabled = viewModel.isLoginEnabled

    //TEXTO REGISTRE-SE
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color(0xFF555555))) {
            append("Ainda não possui conta? ")
        }

        withStyle(
            style = SpanStyle(
                color = Color(0xFF1A73E8),
                fontWeight = FontWeight.Bold
            )
        ) {
            append("registre-se")
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // LOGO
        Image(
            painter = painterResource(id = R.drawable.logo_completo),
            contentDescription = "Logo App",
            modifier = Modifier.height(140.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bem-vindo",
            fontSize = 28.sp,
            style = MaterialTheme.typography.titleLarge,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Email
        TextField(
            value = email,
            label = "Email",
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SENHA
        TextField(
            value = senha,
            label = "Senha",
            onValueChange = { viewModel.onSenhaChange(it) },
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            keyboardType = KeyboardType.Password,
        )

        Text(
            text = annotatedText,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRegisterClick() }
                .padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // BOTÃO LOGIN
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.onBackground
            ),
            enabled = isLoginEnabled
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}