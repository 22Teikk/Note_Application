package com.example.noteapplication.Activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.noteapplication.Model.Note
import com.example.noteapplication.Notification.AlarmNotification
import com.example.noteapplication.Notification.channelID
import com.example.noteapplication.Notification.imageExtra
import com.example.noteapplication.Notification.messageExtra
import com.example.noteapplication.Notification.notificationID
import com.example.noteapplication.Notification.titleExtra
import com.example.noteapplication.R
import com.example.noteapplication.Utilities.Converters
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class AddNoteActivity : AppCompatActivity() {
    private lateinit var layoutOptionColor :LinearLayout
    private var oldID: Int = -1
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    var selectedColor: String = "#333333"
    var selectedImagePath: String = ""
    private var selectedUri: String = ""
    private var isUpdate = false
    private lateinit var oldNote: Note
    var day = -1
    var year = -1
    var month = -1
    var hour = -1
    var minute = -1
    val arl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageView = result.data?.data as Uri
            if (imageView != null) {
                try {
                    var inputStream = contentResolver.openInputStream(imageView)
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    selectedImagePath = Converters.bitmapToString(bitmap)
                    binding.imageNote.setImageBitmap(bitmap)
                    binding.imageNote.visibility = View.VISIBLE
                    binding.imageRemoveImage.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        layoutOptionColor = findViewById<LinearLayout>(R.id.layoutOptionNote)
        try {
            oldNote = intent.getSerializableExtra("note") as Note
            if (oldNote != null)
                isUpdate = true
            oldID = oldNote.id
            binding.inputNoteTitle.setText(oldNote.title)
            binding.textDateTime.setText(oldNote.dateTime)
            binding.inputNoteSubTitle.setText(oldNote.subTitle)
            binding.inputNote.setText(oldNote.noteText)
            if (oldNote.dateTimeAlert != "") {
                binding.layoutTimeAlert.visibility = View.VISIBLE
                binding.textDateTimeAlert.setText(oldNote.dateTimeAlert)
            }
            if (oldNote.imagePath != "") {
                selectedImagePath = oldNote.imagePath
                binding.imageNote.setImageBitmap(Converters.stringToBitmap(oldNote.imagePath))
                binding.imageNote.visibility = View.VISIBLE
                binding.imageRemoveImage.visibility = View.VISIBLE
            }
            if (oldNote.webLink != "") {
                selectedUri = oldNote.webLink
                binding.textWebURL.setText(oldNote.webLink)
                binding.layoutWebURL.visibility = View.VISIBLE
                binding.imageRemoveURL.visibility = View.VISIBLE
            }
            if (oldNote.color != "#333333") {
                selectedColor = oldNote.color
                when (oldNote.color) {
                    "#E79797" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#D3C292" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#B6F892" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#5EC9C8" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#1594B8" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#602587" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(R.drawable.baseline_done_24)
                    }
                }
            }
            setViewSubtitleColor()
        }catch (e: Exception) {
            e.printStackTrace()
        }

        if (isUpdate == false) {
            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
            binding.textDateTime.text = formatter.format(Date())
            selectedColor = "#333333"
            selectedImagePath = ""
            selectedUri = ""
        }

        if (intent.getBooleanExtra("fromQuickAction", false)) {
            if (intent.getStringExtra("type").toString().equals("image")) {
                selectedImagePath = intent.getStringExtra("image").toString()
                binding.imageNote.visibility = View.VISIBLE
                binding.imageNote.setImageBitmap(Converters.stringToBitmap(selectedImagePath))
                binding.imageRemoveImage.visibility = View.VISIBLE
            }
            if (intent.getStringExtra("type").toString().equals("uri")) {
                selectedUri = intent.getStringExtra("uri").toString()
                binding.textWebURL.setText(selectedUri)
                binding.layoutWebURL.visibility = View.VISIBLE
                binding.imageRemoveURL.visibility = View.VISIBLE
            }
        }

        binding.imageRemoveURL.setOnClickListener {
            binding.textWebURL.setText(null)
            binding.layoutWebURL.visibility = View.GONE
            binding.imageRemoveURL.visibility = View.GONE
            selectedUri = ""
        }

        binding.imageRemoveImage.setOnClickListener {
            binding.imageNote.setImageBitmap(null)
            binding.imageNote.visibility = View.GONE
            selectedImagePath = ""
            binding.imageRemoveImage.visibility = View.GONE
        }

        binding.imageDeleteAlert.setOnClickListener {
            binding.layoutTimeAlert.visibility = View.GONE
            day = -1
            year = -1
            month = -1
            hour = -1
            minute = -1
            cancelScheduledNotification()
        }

        intiOptionColor()
        setViewSubtitleColor()
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            if (binding.inputNoteTitle.text.isEmpty())
                Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show()
            else {
                val note: Note
                if (isUpdate) {
                    note = Note(oldID,
                        binding.inputNoteTitle.text.toString(),
                        binding.textDateTime.text.toString(),
                        binding.inputNoteSubTitle.text.toString(),
                        binding.inputNote.text.toString())
                }else {
                    note = Note(0,
                        binding.inputNoteTitle.text.toString(),
                        binding.textDateTime.text.toString(),
                        binding.inputNoteSubTitle.text.toString(),
                        binding.inputNote.text.toString())
                }
                note.color = selectedColor
                if (selectedImagePath != null)
                    note.imagePath = selectedImagePath

                if (binding.layoutWebURL.visibility == View.VISIBLE)
                    note.webLink = selectedUri


                if (day != -1 && year != -1 && month != -1 && hour != -1 && minute != -1) {
                    //Add Notification
                    val permission: Array<String> =
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
                    if (ContextCompat.checkSelfPermission(
                            applicationContext,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(this, permission, notificationID)
                    } else runNotification()
                    val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month - 1)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    note.dateTimeAlert = simpleDateFormat.format(calendar.time)
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }


    private fun intiOptionColor() {
        //Click vào hiển thị more option
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutOptionColor)
        layoutOptionColor.findViewById<TextView>(R.id.textOption).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        setClickColor()

        //Add Image
        layoutOptionColor.findViewById<LinearLayout>(R.id.layoutAddImage).setOnClickListener {
            val permission: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permission, 123)
            }else selectImage()
        }

        //Add URL
        layoutOptionColor.findViewById<LinearLayout>(R.id.layoutAddURL).setOnClickListener {
            bottomSheetBehavior.state = (BottomSheetBehavior.STATE_COLLAPSED)
            showDialogURL()
        }

        //Add Time
        layoutOptionColor.findViewById<LinearLayout>(R.id.layoutAddTime).setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setClickColor() {
        layoutOptionColor.findViewById<View>(R.id.viewColor1).setOnClickListener {
            selectedColor = "#E79797"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor2).setOnClickListener {
            selectedColor = "#D3C292"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor3).setOnClickListener {
            selectedColor = "#B6F892"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor4).setOnClickListener {
            selectedColor = "#5EC9C8"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor5).setOnClickListener {
            selectedColor = "#1594B8"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor6).setOnClickListener {
            selectedColor = "#602587"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            setViewSubtitleColor()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        arl.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 && grantResults.isNotEmpty())
            selectImage()
        else if (requestCode == notificationID && grantResults.isNotEmpty())
            runNotification()
        else
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
    }

    fun cancelScheduledNotification() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)

        // Hủy lịch thông báo bằng cách sử dụng PendingIntent
        alarmManager.cancel(pendingIntent)

        // Đảm bảo rằng PendingIntent không còn sử dụng bằng cách hủy nó
        pendingIntent.cancel()
    }

    //Set background note
    private fun setViewSubtitleColor() {
        val gradientDrawable: GradientDrawable = findViewById<View>(R.id.viewSubTitle).background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedColor))
    }

    private fun showDialogURL() {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.add_url, null)
        val inputURL = dialogView.findViewById<EditText>(R.id.inputURL)
        val textAdd = dialogView.findViewById<TextView>(R.id.textAdd)
        val textCancel = dialogView.findViewById<TextView>(R.id.textCancel)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        textAdd.setOnClickListener {
            if (inputURL.text.isEmpty())
                Toast.makeText(this, "Please entering URL", Toast.LENGTH_LONG).show()
            else if(!Patterns.WEB_URL.matcher(inputURL.text).matches())
                Toast.makeText(this, "URL Invalid", Toast.LENGTH_LONG).show()
            else {
                binding.textWebURL.text = inputURL.text
                selectedUri = inputURL.text.toString()
                binding.layoutWebURL.visibility = View.VISIBLE
                binding.imageRemoveURL.visibility = View.VISIBLE
                alertDialog.dismiss()
            }
        }
        textCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentmonth = calendar.get(Calendar.MONTH)
        val currentday = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                year = selectedYear
                month = selectedMonth + 1
                day = selectedDay
                showTimePicker()
            },
            currentYear,
            currentmonth,
            currentday
        )

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { view, selectedHour, selectedMinute ->
                // Xử lý thời gian đã chọn ở đây\
                val selectedCalendar = Calendar.getInstance()
                hour = selectedHour
                minute = selectedMinute
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, month - 1)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, day)
                selectedCalendar.set(Calendar.MINUTE, minute)
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour)
                if (selectedCalendar.time >= calendar.time) {
                    val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                    binding.textDateTimeAlert.setText(simpleDateFormat.format(selectedCalendar.time))
                    binding.layoutTimeAlert.visibility = View.VISIBLE
                }else Toast.makeText(this, "Time is not valid", Toast.LENGTH_LONG).show()

            },
            currentHour,
            currentMinute,
            true // true để hiển thị 24 giờ
        )
        timePickerDialog.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun runNotification() {
        onCreateNotification()
        val intent = Intent(this, AlarmNotification::class.java)
        intent.putExtra(titleExtra, binding.inputNoteTitle.text.toString())
        intent.putExtra(messageExtra, binding.inputNoteSubTitle.text.toString())
        intent.putExtra(imageExtra, selectedImagePath)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    private fun getTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month-1, day, hour, minute, 0)
        return calendar.timeInMillis
    }

    private fun onCreateNotification() {
        val name = "Noti_Channel"
        val des = "temnsdf"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}
