package com.example.signalclone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signalclone.data.repository.SignalRepository
import com.example.signalclone.ui.theme.BorderLight
import com.example.signalclone.ui.theme.SignalBlue
import com.example.signalclone.ui.theme.SurfaceLight
import com.example.signalclone.ui.theme.TextPrimary
import com.example.signalclone.ui.theme.TextSecondary

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var usernameNumber by remember { mutableStateOf("01") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var pendingVerification by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (pendingVerification) {
                // Verification Step
                Text(
                    text = "Verify email address",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the code we sent to ${email.lowercase()}",
                    fontSize = 15.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                TextButton(
                    onClick = { pendingVerification = false },
                    modifier = Modifier.testTag("wrong_email_button")
                ) {
                    Text("Wrong email?", color = SignalBlue, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    placeholder = { Text("Enter verification code (e.g. 123456)", color = TextSecondary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("verification_code_input")
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        SignalRepository.signUp(firstName, lastName, username, usernameNumber, email)
                        onSignUpSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("verify_submit_button")
                ) {
                    Text(
                        text = "Verify & Complete",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            } else {
                // Form step
                Text(
                    text = "Sign up",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Create an account to get started",
                    fontSize = 15.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = { Text("First name", color = TextSecondary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_first_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = { Text("Last name", color = TextSecondary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_last_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Username with 2-digit number badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Username", color = TextSecondary) },
                        singleLine = true,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SignalBlue,
                            unfocusedBorderColor = BorderLight,
                            focusedContainerColor = SurfaceLight,
                            unfocusedContainerColor = SurfaceLight
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("signup_username")
                    )

                    OutlinedTextField(
                        value = usernameNumber,
                        onValueChange = { if (it.length <= 2) usernameNumber = it },
                        placeholder = { Text("01", color = TextSecondary) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SignalBlue,
                            unfocusedBorderColor = BorderLight,
                            focusedContainerColor = SurfaceLight,
                            unfocusedContainerColor = SurfaceLight
                        ),
                        modifier = Modifier
                            .width(76.dp)
                            .testTag("signup_username_number")
                    )
                }

                Text(
                    text = "Usernames are always paired with a set of numbers.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 4.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email address", color = TextSecondary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_email")
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = TextSecondary) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SignalBlue,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceLight,
                        unfocusedContainerColor = SurfaceLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("signup_password")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (firstName.isBlank() || username.isBlank() || email.isBlank()) {
                            errorMessage = "Please complete all required fields."
                        } else {
                            errorMessage = null
                            pendingVerification = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("signup_continue_button")
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "Sign in",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SignalBlue,
                        modifier = Modifier
                            .clickable { onNavigateToSignIn() }
                            .padding(4.dp)
                            .testTag("signup_to_signin_link")
                    )
                }
            }
        }
    }
}
