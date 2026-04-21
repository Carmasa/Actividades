package com.example.loginsimple

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ListFragment : Fragment() {

    companion object {
        private const val ARG_TYPE = "type"

        fun newInstance(type: String): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putString(ARG_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val tvTitle = view.findViewById<TextView>(R.id.tv_fragment_title)
        val lvItems = view.findViewById<ListView>(R.id.lv_items)

        val type = arguments?.getString(ARG_TYPE) ?: "alumnos"
        tvTitle.text = "Listado de $type"

        val data = loadDataFromDb(type)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, data)
        lvItems.adapter = adapter

        return view
    }

    private fun loadDataFromDb(type: String): List<String> {
        val dbHelper = DatabaseHelper(requireContext())
        val db = dbHelper.readableDatabase
        val table = if (type == "alumnos") DatabaseHelper.TABLE_ALUMNOS else DatabaseHelper.TABLE_PROFESORES
        
        val cursor: Cursor = db.query(table, arrayOf(DatabaseHelper.COL_NOMBRE, DatabaseHelper.COL_APELLIDO), null, null, null, null, null)
        val list = mutableListOf<String>()
        
        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NOMBRE))
            val apellido = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_APELLIDO))
            list.add("$nombre $apellido")
        }
        cursor.close()
        db.close()
        return list
    }
}