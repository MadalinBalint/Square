package com.mendelin.square.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mendelin.square.R

val loraFamily = FontFamily(
    Font(R.font.lora_regular, FontWeight.Normal),
    Font(R.font.lora_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.lora_semibold, FontWeight.SemiBold),
    Font(R.font.lora_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.lora_bold, FontWeight.Bold),
    Font(R.font.lora_bolditalic, FontWeight.Bold, FontStyle.Italic),
)

val latoFamily = FontFamily(
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_bolditalic, FontWeight.Bold, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        fontFamily = loraFamily
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        fontFamily = loraFamily
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        fontFamily = loraFamily
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        fontFamily = loraFamily
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        fontFamily = loraFamily
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        fontFamily = loraFamily
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        fontFamily = latoFamily
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        fontFamily = latoFamily
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        fontFamily = latoFamily
    )
)
