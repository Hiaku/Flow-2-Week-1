package tester;

import entity.Customer2;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Nikolaj
 */
public class Tester {
    public static void main(String[] args) {
        Customer2 cust = new Customer2("Kurt Wonnegut");
        cust.addHobby("football");
        cust.addHobby("tennis");
        cust.addHobby("golf");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(cust);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        em = emf.createEntityManager();
        Customer2 found = em.find(Customer2.class, cust.getId());
    }
}
