package com.example.isometric

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.isometric.ui.theme.IsometricTheme
import kotlin.math.log2
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IsometricTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var isoValue by remember { mutableFloatStateOf(0f) }
    var shutterSpeed by remember { mutableFloatStateOf(0f) }
    var aperture by remember { mutableFloatStateOf(0f) }

//    var show by remember { mutableStateOf(false) }

    val apertures = getApertures()
    val apertureValue = apertures[aperture.toInt()]
    val sliderRangeAperture = apertures.size - 1;

    val shutterSpeeds = getShutterSpeeds()
    val ssVal = shutterSpeeds[shutterSpeed.toInt()]
    val sliderRangeSS = shutterSpeeds.size - 1;

    val isoList = getIsoList()
    val iso = isoList[isoValue.toInt()]
    val isoRange = isoList.size - 1

    val ev = log2(((100F * apertureValue * apertureValue) / (iso * (1F / ssVal)))).roundToInt()
    val evDescr = getEvDescr(ev)

    val colors = SliderDefaults.colors(
        thumbColor = Color.Black,
        activeTrackColor = Color.Black,
        activeTickColor = Color.Black,
        inactiveTrackColor = Color.Black,
        inactiveTickColor = Color.Black,
        disabledThumbColor = Color.Black,
        disabledActiveTrackColor = Color.Black,
        disabledActiveTickColor = Color.Black,
        disabledInactiveTrackColor = Color.Black,
        disabledInactiveTickColor = Color.Black,
    )

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ISO ${iso.toInt()}", modifier = modifier
            )

            Slider(
                steps = isoRange - 1,
                valueRange = 0f..isoRange.toFloat(),
                value = isoValue,
                colors = colors,
                onValueChange = { isoValue = it },
            )

            Text(text = apertureValue.toString())

            Slider(steps = sliderRangeAperture - 1,
                valueRange = 0f..sliderRangeAperture.toFloat(),
                value = aperture,
                colors = colors,
                onValueChange = { aperture = it })

            Text(text = "SS: $ssVal")

            Slider(steps = sliderRangeSS - 1,
                valueRange = 0f..sliderRangeSS.toFloat(),
                value = shutterSpeed,
                colors = colors,
                onValueChange = { shutterSpeed = it })

            Text(text = "EV: $ev")

            Text(text = "\n")

            EvCompensation(value = 0.0)

        }
    }

//    if (show) {
//        Box(
//            contentAlignment = Alignment.BottomCenter, modifier = Modifier.padding(32.dp)
//        ) {
//            Text(
//                text = evDescr,
////            style = TextStyle(color = Color(red = 0x00, green = 0x00, blue = 0x00, alpha = 0x50))
//            )
//
//            Text(text = getEvEmoji(ev))
//        }
//    }
}

@Composable
private fun EvCompensation(value: Double) {
    val indicatorStr = "⁻2.1.0.1.2⁺"

    Row() {
        Text(text = indicatorStr, fontSize = 32.sp, fontFamily = FontFamily.Monospace)
    }

    val indicatorRange = indicatorStr.length

//            -2.5 to 2.5
//            0 to 5
    val adaptedValue = when (value + 2.5) {
        0.0 -> 0
        0.5 -> 1
        1.0 -> 2
        1.5 -> 3
        2.0 -> 4
        2.5 -> 5
        3.0 -> 6
        3.5 -> 7
        4.0 -> 8
        4.5 -> 9
        5.0 -> 10
        else -> -100
    }

    Row() {
        for (i in 0..<indicatorRange) {
            EvItem(i == adaptedValue)
        }
    }
}

@Composable
private fun EvItem(filled: Boolean = false) {
    Box(modifier = Modifier
        .padding(2.dp)
        .width(16.dp)
        .height(24.dp)
        .let { if (filled) it.background(Color.Black) else it.background(Color.Transparent) }

    ) {

    }
}

@Composable
private fun getIsoList() = floatArrayOf(100F, 160F, 200F, 400F, 500F, 800F, 1600F, 3200F)

@Composable
private fun getApertures() =
    floatArrayOf(0.95F, 1.2F, 1.4F, 1.8F, 2.0F, 2.8F, 3.5F, 4F, 5.6F, 8F, 11F, 16F, 22F)

@Composable
private fun getShutterSpeeds() = arrayOf(
    2000, 1500, 1000, 750, 500, 350, 250, 180, 125, 90, 60, 45, 30, 20, 15, 10, 8, 6, 4, 3, 2, 1
).reversedArray()

@Composable
private fun getEvEmoji(ev: Int) = when (ev) {
    10 -> "\uD83C\uDF04"
    11 -> "\uD83C\uDF06"
    12 -> "☁\uFE0F"
    13 -> "\uD83C\uDF25"
    14 -> "⛅\uFE0F"
    15 -> "☀\uFE0F"
    16 -> "\uD83D\uDE0E"

    else -> ""
}


@Composable
private fun getEvDescr(ev: Int) = when (ev) {
    -7 -> "Deep star field or the Milky Way"
    -6 -> "Night under starlight only or the Aurora Borealis"
    -5 -> "Night under crescent moon or the Aurora Borealis"
    -4 -> "Night under half moon"
    -3 -> "Night under full moon and away from city lights"
    -2 -> "Night snowscape under full moon and away from city lights"
    -1 -> "Blue hour or dim ambient lighting"
    0 -> "Dim ambient artificial lighting"
    1 -> "Distant view of a lit skyline"
    2 -> "Under lightning (with time exposure) or a total lunar eclipse"
    3 -> "Fireworks (with time exposure)"
    4 -> "Candle-lit close-ups, Christmas lights, floodlight buildings, bright street lamps"
    5 -> "Home interiors at night, fairs and amusement parks"
    6 -> "Brightly lit home interiors at night, fairs and amusement parks"
    7 -> "Bottom of a rainforest canopy, or along brightly-lit night-time streets; Floodlit indoor sports areas or stadiums, stage shows, circuses"
    8 -> "Store windows, campfires, bonfires, ice shows; Floodlit indoor sports areas or stadiums, and interiors with bright fluorescent."
    9 -> "Landscapes, city skylines 10 minutes after sunset, neon lights"
    10 -> "Landscapes and skylines immediately after sunset"
    11 -> "Sunsets. Subject to deep shade"
    12 -> "Open shade or heavy overcast"
    13 -> "Cloudy-bright light, no shadows"
    14 -> "Weak hazy sun, rainbows, soft shadows"
    15 -> "Bright or hazy sun, clear sky"
    16 -> "Bright daylight on sand or snow"
    17 -> "Very bright artificial lighting"
    18 -> "Very bright artificial lighting"
    19 -> "Very bright artificial lighting"
    20 -> "Extremely bright artificial lighting"

    else -> ""
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IsometricTheme {
        Greeting()
    }
}