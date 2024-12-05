package com.example.remindernotificationapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем NotificationChannel для Android 8+ (API 26+)
        createNotificationChannel()

        setContent {
            ReminderNotificationApp()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for reminder notifications"
            }
            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun ReminderScreen() {
    var message by remember { mutableStateOf("") }
    var timeInMinutes by remember { mutableStateOf(1f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Введите текст напоминания") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Выберите время (минуты): ${timeInMinutes.toInt()}")

        Slider(
            value = timeInMinutes,
            onValueChange = { timeInMinutes = it },
            valueRange = 1f..60f,
            steps = 58,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    delay((timeInMinutes * 60 * 1000).toLong())
                    sendNotification(context, message)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Установить напоминание")
        }
    }
}

private fun sendNotification(context: Context, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Проверяем разрешение для Android 13+
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Нет разрешения на отправку уведомлений", Toast.LENGTH_SHORT).show()
            return
        }
    }

    val notification = NotificationCompat.Builder(context, "reminder_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Напоминание")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(1, notification)
    }
}
