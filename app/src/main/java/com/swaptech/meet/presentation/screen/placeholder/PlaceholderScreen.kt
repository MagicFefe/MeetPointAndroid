package com.swaptech.meet.presentation.screen.placeholder

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swaptech.meet.R

@Composable
fun PlaceholderScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp),
                text = stringResource(id = R.string.meet_point),
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun PlaceholderScreen_Preview() {
    PlaceholderScreen()
}
