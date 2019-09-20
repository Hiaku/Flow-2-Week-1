package tester;

import entity.Address;
import entity.Customer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Nikolaj
 */
public class Tester2 {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        Customer cust = new Customer("Gert", "Nat");
        Address add = new Address("Lyngby bla bla", "Vejle");
        Address add2 = new Address("Lyngby bla bla", "lyngby");
        cust.addAddress(add);
        cust.addAddress(add2);
//        cust.setAddress(add);
//        add.setCustomer(cust);
        try {
            em.getTransaction().begin();
            em.persist(cust);
//            em.persist(add);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
//        em = emf.createEntityManager();
//        Customer found = em.find(Customer.class, cust.getId());
//        System.out.println("Address: " + found.getAddress().getCity());
    }
}
