package com.example.studentmanweek11

data class StudentModel(val fullName: String, val studentId: String) {
    override fun toString(): String = "$fullName \nID: $studentId"
}
