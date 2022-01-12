package com.example.todoapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Adapter.ToDoAdapter
import com.example.todoapp.Model.ToDoModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var taskList: ArrayList<ToDoModel>
    private lateinit var database: FirebaseDatabase
    private lateinit var fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.StatusBarColor)

        init()
        listeners()
    }

    private fun init(){

        database = Firebase.database("https://todoapp-a4f6f-default-rtdb.europe-west1.firebasedatabase.app/")
        fab = findViewById(R.id.fab)
        taskList = ArrayList()

        tasksAdapter = ToDoAdapter()

        recyclerView = findViewById(R.id.tasksRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tasksAdapter
        this.getAllTask()

    }

    private fun listeners(){
        fab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(AddNewTask(), AddNewTask().TAG)
                .commit()
        }
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        this.getAllTask()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getAllTask() {
        var allTaskData: ArrayList<ToDoModel> = ArrayList()
        database.reference.get().addOnSuccessListener { dataSnapshot ->
            var taskList = dataSnapshot.children.iterator()
            for (task in taskList){

                var subTaskList = task.children.iterator()
                for(subTask in subTaskList){

                    var taskToAdd = ToDoModel()
                    taskToAdd.task = subTask.child("task").value.toString()
//                    taskToAdd.status = subTask.child("status").value.toString().toInt()
//                    taskToAdd.id = subTask.child("id").value.toString().toInt()

                    allTaskData.add(taskToAdd)
                }
            }


            tasksAdapter.setTasks(allTaskData)

            tasksAdapter.notifyDataSetChanged()

        }
    }
}
