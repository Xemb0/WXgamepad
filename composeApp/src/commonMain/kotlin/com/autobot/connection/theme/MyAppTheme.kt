package com.autobot.connection.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import com.launcher.arclauncher.compose.theme.MyAppColors
import com.launcher.arclauncher.compose.theme.MyAppShapes
import com.launcher.arclauncher.compose.theme.MyAppSizes
import com.launcher.arclauncher.compose.theme.MyAppThemeColors
import com.launcher.arclauncher.compose.theme.MyAppThemeShapes
import com.launcher.arclauncher.compose.theme.MyAppThemeSizes
import com.launcher.arclauncher.compose.theme.MyAppThemeTypo
import com.launcher.arclauncher.compose.theme.MyAppTypo
import com.launcher.arclauncher.compose.theme.body
import com.launcher.arclauncher.compose.theme.labelLarge
import com.launcher.arclauncher.compose.theme.labelMedium
import com.launcher.arclauncher.compose.theme.labelSmall
import com.launcher.arclauncher.compose.theme.myPrimary
import com.launcher.arclauncher.compose.theme.myPrimaryDark
import com.launcher.arclauncher.compose.theme.mySecodary
import com.launcher.arclauncher.compose.theme.mySecodaryDark
import com.launcher.arclauncher.compose.theme.myText
import com.launcher.arclauncher.compose.theme.myTextDark
import com.launcher.arclauncher.compose.theme.mytertiary
import com.launcher.arclauncher.compose.theme.mytertiaryDark
import com.launcher.arclauncher.compose.theme.titleLarge
import com.launcher.arclauncher.compose.theme.titleMedium
import com.launcher.arclauncher.compose.theme.titleSmall

private val DarkColorScheme = MyAppColors(
    primary = myPrimaryDark,
    secondary = mySecodaryDark,
    tertiary = mytertiaryDark,
    myText = myTextDark
)

private val LightColorScheme = MyAppColors(
    primary = myPrimary,
    secondary = mySecodary,
    tertiary = mytertiary,
    myText = myText
)

private val shape = MyAppShapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(16.dp),
    container = RoundedCornerShape(16.dp),
    button = RoundedCornerShape(50)
)

private val size = MyAppSizes(
    xsmall = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 32.dp,
    xlarge = 64.dp,
    xxlarge = 128.dp
)

private val typo = MyAppTypo(
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    body = body,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall
)

@Composable
fun MyAppThemeComposable(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

//    // Using the new ripple API
//    val rippleIndication = ripple(
//        bounded = true,  // Set to false for an unbounded ripple
//        color = Color.Gray,  // Specify ripple color
//        radius = 24.dp  // Optional ripple radius
//    )

    CompositionLocalProvider(
        MyAppThemeColors provides colorScheme,
        MyAppThemeShapes provides shape,
        MyAppThemeSizes provides size,
//        LocalIndication provides rippleIndication,
        MyAppThemeTypo provides typo,
        content = content
    )
}

object MyAppTheme {
    val colors: MyAppColors
        @Composable
        get() = MyAppThemeColors.current
    val shapes: MyAppShapes
        @Composable
        get() = MyAppThemeShapes.current
    val sizes: MyAppSizes
        @Composable
        get() = MyAppThemeSizes.current
    val typography: MyAppTypo
        @Composable
        get() = MyAppThemeTypo.current
}
