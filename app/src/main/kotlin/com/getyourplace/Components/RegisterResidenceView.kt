package com.getyourplace.Views

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.getyourplace.Components.*
import com.getyourplace.Models.Residence

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterResidenceView(
    residenceToEdit: Residence? = null,
    onSave: (Residence) -> Unit,
    onBack: () -> Unit
) {
    // --- State Management ---
    var name by remember { mutableStateOf(residenceToEdit?.name ?: "") }
    var description by remember { mutableStateOf(residenceToEdit?.description ?: "") }
    var address by remember { mutableStateOf(residenceToEdit?.address ?: "") }
    var location by remember { mutableStateOf(residenceToEdit?.location ?: "") }
    var type by remember { mutableStateOf(residenceToEdit?.type ?: "House") }
    var priceText by remember { mutableStateOf(residenceToEdit?.price?.toString() ?: "") }

    var rooms by remember { mutableIntStateOf(residenceToEdit?.numberOfRooms ?: 1) }
    var beds by remember { mutableIntStateOf(residenceToEdit?.numberOfBeds ?: 1) }
    var baths by remember { mutableIntStateOf(residenceToEdit?.baths ?: 1) }

    var hasGarage by remember { mutableStateOf(residenceToEdit?.hasGarage ?: false) }
    var numberOfGarages by remember { mutableIntStateOf(residenceToEdit?.numberOfGarages ?: 0) }
    var acceptPets by remember { mutableStateOf(residenceToEdit?.acceptPets ?: false) }
    var isPublished by remember { mutableStateOf(residenceToEdit?.isPublished ?: false) }

    // Media State
    var mainImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        mainImageUri = it
    }

    val propertyTypes = listOf("House", "Apartment", "Villa", "Studio")
    val customDark = Color(0xFF1A1A1A)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (residenceToEdit == null) "New Property" else "Edit Property", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = customDark)
            )
        },
        containerColor = customDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Media Section ---
            SectionHeader("Media")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.opacity(0.05f))
                    .clickable { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (mainImageUri != null) {
                    AsyncImage(
                        model = mainImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
                        Text("Select Main Photo", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            // --- Property Info ---
            SectionHeader("Property Information")
            CustomInputField(label = "Property Name", text = name, onTextChange = { name = it })
            CustomDescriptionField(label = "Description", text = description, onTextChange = { description = it })
            CustomInputField(label = "Address", text = address, onTextChange = { address = it })
            CustomInputField(label = "Location (City, State)", text = location, onTextChange = { location = it })

            CustomDropdownField(
                label = "Type",
                value = type,
                options = propertyTypes,
                onOptionSelected = { type = it }
            )

            // --- Pricing & Details ---
            SectionHeader("Pricing & Details")
            CustomInputField(label = "Price (Monthly)", text = priceText, onTextChange = { priceText = it })

            ComposeStepper(label = "Rooms", value = rooms, onValueChange = { rooms = it })
            ComposeStepper(label = "Beds", value = beds, onValueChange = { beds = it })
            ComposeStepper(label = "Baths", value = baths, onValueChange = { baths = it })

            ComposeToggle(label = "Has Garage", checked = hasGarage, onCheckedChange = { hasGarage = it })
            if (hasGarage) {
                ComposeStepper(label = "Garages", value = numberOfGarages, onValueChange = { numberOfGarages = it })
            }

            ComposeToggle(label = "Accept Pets", checked = acceptPets, onCheckedChange = { acceptPets = it }, icon = Icons.Default.Pets)
            ComposeToggle(label = "Visible to Public", checked = isPublished, onCheckedChange = { isPublished = it }, activeColor = Color.Green)

            // --- Save Button ---
            Button(
                onClick = { /* Call your save logic here similar to SwiftUI save() */ },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotEmpty()
            ) {
                Text(if (residenceToEdit == null) "Register Property" else "Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(title, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
}

@Composable
fun ComposeStepper(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            IconButton(onClick = { if (value > 0) onValueChange(value - 1) }) {
                Icon(Icons.Default.RemoveCircleOutline, contentDescription = null, tint = Color.White)
            }
            Text("$value", color = Color.White, fontWeight = FontWeight.Bold)
            IconButton(onClick = { onValueChange(value + 1) }) {
                Icon(Icons.Default.AddCircleOutline, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun ComposeToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, activeColor: Color = Color.Blue) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (icon != null) Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Text(label, color = Color.White)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = activeColor)
        )
    }
}

// Extension for Color opacity shortcut
fun Color.opacity(alpha: Float) = this.copy(alpha = alpha)

// --- PREVIEWS ---

@Preview(showBackground = true, name = "New Property Form")
@Composable
fun RegisterResidencePreview() {
    MaterialTheme {
        RegisterResidenceView(
            residenceToEdit = null,
            onSave = { println("Saved residence: ${it.name}") },
            onBack = { println("Back pressed") }
        )
    }
}

@Preview(showBackground = true, name = "Edit Property Form")
@Composable
fun EditResidencePreview() {
    // Creating a dummy object to simulate "Edit" mode
    val context = LocalContext.current
    // Use the mock function
    val mockResidence = Residence.mock(context)

    MaterialTheme {
        RegisterResidenceView(
            residenceToEdit = mockResidence,
            onSave = { println("Updated residence: ${it.name}") },
            onBack = { println("Back pressed") }
        )
    }
}