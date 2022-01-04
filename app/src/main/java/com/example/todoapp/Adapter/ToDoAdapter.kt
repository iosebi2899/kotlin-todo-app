package com.example.todoapp.Adapter

import android.annotation.SuppressLint
import android.os.Bundle
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

    private lateinit var  todoList: ArrayList<ToDoModel>
    private lateinit var database: FirebaseDatabase
    private lateinit var activity: MainActivity

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var task: CheckBox = view.findViewById(R.id.todoCheckBox)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        database = Firebase.database("https://todoapp-a4f6f-default-rtdb.europe-west1.firebasedatabase.app/")
        val item = todoList[position]
        viewHolder.task.text = item.task
        viewHolder.task.isChecked = toBool(item.status)
        viewHolder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                database.reference.child(item.id.toString()).child("status").setValue(true)
            }else{
                database.reference.child(item.id.toString()).child("status").setValue(false)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(todoList: ArrayList<ToDoModel>){
        this.todoList = todoList
        notifyDataSetChanged()
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