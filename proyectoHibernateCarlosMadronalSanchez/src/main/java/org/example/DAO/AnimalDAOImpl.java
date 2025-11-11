// org/example/DAO/AnimalDAOImpl.java
package org.example.DAO;

import org.example.Util.HibernateUtil;
import org.example.entities.Animal;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AnimalDAOImpl implements AnimalDAO {

    @Override
    public List<Animal> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Animal", Animal.class).list();
        }
    }

    @Override
    public Animal findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Animal.class, id);
        }
    }

    @Override
    public List<Animal> findByNombre(String nombre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Animal WHERE nombre = :nombre", Animal.class)
                    .setParameter("nombre", nombre)
                    .list();
        }
    }

    @Override
    public List<Animal> findByEspecie(String especie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Animal WHERE especie = :especie", Animal.class)
                    .setParameter("especie", especie)
                    .list();
        }
    }

    @Override
    public Animal create(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(animal);
            tx.commit();
            return animal;
        }
    }

    @Override
    public Animal update(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(animal);
            tx.commit();
            return animal;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Animal animal = session.get(Animal.class, id);
            if (animal != null) {
                session.remove(animal);
                tx.commit();
                return true;
            }
            return false;
        }
    }
}