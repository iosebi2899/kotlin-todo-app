//package com.example.todoapp
//
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.graphics.drawable.Drawable
//import android.view.View
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.RecyclerView
//import com.example.todoapp.Adapter.ToDoAdapter
//
//class RecyclerItemTouchHelper(adapter: ToDoAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//
//    private lateinit var icon: Drawable
//    private lateinit var background: ColorDrawable
//
//    override fun onMove(
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        target: RecyclerView.ViewHolder
//    ): Boolean {
//        return false
//    }
//
//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        var position = viewHolder.adapterPosition
//        if(direction == ItemTouchHelper.LEFT){
//            adapter.deleteItem(position)
//        }else{
//            adapter.editItem(position)
//        }
//    }
//
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//
//        var itemView: View = viewHolder.itemView
//        var backgroundColorOffset = 20
//
//
//        if(dX>0){
//            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.edit_icon)!!
//            background = ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.teal_700))
//        }else{
//            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_delete_24)!!
//            background = ColorDrawable(Color.RED)
//        }
//        var iconMargin = (itemView.height - icon.intrinsicHeight) / 2
//        var iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
//        var iconBottom = iconTop + icon.intrinsicHeight
//
//        if(dX > 0){
//            var iconLeft = itemView.left + iconMargin
//            var iconRight = itemView.left + iconMargin + icon.intrinsicWidth
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundColorOffset, itemView.bottom)
//        }else if(dX<0){
//            var iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
//            var iconRight = itemView.right - iconMargin
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            background.setBounds(itemView.right + dX.toInt() - backgroundColorOffset, itemView.top, itemView.right , itemView.bottom)
//        }else {
//            background.setBounds(0, 0, 0, 0)
//        }
//
//        background.draw(c)
//        icon.draw(c)
//    }
//}