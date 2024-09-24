package com.autobot.connection
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.autobot.connection.models.User
import connection.composeapp.generated.resources.Res
import connection.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val isButtonEnabled by viewModel.isProcessing.collectAsState()
    val currentUser by viewModel.currentUserId.collectAsState()


    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick = { viewModel.onSignInClick() },
        isProcessing = isProcessing,
        currentUser = currentUser,
        isError = emailError || passwordError,
        onSignOut = viewModel::onSignOut
    )

}
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    isProcessing: Boolean,
    currentUser: User?,
    isError: Boolean,
    onSignOut: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        val finalModifier = if (width >= 780.dp) modifier.width(400.dp) else modifier.fillMaxWidth()

        Column(
            modifier = finalModifier
                .padding(16.dp)
                .fillMaxSize()
                .background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(Color.Cyan),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp) // Set the height for the image
                )
            }

            // Spacer to push buttons to the bottom

            // Buttons Section at the bottom
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = onSignInClick
                ) {
                    Text("Continue with Google")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = onSignInClick
                ) {
                    Text("Sign in with Email")
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))

                if (isProcessing) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = onSignInClick
                    ) {
                        Text("Log in")
                    }
                }
            }
        }
    }
}
