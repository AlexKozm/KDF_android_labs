package com.example.notifyme

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.example.notifyme.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val NOTIFICATION_ID = 0
private const val ACTION_UPDATE_NOTIFICATION =
    "com.example.notifyme.ACTION_UPDATE_NOTIFICATION"
private const val NOTIFICATION_DELETED =
    "com.example.notifyme.NOTIFICATION_DELETED"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotifyManager: NotificationManager
    private lateinit var mNotifyButton: Button
    private lateinit var mUpdateButton: Button
    private lateinit var mCancelButton: Button

    private val mReceiver = NotificationReceiver()
    private val mFilterUpdate = IntentFilter(ACTION_UPDATE_NOTIFICATION)
    private val mFilterCancel = IntentFilter(NOTIFICATION_DELETED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        mNotifyManager = createNotificationChannel()
        addListeners()
        setNotificationButtonState(
            isNotifyEnabled = true,
            isUpdateEnabled = false,
            isCancelEnabled = false
        )
        setContentView(binding.root)
        lifecycleScope.launch {
            registerReceiver(mReceiver, mFilterCancel)
            registerReceiver(mReceiver, mFilterUpdate)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private fun addListeners() {
        mNotifyButton = binding.notify.apply {
            setOnClickListener {
                sendNotification()
            }
        }
        mUpdateButton = binding.update.apply {
            setOnClickListener {
                updateNotification()
            }
        }
        mCancelButton = binding.cancel.apply {
            setOnClickListener {
                cancelNotification()
            }
        }
    }

    private fun createNotificationChannel(): NotificationManager {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notification from Mascot"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(notificationChannel)
        }
        return manager
    }

    fun updateNotification() {
        setNotificationButtonState(
            isNotifyEnabled = false,
            isUpdateEnabled = false,
            isCancelEnabled = true
        )
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notifyBuilder = getNotificationBuilder()
        notifyBuilder
            .setStyle(NotificationCompat
                .BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"))
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun cancelNotification() {
        setNotificationButtonState(
            isNotifyEnabled = true,
            isUpdateEnabled = false,
            isCancelEnabled = false
        )
        mNotifyManager.cancel(NOTIFICATION_ID)
    }


    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val deleteIntent = Intent(NOTIFICATION_DELETED)
        val deletePendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            deleteIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    private fun setNotificationButtonState(isNotifyEnabled: Boolean,
                                           isUpdateEnabled: Boolean,
                                           isCancelEnabled: Boolean) {
        mNotifyButton.isEnabled = isNotifyEnabled
        mUpdateButton.isEnabled = isUpdateEnabled
        mCancelButton.isEnabled = isCancelEnabled
    }


    private fun sendNotification() {
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent
            .getBroadcast(this, NOTIFICATION_ID, updateIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)
        setNotificationButtonState(
            isNotifyEnabled = false,
            isUpdateEnabled = true,
            isCancelEnabled = true
        )
        val builder = getNotificationBuilder().apply {
            addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent)
        }
        mNotifyManager.notify(NOTIFICATION_ID, builder.build())
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            println(p1?.action)
            when (p1?.action) {
                ACTION_UPDATE_NOTIFICATION -> updateNotification()
                NOTIFICATION_DELETED -> setNotificationButtonState(
                        isNotifyEnabled = true,
                        isUpdateEnabled = false,
                        isCancelEnabled = false
                    )
            }
        }
    }
}