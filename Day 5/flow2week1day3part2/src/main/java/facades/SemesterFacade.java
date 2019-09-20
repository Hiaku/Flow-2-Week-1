/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Student;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nikolaj
 */
public class SemesterFacade {

    private static SemesterFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private SemesterFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static SemesterFacade getSemesterFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SemesterFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    // 1. Find all Students
    public Collection<Student> allStudents() throws Exception{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Collection<Student> students = em.createNamedQuery("Student.findAll").getResultList();
            em.getTransaction().commit();
            return students;
        } finally {
            em.close();
        }
    }
    
    // 2. Find All student with the firstname Anders
    public Collection<Student> studentsWithFirstnameAnders(String firstName) throws Exception{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Collection<Student> studentsStartingWithAnders = em.createNamedQuery("Student.findByFirstname").setParameter("firstname", firstName).getResultList();
            em.getTransaction().commit();
            return studentsStartingWithAnders;
        } finally {
            em.close();
        }
    }
    
    // 3. Insert a new Student
    public Student addStudent(Student newStudent) throws Exception{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(newStudent);
            em.getTransaction().commit();
            return newStudent;
        } finally {
            em.close();
        }
    } 
    
    // 4. Assign a new student to a semester (given the student-id and semester-id)
    
    
    // 6. Find (using JPQL) all Students in the system with the last name And
    public Collection<Student> studentsWithLastnameAnd(String lastName) throws Exception{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Collection<Student> studentsWithLastnameAnd = em.createNamedQuery("Student.findByLastname").setParameter("lastname", lastName).getResultList();
            em.getTransaction().commit();
            return studentsWithLastnameAnd;
        } finally {
            em.close();
        }
    }
    
    // 7. Find (using JPQL) the total amount of all students
    public int studentCount() throws Exception{
         EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int studentCount = em.createNamedQuery("Student.studentCount").getFirstResult();
            em.getTransaction().commit();
            return studentCount;
        } finally {
            em.close();
        }
    }
    
    // 8. Find (using JPQL)  the total number of students, for a semester given the semester name as a parameter.
    
}
