package com.example.todoapp

import android.content.DialogInterface
import com.example.todoapp.Model.ToDoModel

interface DialogCloseListener {

    fun handleDialogClose(dialog: DialogInterface);

}