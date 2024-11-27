package com.example.studentmanweek11

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )
    private lateinit var adapter: ArrayAdapter<StudentModel>

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val fullName = result.data?.getStringExtra("fullName") ?: return@registerForActivityResult
            val studentId = result.data?.getStringExtra("studentId") ?: return@registerForActivityResult
            val originalId = result.data?.getStringExtra("originalId") ?: return@registerForActivityResult

            val updatedStudent = StudentModel(fullName, studentId)

            // If the originalId exists, update that student; otherwise, it's a new student
            val existingIndex = students.indexOfFirst { it.studentId == originalId }
            if (existingIndex != -1) {
                // Update the existing student if the ID matches
                students[existingIndex] = updatedStudent
            } else {
                // Avoid duplicate entries and add the new student
                if (students.none { it.studentId == studentId }) {
                    students.add(updatedStudent)
                } else {
                    Toast.makeText(this, "A student with ID $studentId already exists.", Toast.LENGTH_SHORT).show()
                    return@registerForActivityResult
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, students)
        listView.adapter = adapter
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterContextMenuInfo
        val student = students[info.position]

        when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, AddNewActivity::class.java).apply {
                    putExtra("fullName", student.fullName)
                    putExtra("studentId", student.studentId)
                    putExtra("originalId", student.studentId) // Pass the original ID for reference
                }
                launcher.launch(intent)
                return true
            }
            R.id.action_remove -> {
                students.removeAt(info.position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "${student.fullName} removed.", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.action_add) {
//            val intent = Intent(this, AddNewActivity::class.java)
//            launcher.launch(intent) // Launch the AddStudentActivity for new student
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, AddNewActivity::class.java)
                launcher.launch(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}