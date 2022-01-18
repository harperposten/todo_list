package com.example.todolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        todoAdapter = TodoAdapter(mutableListOf())
        setContentView(binding.root)

        loadData()

        rvList.adapter = todoAdapter
        rvList.layoutManager = LinearLayoutManager(this)
        addButton.setOnClickListener {
            val todoTitle = addBox.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle, false)
                todoAdapter.addTodo(todo)
                addBox.text.clear()
            }
        }

        clearButton.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }

        saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        for((i, todo) in todoAdapter.getTodos().withIndex()) {
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("STRING_KEY$i", todo.title)
                putBoolean("BOOLEAN_KEY$i", todo.isChecked)
            }.apply()
        }

        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("LIST_SIZE", todoAdapter.itemCount)
        }.apply()

        Toast.makeText(this, "List saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedSize = sharedPreferences.getInt("LIST_SIZE", 0)
        if(savedSize > 0) {
            for(i in 0 until savedSize) {
                val savedString = sharedPreferences.getString("STRING_KEY$i", null).toString()
                val savedBoolean = sharedPreferences.getBoolean("BOOLEAN_KEY$i", false)
                val todo = Todo(savedString, savedBoolean)
                todoAdapter.addTodo(todo)
            }
        }
    }
}