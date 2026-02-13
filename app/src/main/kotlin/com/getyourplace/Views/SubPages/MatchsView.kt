package com.getyourplace.Views.SubPages

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
    var selectedTab by remember { mutableIntStateOf(0) }
    var activeConversation by remember { mutableStateOf<Conversation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
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
                indicator = {},
                divider = {}
            ) {
                listOf("Matches", "Chat").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(0.dp)
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
        
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = Triple(selectedTab, activeConversation != null, activeConversation),
                transitionSpec = {
                    if (targetState.second && !initialState.second) {
                        slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                    } else {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(90))
                    }
                },
                label = "MatchsNavigation"
            ) { (tab, isChatActive, conversation) ->
                when {
                    tab == 0 -> {
                        MatchsResidencesView(
                            onTabChange = { selectedTab = it },
                            onConversationActive = { activeConversation = it },
                            authManager = authManager
                        )
                    }

                    tab == 1 && isChatActive && conversation != null -> {
                        Column(modifier = Modifier.fillMaxSize()) {
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

                            val messagesState = remember(conversation) {
                                conversation.conversationMessages.toMutableStateList()
                            }

                            ChatView(
                                title = conversation.name,
                                messages = messagesState
                            )
                        }
                    }
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
        Box(modifier = Modifier.background(Color(0xFF1A1A1A))) {
            MatchsView(authManager = mockAuthManager)
        }
    }
}