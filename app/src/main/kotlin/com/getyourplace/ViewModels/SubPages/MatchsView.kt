package com.getyourplace.ViewModels.SubPages

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.getyourplace.Components.ChatView
import com.getyourplace.Components.ConversationsListView
import com.getyourplace.Components.MatchsResidencesView
import com.getyourplace.Managers.AuthManager
import com.getyourplace.Models.Conversation

@Composable
fun MatchsView(
    authManager: AuthManager
) {
    // Estados para controle de aba e conversa ativa
    var selectedTab by remember { mutableIntStateOf(0) }
    var activeConversation by remember { mutableStateOf<Conversation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // 1. Header / Segmented Control (Estilo SwiftUI)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF1A1A1A)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF333333),
                modifier = Modifier
                    .padding(16.dp)
                    .height(40.dp)
                    .background(Color(0xFF333333), RoundedCornerShape(8.dp)),
                indicator = {}, // Remove a linha padrão do Material Design
                divider = {}    // Remove a linha divisória padrão
            ) {
                listOf("Matchs", "Chat").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(2.dp)
                            .background(
                                if (selectedTab == index) Color.White else Color.Transparent,
                                RoundedCornerShape(6.dp)
                            ),
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.Black else Color.LightGray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        // 2. Content Area com Animações de Transição
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = Triple(selectedTab, activeConversation != null, activeConversation),
                transitionSpec = {
                    // Se estiver entrando no Chat (indo para a direita)
                    if (targetState.second && !initialState.second) {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        // Transição suave entre abas Matchs/Chat
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(90))
                    }
                },
                label = "MatchsNavigation"
            ) { (tab, isChatActive, conversation) ->
                when {
                    // --- ABA 0: MATCHS ---
                    tab == 0 -> {
                        MatchsResidencesView(
                            onTabChange = { selectedTab = it },
                            onConversationActive = { activeConversation = it },
                            authManager = authManager
                        )
                    }

                    // --- ABA 1: CHAT ATIVO ---
                    tab == 1 && isChatActive && conversation != null -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Botão de Voltar para a lista de conversas
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { activeConversation = null }) {
                                    Icon(Icons.Default.ChevronLeft, "Back", tint = Color.White)
                                    Text("Back", color = Color.White)
                                }
                            }

                            // --- RESOLUÇÃO DO TYPE MISMATCH ---
                            // Convertemos a List imutável do seu Model para uma MutableStateList
                            // Isso permite que o ChatView use .add() e o Compose atualize a tela.
                            val messagesState = remember(conversation) {
                                conversation.conversationMessages.toMutableStateList()
                            }

                            ChatView(
                                title = conversation.name,
                                messages = messagesState
                            )
                        }
                    }

                    // --- ABA 1: LISTA DE CONVERSAS (PADRÃO) ---
                    else -> {
                        ConversationsListView(
                            activeConversation = activeConversation,
                            onConversationSelected = { selectedChat ->
                                activeConversation = selectedChat
                                selectedTab = 1
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Matchs View - Renter")
@Composable
fun MatchsViewPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current

    val mockAuthManager = remember {
        AuthManager.mock(context, com.getyourplace.Models.UserRole.RENTER)
    }

    // mockAuthManager.setAuthenticated(true)

    MaterialTheme {
        // Envolvemos em uma Box escura para condizer com o tema do App
        Box(modifier = Modifier.background(Color(0xFF1A1A1A))) {
            MatchsView(authManager = mockAuthManager)
        }
    }
}