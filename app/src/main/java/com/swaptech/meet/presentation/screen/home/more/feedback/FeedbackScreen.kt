package com.swaptech.meet.presentation.screen.home.more.feedback

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.swaptech.meet.R
import com.swaptech.meet.domain.feedback.model.Feedback
import com.swaptech.meet.presentation.MAX_FEEDBACK_MESSAGE_LENGTH
import com.swaptech.meet.presentation.utils.VerticalScrollableContent
import com.swaptech.meet.presentation.utils.network_error_handling.handleError

@Composable
fun FeedbackScreen(
    userId: String,
    feedbackScreenViewModel: FeedbackScreenViewModel,
    nestedNavController: NavHostController
) {
    var message by rememberSaveable {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    VerticalScrollableContent(
        scrollState = scrollState,
        stickyBottomContent = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    when {
                        message.isEmpty() || message.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.feedback_cannot_be_empty,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        message.length > MAX_FEEDBACK_MESSAGE_LENGTH -> {
                            Toast.makeText(
                                context,
                                R.string.feedback_is_too_long,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            val feedback = Feedback(
                                userId = userId,
                                message = message
                            )
                            feedbackScreenViewModel.sendFeedback(
                                feedback,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        R.string.thanks_for_feedback,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    nestedNavController.popBackStack()
                                },
                                onFail = { _, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                },
                                onError = { error ->
                                    handleError(
                                        error,
                                        onConnectionFault = {
                                            Toast.makeText(
                                                context,
                                                R.string.no_internet_connection,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onSocketTimeout = {
                                            Toast.makeText(
                                                context,
                                                R.string.remote_services_unavailable,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.send))
            }
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(10.dp),
            value = message,
            placeholder = {
                Text(text = stringResource(id = R.string.enter_your_feedback))
            },
            maxLines = 10,
            onValueChange = { input ->
                if (input.length <= MAX_FEEDBACK_MESSAGE_LENGTH) {
                    message = input
                }
            }
        )
    }
}
