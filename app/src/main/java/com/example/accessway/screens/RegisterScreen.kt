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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.accessway.R
import com.example.accessway.ui.components.TextField

@Composable
fun RegisterScreen(
    modifier: Modifier,
    goBack: () -> Unit,
    submit: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Funções de validação auxiliares
    fun String.isValidEmail(): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return this.matches(emailRegex)
    }

    // Regras de validação (Modificadas para refletir o estado de ERRO se forem TRUE)
    val nameError = name.isEmpty() // Só exibe erro se o dev quiser validar ao clicar, ou remova se não quiser erro ao iniciar
    val emailError = email.isNotEmpty() && !email.isValidEmail()
    val passwordError = password.isNotEmpty() && password.length < 6
    val passwordMismatchError = confirmPassword.isNotEmpty() && password != confirmPassword

    fun handleRegister() {
        errorMessage = null

        if (name.isBlank()) {
            errorMessage = "Erro ao registrar conta, nome não pode ser vazio."
            return
        }
        if (password.length < 6) {
            errorMessage = "A senha deve ter pelo menos 6 caracteres."
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Erro ao registrar conta, Senhas diferentes."
            return
        }
        if (!email.isValidEmail()) {
            errorMessage = "Erro ao registrar conta, Email inválido."
            return
        }

        submit()
    }

    Column(
        modifier = Modifier
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

        // 1. Corrigido: Usando a variável name e checando se está vazia apenas após interação (opcional)
        TextField(
            value = name,
            label = "Insira seu username",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = false, // Geralmente não mostramos erro no nome assim que a tela abre
            errorMessage = "Nome não pode ser Vazio",
            onValueChange = { name = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Corrigido: Mostra erro se o email NÃO for válido (e não estiver vazio)
        TextField(
            value = email,
            label = "Insira seu email",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = emailError,
            keyboardType = KeyboardType.Email,
            errorMessage = "Email Inválido",
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Corrigido: Mostra erro se a senha digitada for menor que 6 caracteres
        TextField(
            value = password,
            label = "Insira sua senha",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = passwordError,
            isPassword = true,
            errorMessage = "A senha deve ter no mínimo 6 caracteres",
            onValueChange = { password = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Corrigido: Mostra erro se a confirmação for diferente da senha original
        TextField(
            value = confirmPassword,
            label = "Confirme sua senha",
            modifier = Modifier.fillMaxWidth(0.9f),
            isError = passwordMismatchError,
            isPassword = true,
            errorMessage = "As senhas não conferem",
            onValueChange = { confirmPassword = it }
        )

        // Texto de aviso extra abaixo do campo (aparece apenas se forem diferentes)
        if (passwordMismatchError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "As senhas não conferem.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp
            )
        }

        // BÔNUS: Mostrar a mensagem global do handleRegister() caso o botão seja clicado com algo errado
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { handleRegister() },
            modifier = Modifier
                .fillMaxWidth(0.9f) // Alinhado com a largura dos TextFields
                .height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF43A047),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Cadastrar",
                fontSize = 18.sp
            )
        }
    }
}