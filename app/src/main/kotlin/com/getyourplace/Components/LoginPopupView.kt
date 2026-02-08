package com.getyourplace.Components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.UserProfile
import com.getyourplace.Models.UserRole
import java.io.ByteArrayOutputStream

enum class AuthMode { LOGIN, RECOVERY, REGISTER }

@Composable
fun LoginPopupView(
    isPresented: MutableState<Boolean>,
    canClose: Boolean = false,
    auth: AuthManager
) {
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

    // Image Picker Launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            profileBitmap = bitmap

            // Convert to Base64
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }
    }

    // Validation Logic
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
                .background(Color(0xFF1A1A1A))
                .clickable(enabled = false) { } // Prevent clicks from closing when touching the box
        ) {
            // Header
            Text(
                text = when(mode) {
                    AuthMode.REGISTER -> "Create Account"
                    AuthMode.RECOVERY -> "Reset"
                    else -> "Sign In"
                },
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp).align(Alignment.CenterHorizontally)
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .heightIn(max = if (mode == AuthMode.REGISTER) 500.dp else 300.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (mode == AuthMode.REGISTER) {
                    Spacer(modifier = Modifier.height(20.dp))

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
                                modifier = Modifier.size(80.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier.size(80.dp).background(Color.White.copy(alpha = 0.1f), CircleShape)) {
                                Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                        Text("Add Profile Photo", color = if (base64Image == null) Color.Cyan else Color.Gray, fontSize = 12.sp)
                    }

                    // Role Selection
                    Text("I am a...", color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        UserRole.values().forEach { role ->
                            RoleButton(role, selectedRole == role) { selectedRole = role }
                        }
                    }

                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = profession, onValueChange = { profession = it }, label = { Text("Profession") }, modifier = Modifier.fillMaxWidth())
                }

                // Common Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                    isError = email.isNotEmpty() && !isEmailValid
                )
                if (email.isNotEmpty() && !isEmailValid) {
                    Text("Invalid email", color = Color.Red, fontSize = 10.sp, modifier = Modifier.padding(start = 10.dp))
                }

                if (mode != AuthMode.RECOVERY) {
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
                }

                if (mode == AuthMode.REGISTER) {
                    OutlinedTextField(value = country, onValueChange = { country = it }, label = { Text("Country") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                }
            }

            // Action Buttons
            Button(
                onClick = {
                    val profile = UserProfile(email = email, password = password, name = name, role = selectedRole)
                    auth.login(profile)
                    isPresented.value = false
                },
                enabled = isFormComplete,
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (mode == AuthMode.REGISTER) "Register Account" else if (mode == AuthMode.RECOVERY) "Send Link" else "Login")
            }
        }
    }
}

@Composable
fun RowScope.RoleButton(role: UserRole, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable { onClick() }
            .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(role.name, color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}