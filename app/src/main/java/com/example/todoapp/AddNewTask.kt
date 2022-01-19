package com.example.todoapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.provider.Settings
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.todoapp.Model.ToDoModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class AddNewTask: BottomSheetDialogFragment() {
    lateinit var newTaskText: EditText
    lateinit var newTaskSave: Button
    lateinit var id: Number
    lateinit var deviceId: String

    var database = Firebase.database("https://todoapp-a4f6f-default-rtdb.europe-west1.firebasedatabase.app/")
    val TAG = "ActionBottomDialog"

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)

        deviceId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)
    }

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.new_todo, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        return view
    }


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTaskText = view.findViewById(R.id.newTaskText)
        newTaskSave = view.findViewById(R.id.newTaskSave)


        var isUpdate = false
        var bundle = arguments

        if(bundle != null){
            isUpdate = true

            var task = bundle.getString("task")
            newTaskText.setText(task)
            if (task != null && task.isNotEmpty()) {
                newTaskSave.setTextColor(Color.parseColor("#3c4043"))
            }
        }
        newTaskText.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString().isEmpty()){
                    newTaskSave.isEnabled = false
                    newTaskSave.setTextColor(Color.GRAY)
                }else{
                    newTaskSave.isEnabled = true
                    newTaskSave.setTextColor(Color.parseColor("#80CBC4"))
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        newTaskSave.setOnClickListener{

            val id = bundle?.getInt("id").toString()
            val text = newTaskText.text.toString()

            if(isUpdate){
                database.reference.child(deviceId).child(id).child("task").setValue(text)
            }else{
                val id = Random.nextInt(0, 99999)
                val task = ToDoModel()
                task.task = text
                task.status = 0
                task.id = id
                database.reference.child(deviceId).child(id.toString()).setValue(task)
            }

            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val currentActivity = activity

        if(currentActivity is DialogCloseListener){
            currentActivity.handleDialogClose(dialog)
        }
    }

}