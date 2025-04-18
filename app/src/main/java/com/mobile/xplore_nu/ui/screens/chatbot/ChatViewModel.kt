package com.mobile.xplore_nu.ui.screens.chatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mobile.xplore_nu.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
) : ViewModel() {

    val generativeModel =
        GenerativeModel(modelName = "gemini-2.0-flash", apiKey = BuildConfig.GEMINI_API_KEY)

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }

    val predefinedQuestions = listOf(
        "What programs does the university offer?",
        "Where is the admissions office located?",
        "What are the housing options for students?",
        "Is there a gym or fitness center on campus?",
        "What scholarships and financial aid are available?",
    )

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val chat = generativeModel.startChat(

                history = buildList {
                    add(
                        content("user") {
                            text(
                                """
                    You are a Northeastern university tour guide assistant for the boston campus. Your name is Paws
                    Only answer questions about university life, academics, campus facilities, admission process, or campus events and general information about the university.
                    
                    If the user asks about anything unrelated to the university, do not try to answer.
                    Instead, always respond with exactly this message:
                    
                    "I'm here to help with questions about the university and campus tours. You can ask me about admissions, facilities, campus life, or related topics!"
                    
                    Never attempt to answer off-topic questions.
                    """.trimIndent()
                            )
                        }
                    )

                    addAll(
                        messageList.map {
                            content(it.role) { text(it.message) }
                        }.toList()
                    )
                }

            )
            messageList.add(MessageModel(message, "user"))
            val response = chat.sendMessage(message)
            messageList.add(MessageModel(response.text.toString(), "model"))

        }
    }
}

data class MessageModel(
    val message: String,
    val role: String
)