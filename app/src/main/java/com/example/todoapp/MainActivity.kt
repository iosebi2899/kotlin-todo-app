package com.example.todoapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
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
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var taskList: ArrayList<ToDoModel>
    private lateinit var database: FirebaseDatabase
    private lateinit var fab: FloatingActionButton
    private lateinit var icon: Drawable
    private lateinit var background: ColorDrawable


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

        itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {


                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var position = viewHolder.adapterPosition
                    if(direction == ItemTouchHelper.LEFT){
                        tasksAdapter.deleteItem(position)
                    }else{
                        val item = tasksAdapter.getItem(position)
                        val bundle = Bundle()
                        bundle.putInt("id", item.id )
                        bundle.putString("task", item.task)
                        val fragment = AddNewTask()
                        fragment.arguments = bundle
                        fragment.show(this@MainActivity.supportFragmentManager, AddNewTask().TAG)
                    }
                }
                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                    var itemView: View = viewHolder.itemView
                    var backgroundColorOffset = 20


                    if(dX>0){
                        icon = ContextCompat.getDrawable(this@MainActivity,R.drawable.edit_icon)!!
                        background = ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.teal_700))
                    }else{
                        icon = ContextCompat.getDrawable(this@MainActivity,R.drawable.ic_baseline_delete_24)!!
                        background = ColorDrawable(Color.RED)
                    }
                    var iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                    var iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                    var iconBottom = iconTop + icon.intrinsicHeight

                    if(dX > 0){
                        var iconLeft = itemView.left + iconMargin
                        var iconRight = itemView.left + iconMargin + icon.intrinsicWidth
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundColorOffset, itemView.bottom)
                    }else if(dX<0){
                        var iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                        var iconRight = itemView.right - iconMargin
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        background.setBounds(itemView.right + dX.toInt() - backgroundColorOffset, itemView.top, itemView.right , itemView.bottom)
                    }else {
                        background.setBounds(0, 0, 0, 0)
                    }

                    background.draw(c)
                    icon.draw(c)
                }
            })
        itemTouchHelper.attachToRecyclerView(recyclerView)

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
                    taskToAdd.status = subTask.child("status").value.toString().toInt()
                    taskToAdd.id = subTask.child("id").value.toString().toInt()
                    allTaskData.add(taskToAdd)
                }
            }

            tasksAdapter.setTasks(allTaskData)
            tasksAdapter.notifyDataSetChanged()

        }
    }
}
