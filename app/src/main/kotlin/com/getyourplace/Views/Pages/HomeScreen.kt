package com.getyourplace.Views.Pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.R
import com.getyourplace.Components.ArrowButton
import com.getyourplace.Persistence.ItemRepository

@Composable
fun HomeScreen(
    itemRepository: ItemRepository,
    onNavigateToHome: () -> Unit // Parâmetro necessário para a navegação
) {
    // Equivalent to NavigationStack + ZStack em SwiftUI
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.dream_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Main Content Overlay
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Branding Section
            Column(
                modifier = Modifier.padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.get_your_place_icon),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Get Your Place",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Ajuste se a imagem de fundo for muito escura
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Description Box
            Box(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp)
            ) {
                Text(
                    text = "Your Real Estate Partner Anytime, Anywhere. Find the Perfect Place That Fits your Lifestyle.",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ArrowButton com a ação de navegação injetada
            ArrowButton(
                title = "Find Your Own Place",
                onClick = { onNavigateToHome() }, // Aciona o callback de navegação
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(bottom = 40.dp)
                    .fillMaxWidth()
            )
        }
    }
}

// --- Preview para o Android Studio ---
@androidx.compose.ui.tooling.preview.Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    // Para o preview, passamos uma função vazia {} e um repositório nulo ou mock
    // Se o ItemRepository precisar de DAO real, você precisará mockar aqui.
    /* HomeScreen(
        itemRepository = ...,
        onNavigateToHome = {}
    )
    */
}