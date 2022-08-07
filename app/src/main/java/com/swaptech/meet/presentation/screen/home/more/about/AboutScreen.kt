package com.swaptech.meet.presentation.screen.home.more.about

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AboutScreen() {
    Column {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Alpha version 1.0.0"
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "You have entered the pre-testing program."
        )
        Text(
            text = "By continuing to use this application, you acknowledge that you are aware of: \n" +
                    "1. Your data created in the application can be PERMANENTLY deleted without any notice from the developer. \n" +
                    "2. The developer DOES NOT GUARANTEE the stable operation of the application. Based on this, failures of a different nature may occur in the application. If you notice any problems, you can contact the developer directly or through the Feedback screen. \n" +
                    "3. You will not use this application directly or indirectly to commit illegal acts. \n" +
                    "4. Contact the developer directly for an update. You may need to uninstall the old version. \n" +
                    "5. You may be the one to catch the app before it's known. \n" +
                    "6. I am very grateful to you that you agreed to test my application, I hope you will be satisfied using it."
        )
    }
}