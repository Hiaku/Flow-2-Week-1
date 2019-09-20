package facade;

import entity.Address;
import entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Nikolaj
 */
public class CustomerFacade {
    Customer cust;
    
    public Customer getCustomer(int id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        try {
            cust = em.find(Customer.class, id);
        } finally {
            em.close();
        }
        return cust;
    }
    
    public List<Customer> getCustomers(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Customer> query
                    = em.createQuery("Select c from Customer c", Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Customer addCustomer(Customer cust){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(cust);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return cust;
    }
    
    public Customer deleteCustomer(int id){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        Customer deletedCust;
        try{
            deletedCust = em.find(Customer.class, id);
            TypedQuery<Customer> query
                    = em.createQuery("DELETE FROM Customer c WHERE c.id = :id", Customer.class).setParameter("id", id);
        }finally{
            em.close();
        }
        return deletedCust;
    }
    
    public Customer editCustomer(Customer cust){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        cust.setFirstName(this.cust.getFirstName());
        cust.setLastName(this.cust.getLastName());
        cust.addAddress((Address) this.cust.getAddresses());
        try{
            em.getTransaction().begin();
            em.persist(cust);
            em.getTransaction().commit();
        }finally{
            em.close();
        }
        return this.cust;
    }
}
