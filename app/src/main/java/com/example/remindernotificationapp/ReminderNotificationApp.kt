package com.example.remindernotificationapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReminderNotificationApp() {
    val context = LocalContext.current
    var reminderText by remember { mutableStateOf("") }
    var sliderValue by remember { mutableStateOf(1f) }
    val scope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    // Проверяем наличие канала уведомлений при первом запуске
    LaunchedEffect(context) {
        createNotificationChannel(context)
    }

    // Запрашиваем разрешение на отправку уведомлений
    LaunchedEffect(permissionState) {
        if (permissionState.status.isGranted) {
            // Разрешение получено, продолжим
        } else {
            // Разрешение не получено, показываем сообщение
            Toast.makeText(context, "Пожалуйста, предоставьте разрешение на уведомления", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = reminderText,
            onValueChange = { reminderText = it },
            label = { Text("Введите текст напоминания") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Установите время (минуты): ${sliderValue.toInt()}")

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 1f..60f,
            steps = 59,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (permissionState.status.isGranted) {
                val delayMillis = sliderValue.toInt() * 60 * 1000L
                scope.launch {
                    delay(delayMillis)
                    sendNotification(context, reminderText)
                }
            } else {
                permissionState.launchPermissionRequest() // Запросить разрешение
            }
        }) {
            Text("Установить напоминание")
        }
    }
}

// Функция для создания канала уведомлений
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "reminder_channel"
        val channelName = "Напоминания"
        val channelDescription = "Канал для уведомлений о напоминаниях"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@SuppressLint("MissingPermission")
private fun sendNotification(context: Context, message: String) {
    val notification = NotificationCompat.Builder(context, "reminder_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Напоминание")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    NotificationManagerCompat.from(context).notify(1, notification)
}