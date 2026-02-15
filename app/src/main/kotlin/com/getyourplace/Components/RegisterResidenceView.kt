package com.getyourplace.Views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.scale
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
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterResidenceView(
    residenceToEdit: Residence? = null,
    onSave: (Residence) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val MAX_PHOTOS = 12

    // --- State Management ---
    var name by remember { mutableStateOf(residenceToEdit?.name ?: "") }
    var description by remember { mutableStateOf(residenceToEdit?.description ?: "") }
    var address by remember { mutableStateOf(residenceToEdit?.address ?: "") }
    var location by remember { mutableStateOf(residenceToEdit?.location ?: "") }
    var type by remember { mutableStateOf(residenceToEdit?.type ?: "House") }
    var priceText by remember { mutableStateOf(residenceToEdit?.price?.toString() ?: "") }
    var sqftText by remember { mutableStateOf(residenceToEdit?.squareFootage?.toString() ?: "") }

    var rooms by remember { mutableIntStateOf(residenceToEdit?.numberOfRooms ?: 1) }
    var beds by remember { mutableIntStateOf(residenceToEdit?.numberOfBeds ?: 1) }
    var baths by remember { mutableIntStateOf(residenceToEdit?.baths ?: 1) }

    var hasGarage by remember { mutableStateOf(residenceToEdit?.hasGarage ?: false) }
    var numberOfGarages by remember { mutableIntStateOf(residenceToEdit?.numberOfGarages ?: 0) }
    var acceptPets by remember { mutableStateOf(residenceToEdit?.acceptPets ?: false) }
    var isPublished by remember { mutableStateOf(residenceToEdit?.isPublished ?: false) }

    // --- Media State ---
    var mainImageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var existingGalleryBase64 by remember { mutableStateOf(residenceToEdit?.galleryImagesBase64 ?: emptyList()) }

    // --- Image Helpers ---
    fun uriToBase64(uri: Uri): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            // Using NO_WRAP is cleaner for data transmission
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
            val bytes = outputStream.toByteArray()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) { "" }
    }

    // MANDATORY: Coil needs the data prefix to render Base64 strings
    fun formatBase64(base64: String): String {
        return if (base64.isEmpty()) "" else "data:image/jpeg;base64,$base64"
    }

    val mainLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) mainImageUri = uri
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(MAX_PHOTOS)) { uris ->
        if (uris.isNotEmpty()) galleryUris = uris.take(MAX_PHOTOS)
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
            SectionHeader("Media")

            // --- Main Photo Logic ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .clickable { mainLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                val modelToDisplay: Any? = when {
                    mainImageUri != null -> mainImageUri
                    !residenceToEdit?.mainImageBase64.isNullOrEmpty() -> formatBase64(residenceToEdit!!.mainImageBase64)
                    residenceToEdit?.imageRes != null -> residenceToEdit.imageRes
                    else -> null
                }

                if (modelToDisplay != null) {
                    AsyncImage(
                        model = modelToDisplay,
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

            // --- Gallery Logic ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Gallery (Max $MAX_PHOTOS)", color = Color.Gray, fontSize = 12.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                                .clickable { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Collections, contentDescription = null, tint = Color.White)
                        }
                    }

                    if (galleryUris.isNotEmpty()) {
                        items(galleryUris) { uri ->
                            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        }
                    } else if (residenceToEdit != null) {
                        // Show Base64 Gallery
                        items(existingGalleryBase64) { base64 ->
                            AsyncImage(model = formatBase64(base64), contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        }
                        // Show Resource-based Gallery (from Mocks)
                        items(residenceToEdit.galleryImageRes.filterNotNull()) { resId ->
                            AsyncImage(model = resId, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        }
                    }
                }
            }

            // --- Form Fields ---
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

            SectionHeader("Pricing & Details")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomInputField(label = "Price (Monthly)", text = priceText, onTextChange = { priceText = it })
                }
                Box(modifier = Modifier.weight(1f)) {
                    CustomInputField(label = "Sq Footage", text = sqftText, onTextChange = { sqftText = it })
                }
            }

            ComposeStepper(label = "Rooms", value = rooms, onValueChange = { rooms = it })
            ComposeStepper(label = "Beds", value = beds, onValueChange = { beds = it })
            ComposeStepper(label = "Baths", value = baths, onValueChange = { baths = it })

            ComposeToggle(label = "Has Garage", checked = hasGarage, onCheckedChange = { hasGarage = it })
            if (hasGarage) {
                ComposeStepper(label = "Garages", value = numberOfGarages, onValueChange = { numberOfGarages = it })
            }

            ComposeToggle(label = "Accept Pets", checked = acceptPets, onCheckedChange = { acceptPets = it }, icon = Icons.Default.Pets)
            ComposeToggle(label = "Visible to Public", checked = isPublished, onCheckedChange = { isPublished = it }, activeColor = Color.Green)

            Button(
                onClick = {
                    val finalPrice = priceText.toDoubleOrNull() ?: 0.0
                    val finalSqft = sqftText.toDoubleOrNull() ?: 0.0

                    val updatedMainImage = if (mainImageUri != null) uriToBase64(mainImageUri!!) else residenceToEdit?.mainImageBase64 ?: ""
                    val updatedGallery = if (galleryUris.isNotEmpty()) galleryUris.map { uriToBase64(it) } else existingGalleryBase64

                    val residence = (residenceToEdit?.copy(
                        name = name,
                        description = description,
                        address = address,
                        location = location,
                        type = type,
                        price = finalPrice,
                        squareFootage = finalSqft,
                        numberOfRooms = rooms,
                        numberOfBeds = beds,
                        baths = baths,
                        hasGarage = hasGarage,
                        numberOfGarages = if (hasGarage) numberOfGarages else 0,
                        acceptPets = acceptPets,
                        isPublished = isPublished,
                        mainImageBase64 = updatedMainImage,
                        galleryImagesBase64 = updatedGallery
                    ) ?: Residence(
                        name = name,
                        description = description,
                        address = address,
                        location = location,
                        type = type,
                        price = finalPrice,
                        squareFootage = finalSqft,
                        numberOfRooms = rooms,
                        numberOfBeds = beds,
                        baths = baths,
                        hasGarage = hasGarage,
                        numberOfGarages = if (hasGarage) numberOfGarages else 0,
                        acceptPets = acceptPets,
                        isPublished = isPublished,
                        mainImageBase64 = updatedMainImage,
                        galleryImagesBase64 = updatedGallery
                    ))
                    onSave(residence)
                },
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

@Composable fun SectionHeader(title: String) {
    Text(title, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
}

@Composable fun ComposeStepper(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
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
fun ComposeToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    activeColor: Color = Color.Green // You can change this to match your accent color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp), // Fixed height for consistent vertical alignment
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Custom styling for the Switch to make it pop against the dark background
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                // Colors when ON
                checkedThumbColor = Color.White,
                checkedTrackColor = activeColor,
                checkedBorderColor = activeColor,

                // Colors when OFF
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f),
                uncheckedBorderColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.scale(0.8f) // Makes the switch look more "iOS-sleek" and less bulky
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
    val context = LocalContext.current
    val mockResidence = Residence.mock(context)

    MaterialTheme {
        RegisterResidenceView(
            residenceToEdit = mockResidence,
            onSave = { println("Updated residence: ${it.name}") },
            onBack = { println("Back pressed") }
        )
    }
}