package com.example.noteapplication.Notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.noteapplication.R
import com.example.noteapplication.Utilities.Converters

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val imageExtra = "imageExtra"
class AlarmNotification: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setLargeIcon(Converters.stringToBitmap(intent.getStringExtra(imageExtra).toString()))
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationID, builder)

    }
}