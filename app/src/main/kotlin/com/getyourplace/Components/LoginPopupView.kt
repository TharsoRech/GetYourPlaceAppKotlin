package com.getyourplace.Components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserRole
import java.io.ByteArrayOutputStream

enum class AuthMode { LOGIN, RECOVERY, REGISTER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPopupView(
    isPresented: MutableState<Boolean>,
    canClose: Boolean = false,
    auth: AuthManager
) {
    // Use var with remember so we can toggle modes like SwiftUI
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }

    // Form States
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    var base64Image by remember { mutableStateOf<String?>(null) }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            profileBitmap = bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
    }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isFormComplete = when (mode) {
        AuthMode.REGISTER -> isEmailValid && password.isNotEmpty() && name.isNotEmpty() &&
                profession.isNotEmpty() && country.isNotEmpty() &&
                bio.isNotEmpty() && base64Image != null && selectedRole != null
        AuthMode.LOGIN -> isEmailValid && password.isNotEmpty()
        AuthMode.RECOVERY -> isEmailValid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(enabled = canClose) { isPresented.value = false },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF1A1A1A)) // Dark grey background matching SwiftUI
                .clickable(enabled = false) { }
                .widthIn(max = 400.dp)
        ) {
            // Close Button Overlay (matching SwiftUI overlay alignment)
            if (canClose) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = { isPresented.value = false },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape) // Fixed here
                    ) {
                        Icon(
                            Icons.Default.Close,
                            null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Header Section
            Text(
                text = when(mode) {
                    AuthMode.REGISTER -> "Create Account"
                    AuthMode.RECOVERY -> "Reset"
                    else -> "Sign In"
                },
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = if (canClose) 0.dp else 25.dp).align(Alignment.CenterHorizontally)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .heightIn(max = if (mode == AuthMode.REGISTER) 500.dp else 250.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                if (mode == AuthMode.REGISTER) {
                    Spacer(modifier = Modifier.height(5.dp))

                    // Image Picker Section
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(80.dp).clip(CircleShape).border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.size(80.dp).background(Color.White.copy(alpha = 0.05f), CircleShape)) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        Text("Add Profile Photo", color = if (base64Image == null) Color.Cyan else Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    }

                    // Role Selection
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("I am a...", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            UserRole.values().forEach { role ->
                                RoleButton(role, selectedRole == role) { selectedRole = role }
                            }
                        }
                    }

                    CustomInputField(label = "Full Name", value = name, onValueChange = { name = it })
                    CustomInputField(label = "Profession", value = profession, onValueChange = { profession = it })
                }

                // Email Field
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    CustomInputField(label = "Email", value = email, onValueChange = { email = it })
                    if (email.isNotEmpty() && !isEmailValid) {
                        Text("Please enter a valid email address", color = Color.Red.copy(alpha = 0.8f), fontSize = 11.sp, modifier = Modifier.padding(start = 10.dp))
                    }
                }

                if (mode != AuthMode.RECOVERY) {
                    CustomInputField(label = "Password", value = password, onValueChange = { password = it }, isPassword = true)
                }

                if (mode == AuthMode.REGISTER) {
                    CustomInputField(label = "Country", value = country, onValueChange = { country = it })

                    // Bio Section (TextEditor equivalent)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("About Me", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("Required", color = Color.Cyan, fontSize = 10.sp)
                        }
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            colors = textFieldColors(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Action Buttons Section (Bottom)
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Button(
                    onClick = {
                        val profile = UserProfile(email = email, password = password, name = name, role = selectedRole)
                        auth.login(profile)
                        isPresented.value = false
                    },
                    enabled = isFormComplete,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (mode == AuthMode.REGISTER) "Register Account" else if (mode == AuthMode.RECOVERY) "Send Link" else "Login", fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // Forgot Password / Back
                    Text(
                        text = if (mode == AuthMode.RECOVERY) "Back to Login" else "Forgot Password?",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            mode = if (mode == AuthMode.RECOVERY) AuthMode.LOGIN else AuthMode.RECOVERY
                        }
                    )

                    // Toggle Register/Login
                    Text(
                        text = if (mode == AuthMode.REGISTER) "Already have an account?" else "Create Account",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            mode = if (mode == AuthMode.REGISTER) AuthMode.LOGIN else AuthMode.REGISTER
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(label: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = textFieldColors(),
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color.White.copy(alpha = 0.05f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
    focusedBorderColor = Color.White.copy(alpha = 0.2f),
    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
    focusedLabelColor = Color.Gray,
    unfocusedLabelColor = Color.Gray
)

@Composable
fun RowScope.RoleButton(role: UserRole, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
            .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .border(1.dp, if (isSelected) Color.White else Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(role.name, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
@Preview(showSystemUi = true)
@Composable
fun LoginPopupPreview() {
    val context = LocalContext.current
    val mockAuth = remember { AuthManager.mock(context, UserRole.OWNER) }
    val isPresented = remember { mutableStateOf(true) }

    MaterialTheme {
        LoginPopupView(
            isPresented = isPresented,
            canClose = true,
            auth = mockAuth
        )
    }
}
