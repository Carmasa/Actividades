package com.example.loginsimple

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskClicked: (Task, Int) -> Unit,
    private val onTaskDeleted: (Int) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_task_name)
        val tvStatus: TextView = view.findViewById(R.id.tv_task_status)
        val vPriority: View = view.findViewById(R.id.v_priority_indicator)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete_task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvName.text = task.name
        
        if (task.isCompleted) {
            holder.tvStatus.text = "Completada"
            holder.vPriority.setBackgroundColor(Color.GREEN)
        } else {
            holder.tvStatus.text = "Pendiente"
            // Color Naranja para pendiente como se pidió, o según prioridad
            holder.vPriority.setBackgroundColor(Color.parseColor("#FFA500")) 
        }

        holder.itemView.setOnClickListener {
            if (!task.isCompleted) {
                task.isCompleted = true
                Toast.makeText(holder.itemView.context, "Completando... ${task.name}", Toast.LENGTH_SHORT).show()
                notifyItemChanged(position)
            }
        }

        holder.btnDelete.setOnClickListener {
            onTaskDeleted(position)
        }
    }

    override fun getItemCount() = tasks.size
}