// org/example/DAO/PersonaDAOImpl.java
package org.example.DAO;

import org.example.Util.HibernateUtil;
import org.example.entities.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PersonaDAOImpl implements PersonaDAO {

    @Override
    public List<Persona> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Persona", Persona.class).list();
        }
    }

    @Override
    public Persona findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Persona.class, id);
        }
    }

    @Override
    public Persona findByDNI(String dni) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Persona WHERE DNI = :dni", Persona.class)
                    .setParameter("dni", dni)
                    .uniqueResult();
        }
    }

    @Override
    public Persona create(Persona persona) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(persona);
            tx.commit();
            return persona;
        }
    }

    @Override
    public Persona update(Persona persona) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(persona);
            tx.commit();
            return persona;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Persona persona = session.get(Persona.class, id);
            if (persona != null) {
                session.remove(persona);
                tx.commit();
                return true;
            }
            return false;
        }
    }
}