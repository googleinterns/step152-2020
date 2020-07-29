package com.google.sps.data;
import java.io.*;
import com.google.sps.data.Teacher;
import com.google.sps.data.Student;
import com.google.sps.data.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; 
import com.google.appengine.api.datastore.Entity;

public class Classroom {

    private Teacher teacher;
    private List<Student> students = new ArrayList<>();
    private List<Form> forms = new ArrayList<>();
    private String subject;
    private Key key;

    public Classroom(Teacher teacher, List<Student> students, List<Form> forms, String subject, Key key) {
        this.teacher = teacher;
        this.students = students;
        this.forms = forms;
        this.subject = subject;
        this.key = key;
    }

    // Setters
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    // Getters
    public Teacher getTeacher() {
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

    public Key getKey() {
        assert this.key.getId() != -1: "The key doesn't exist.";
        return this.key;
    }

    // Database
    public void addStudent(Student student) { 
        students.add(student);
    }

    public void addForm(Form form) {
        forms.add(form);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public boolean isStudentInClass(Student student) {
        assert students.size() > 0: "There are no students in this class!";

        if (students.lastIndexOf(student) == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Entity toDatastoreEntity(){
        Entity classroomEntity = new Entity("Classroom");
        classroomEntity.setProperty("teacher", teacher);
        classroomEntity.setProperty("students", students);
        classroomEntity.setProperty("forms", forms);
        classroomEntity.setProperty("subject", subject);
        classroomEntity.setProperty("key", key);
        return classroomEntity;
    }
}