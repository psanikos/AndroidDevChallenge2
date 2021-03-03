/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.View
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemUi(windows = window)
            MyTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun SystemUi(windows: Window) =
    MaterialTheme {

        windows.statusBarColor = Color(0xFF252525).toArgb()
        windows.navigationBarColor = MaterialTheme.colors.surface.toArgb()

        @Suppress("DEPRECATION")
        if (MaterialTheme.colors.surface.luminance() > 0.5f) {
            windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        @Suppress("DEPRECATION")
        if (MaterialTheme.colors.surface.luminance() > 0.5f) {
            windows.decorView.systemUiVisibility = windows.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

// Start building your app here!

enum class TimeUnit {
    HOUR, MINUTE, SECOND
}
fun formatDuration(seconds: Long): String = if (seconds < 60) {
    seconds.toString()
} else {
    DateUtils.formatElapsedTime(seconds)
}
@Composable
fun MyApp() {

    val progress = remember { mutableStateOf(1.0F) }
    val countdownStart = remember { mutableStateOf(10000) }
    val timeLeft = remember { mutableStateOf(10) }
    val timerRunning = remember { mutableStateOf(false) }
    val timeUnit = remember { mutableStateOf(TimeUnit.SECOND) }

    val myButton = Modifier
        .width(220.dp)
        .height(50.dp)
        .clip(RoundedCornerShape(20))

    val timer: CountDownTimer = object : CountDownTimer(countdownStart.value.toLong(), 10) {

        override fun onTick(millisUntilFinished: Long) {
            val value: Float = millisUntilFinished.toFloat() / (countdownStart.value.toFloat())
            progress.value = value
            timeLeft.value = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            cancel()
            timerRunning.value = false
            progress.value = 1F
            timeLeft.value = (countdownStart.value / 1000)
        }
    }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .height(280.dp)
                    .width(280.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = 1F, strokeWidth = 16.dp, color = Color.LightGray,
                    modifier = Modifier
                        .height(280.dp)
                        .width(280.dp)
                )
                CircularProgressIndicator(
                    progress = progress.value, strokeWidth = 16.dp, color = if (progress.value > 0.3) MaterialTheme.colors.onPrimary else Color.Red,
                    modifier = Modifier
                        .height(280.dp)
                        .width(280.dp)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        formatDuration(timeLeft.value.toLong()),
                        style = TextStyle(
                            fontSize = if (timeLeft.value >= 3600) 60.sp else 90.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            }
            if (!timerRunning.value) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                    Row(modifier = Modifier.clip(RoundedCornerShape(12))) {
                        IconButton(
                            onClick = {
                                timeUnit.value = TimeUnit.HOUR
                            },
                            modifier = Modifier
                                .background(color = if (timeUnit.value == TimeUnit.HOUR) Color.Gray else Color.LightGray)

                        ) {
                            Text(
                                "H",
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        IconButton(
                            onClick = {
                                timeUnit.value = TimeUnit.MINUTE
                            },
                            modifier = Modifier
                                .background(color = if (timeUnit.value == TimeUnit.MINUTE) Color.Gray else Color.LightGray)
                        ) {
                            Text(
                                "M",
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        IconButton(
                            onClick = {
                                timeUnit.value = TimeUnit.SECOND
                            },
                            modifier = Modifier
                                .background(color = if (timeUnit.value == TimeUnit.SECOND) Color.Gray else Color.LightGray)
                        ) {
                            Text(
                                "S",
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                    Row(modifier = Modifier.clip(RoundedCornerShape(12))) {
                        IconButton(
                            onClick = {

                                val value = if (timeUnit.value == TimeUnit.SECOND) 1000 else if (timeUnit.value == TimeUnit.MINUTE) 60000 else 3600000

                                if (countdownStart.value > value) {
                                    countdownStart.value = countdownStart.value - value
                                    timeLeft.value = (countdownStart.value / 1000)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Remove, contentDescription = null, tint = Color.White)
                        }
                        IconButton(
                            onClick = {
                                val value = if (timeUnit.value == TimeUnit.SECOND) 1000 else if (timeUnit.value == TimeUnit.MINUTE) 60000 else 3600000

                                countdownStart.value = countdownStart.value + value
                                timeLeft.value = (countdownStart.value / 1000)
                            }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
            Button(
                onClick = {
                    when (timerRunning.value) {
                        true -> {
                            timer.cancel()
                            timerRunning.value = false
                            progress.value = 1F
                            timeLeft.value = (countdownStart.value / 1000)
                        }
                        false -> {
                            timer.start()
                            timerRunning.value = true
                        }
                    }
                },
                modifier = myButton,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (timerRunning.value) Color.Red else Color.DarkGray,

                    contentColor = Color.White
                )
            ) {
                Text(
                    if (timerRunning.value) "Stop" else "Begin",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                )
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
