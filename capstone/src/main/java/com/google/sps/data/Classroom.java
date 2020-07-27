package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Teacher;
import com.google.sps.data.Student;
import com.google.sps.data.Form;
import java.util.ArrayList;
import java.util.List;


// TODO: When auth is complete change all strings to integers for access tokens

public class Classroom {

    String teacher;
    List<Student> students = new ArrayList<>();
    List<Form> forms = new ArrayList<>();
    String subject;

    public Classroom(String teacher, List<Student> students, List<Form> forms, String subject) {
        this.teacher = teacher;
        this.students = students;
        this.forms = forms;
        this.subject = subject;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addForm(Form form) {
        forms.add(form);
    }

    public boolean isStudentInClass(Student student) {

        if (students.lastIndexOf(student) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getTeacher() {
        return this.teacher;
    }

    public String getSubject() {
        return this.subject;
    }

    public List<Student> getAllStudents() {
        return this.students;
    }

    public List<Form> getForms() {
        return this.forms;
    }
}