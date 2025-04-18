package com.mobile.xplore_nu.ui.screens.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.xplore_nu.R

@Composable
fun ChatPage(
    onMessageSent: (String) -> Unit, messages: List<MessageModel>, predefinedQuestions: List<String>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Messages(modifier = Modifier.weight(1f), messages)
        MessageInput(onMessageSent = onMessageSent, predefinedQuestions = predefinedQuestions)
    }
}

@Composable
fun Messages(modifier: Modifier, messageList: List<MessageModel>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = R.drawable.husky_logo),
                contentDescription = "logo",
            )
            Text("Ask me anything", fontSize = 22.sp)
        }
    } else {
        LazyColumn(modifier = modifier, reverseLayout = true) {
            items(messageList.reversed()) {
                MessageRow(it)
            }
        }
    }

}

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(
                        if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                    )
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isModel) Color.Red else Color.Gray)
                    .padding(16.dp)
            ) {
                Text(
                    messageModel.message,
                    fontWeight = FontWeight.W500,
                    color = if (isModel) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun MessageInput(
    onMessageSent: (String) -> Unit, predefinedQuestions: List<String>
) {
    var message by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(predefinedQuestions) { question ->
                QuestionChip(question) {
                    onMessageSent(question)
                    message = ""
                }
            }
        }

        // Message input row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Type your message here")
                }
            )
            IconButton(
                onClick = {
                    if (message.isNotEmpty()) {
                        onMessageSent(message.trim())
                        message = ""
                    }
                }
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun QuestionChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12),
        color = Color.Red,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.body1,
            color = Color.White
        )
    }
}