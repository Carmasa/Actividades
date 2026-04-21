package com.example.loginsimple

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAlumnos = findViewById<Button>(R.id.btn_show_alumnos)
        val btnProfesores = findViewById<Button>(R.id.btn_show_profesores)

        // Cargar por defecto alumnos
        replaceFragment(ListFragment.newInstance("alumnos"))

        btnAlumnos.setOnClickListener {
            replaceFragment(ListFragment.newInstance("alumnos"))
        }

        btnProfesores.setOnClickListener {
            replaceFragment(ListFragment.newInstance("profesores"))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}