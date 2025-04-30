package com.groupec.cleanarchitecturesampleapp.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.Primary
import com.groupec.cleanarchitecturesampleapp.core.designsystem.theme.White

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    text: String,
    textcolor: Color = White,
    containerColor: Color = Primary,
    border: BorderStroke? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    style: TextStyle? = null,
    onClick: () -> Unit
) {
    MaterialTheme {
        Button(
            modifier = modifier,
            shape = RoundedCornerShape(5.dp),
            border = border,
            enabled = enabled,
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = textcolor
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            if (isLoading) {
                AppLoadingScreen(Modifier.wrapContentSize(), color = textcolor)
            } else {
                if (style != null) {
                    Text(text = text, style = style)
                } else {
                    Text(text = text)
                }

            }
        }
    }
}

@Composable
fun UnderlinedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = text,
            color = Primary,
            style = TextStyle(
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}


@Preview
@Composable
fun ButtonsPreview() {
    Column {
        DefaultButton(onClick = { /*TODO*/ }, text = "Validate")
        UnderlinedTextButton(text = "Voir plus") {

        }
    }
}