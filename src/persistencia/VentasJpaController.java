/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Pedidos;
import modelo.Entregas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Ventas;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author germa
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public VentasJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        if (ventas.getEntregasCollection() == null) {
            ventas.setEntregasCollection(new ArrayList<Entregas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidos idPedido = ventas.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedido());
                ventas.setIdPedido(idPedido);
            }
            Collection<Entregas> attachedEntregasCollection = new ArrayList<Entregas>();
            for (Entregas entregasCollectionEntregasToAttach : ventas.getEntregasCollection()) {
                entregasCollectionEntregasToAttach = em.getReference(entregasCollectionEntregasToAttach.getClass(), entregasCollectionEntregasToAttach.getIdEntrega());
                attachedEntregasCollection.add(entregasCollectionEntregasToAttach);
            }
            ventas.setEntregasCollection(attachedEntregasCollection);
            em.persist(ventas);
            if (idPedido != null) {
                idPedido.getVentasCollection().add(ventas);
                idPedido = em.merge(idPedido);
            }
            for (Entregas entregasCollectionEntregas : ventas.getEntregasCollection()) {
                Ventas oldIdVentaOfEntregasCollectionEntregas = entregasCollectionEntregas.getIdVenta();
                entregasCollectionEntregas.setIdVenta(ventas);
                entregasCollectionEntregas = em.merge(entregasCollectionEntregas);
                if (oldIdVentaOfEntregasCollectionEntregas != null) {
                    oldIdVentaOfEntregasCollectionEntregas.getEntregasCollection().remove(entregasCollectionEntregas);
                    oldIdVentaOfEntregasCollectionEntregas = em.merge(oldIdVentaOfEntregasCollectionEntregas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getIdVenta());
            Pedidos idPedidoOld = persistentVentas.getIdPedido();
            Pedidos idPedidoNew = ventas.getIdPedido();
            Collection<Entregas> entregasCollectionOld = persistentVentas.getEntregasCollection();
            Collection<Entregas> entregasCollectionNew = ventas.getEntregasCollection();
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedido());
                ventas.setIdPedido(idPedidoNew);
            }
            Collection<Entregas> attachedEntregasCollectionNew = new ArrayList<Entregas>();
            for (Entregas entregasCollectionNewEntregasToAttach : entregasCollectionNew) {
                entregasCollectionNewEntregasToAttach = em.getReference(entregasCollectionNewEntregasToAttach.getClass(), entregasCollectionNewEntregasToAttach.getIdEntrega());
                attachedEntregasCollectionNew.add(entregasCollectionNewEntregasToAttach);
            }
            entregasCollectionNew = attachedEntregasCollectionNew;
            ventas.setEntregasCollection(entregasCollectionNew);
            ventas = em.merge(ventas);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getVentasCollection().remove(ventas);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getVentasCollection().add(ventas);
                idPedidoNew = em.merge(idPedidoNew);
            }
            for (Entregas entregasCollectionOldEntregas : entregasCollectionOld) {
                if (!entregasCollectionNew.contains(entregasCollectionOldEntregas)) {
                    entregasCollectionOldEntregas.setIdVenta(null);
                    entregasCollectionOldEntregas = em.merge(entregasCollectionOldEntregas);
                }
            }
            for (Entregas entregasCollectionNewEntregas : entregasCollectionNew) {
                if (!entregasCollectionOld.contains(entregasCollectionNewEntregas)) {
                    Ventas oldIdVentaOfEntregasCollectionNewEntregas = entregasCollectionNewEntregas.getIdVenta();
                    entregasCollectionNewEntregas.setIdVenta(ventas);
                    entregasCollectionNewEntregas = em.merge(entregasCollectionNewEntregas);
                    if (oldIdVentaOfEntregasCollectionNewEntregas != null && !oldIdVentaOfEntregasCollectionNewEntregas.equals(ventas)) {
                        oldIdVentaOfEntregasCollectionNewEntregas.getEntregasCollection().remove(entregasCollectionNewEntregas);
                        oldIdVentaOfEntregasCollectionNewEntregas = em.merge(oldIdVentaOfEntregasCollectionNewEntregas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventas.getIdVenta();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getIdVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            Pedidos idPedido = ventas.getIdPedido();
            if (idPedido != null) {
                idPedido.getVentasCollection().remove(ventas);
                idPedido = em.merge(idPedido);
            }
            Collection<Entregas> entregasCollection = ventas.getEntregasCollection();
            for (Entregas entregasCollectionEntregas : entregasCollection) {
                entregasCollectionEntregas.setIdVenta(null);
                entregasCollectionEntregas = em.merge(entregasCollectionEntregas);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
