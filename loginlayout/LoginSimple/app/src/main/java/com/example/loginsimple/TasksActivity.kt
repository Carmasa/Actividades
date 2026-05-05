package com.example.loginsimple

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TasksActivity : AppCompatActivity() {

    private lateinit var adapter: TasksAdapter
    private val taskList = mutableListOf<Task>()
    private var taskCounter = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tasks_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val profName = intent.getStringExtra("PROF_NAME") ?: "Profesor"
        findViewById<TextView>(R.id.tv_tasks_title).text = "Tareas de $profName"

        val rvTasks = findViewById<RecyclerView>(R.id.rv_tasks)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add_task)

        // Setup RecyclerView
        adapter = TasksAdapter(taskList, 
            onTaskClicked = { task, position ->
                // La lógica de completar ya está en el adapter según lo solicitado
            },
            onTaskDeleted = { position ->
                taskList.removeAt(position)
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
            }
        )

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = adapter

        fabAdd.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nueva Tarea")

        val input = EditText(this)
        input.hint = "Descripción de la tarea"
        builder.setView(input)

        builder.setPositiveButton("Añadir") { _, _ ->
            val taskName = input.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = Task(taskCounter, taskName, false, "Normal")
                taskList.add(newTask)
                adapter.notifyItemInserted(taskList.size - 1)
                taskCounter++
            } else {
                Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}