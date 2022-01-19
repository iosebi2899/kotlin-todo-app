package com.example.todoapp.Adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.AddNewTask
import com.example.todoapp.MainActivity
import com.example.todoapp.Model.ToDoModel
import com.example.todoapp.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ToDoAdapter: RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    private var todoList: ArrayList<ToDoModel> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var activity: MainActivity




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox = view.findViewById(R.id.todoCheckBox)
    }


    fun getItem(position: Int): ToDoModel {

        return todoList[position]
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo, viewGroup, false)

        return ViewHolder(view)
    }

    @SuppressLint("HardwareIds")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        database = Firebase.database("https://todoapp-a4f6f-default-rtdb.europe-west1.firebasedatabase.app/")
        val item = todoList[position]
        viewHolder.task.text = item.task
        viewHolder.task.isChecked = toBool(item.status)
        viewHolder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                database.reference.child(item.deviceId).child(item.id.toString()).child("status").setValue(1)
            }else{
                database.reference.child(item.deviceId).child(item.id.toString()).child("status").setValue(0)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(todoList: ArrayList<ToDoModel>){
        this.todoList = todoList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        var item = todoList[position]
        database.reference.child(item.deviceId).child(item.id.toString()).removeValue()
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int){
        activity = MainActivity()
        val item = todoList.get(position)
        val bundle = Bundle()
        bundle.putInt("id", item.id )
        bundle.putString("task", item.task)
        val fragment = AddNewTask()
        fragment.arguments = bundle
        fragment.show(activity.supportFragmentManager, AddNewTask().TAG)
    }



    private fun toBool(n: Int): Boolean{
        return n != 0
    }

    override fun getItemCount() = todoList.size
}