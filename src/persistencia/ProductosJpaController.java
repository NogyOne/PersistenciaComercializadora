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
import modelo.Remisionesproductos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Pedidosproductos;
import modelo.Productos;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author germa
 */
public class ProductosJpaController implements Serializable {

    public ProductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ProductosJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Productos productos) {
        if (productos.getRemisionesproductosCollection() == null) {
            productos.setRemisionesproductosCollection(new ArrayList<Remisionesproductos>());
        }
        if (productos.getPedidosproductosCollection() == null) {
            productos.setPedidosproductosCollection(new ArrayList<Pedidosproductos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Remisionesproductos> attachedRemisionesproductosCollection = new ArrayList<Remisionesproductos>();
            for (Remisionesproductos remisionesproductosCollectionRemisionesproductosToAttach : productos.getRemisionesproductosCollection()) {
                remisionesproductosCollectionRemisionesproductosToAttach = em.getReference(remisionesproductosCollectionRemisionesproductosToAttach.getClass(), remisionesproductosCollectionRemisionesproductosToAttach.getIdremisionProducto());
                attachedRemisionesproductosCollection.add(remisionesproductosCollectionRemisionesproductosToAttach);
            }
            productos.setRemisionesproductosCollection(attachedRemisionesproductosCollection);
            Collection<Pedidosproductos> attachedPedidosproductosCollection = new ArrayList<Pedidosproductos>();
            for (Pedidosproductos pedidosproductosCollectionPedidosproductosToAttach : productos.getPedidosproductosCollection()) {
                pedidosproductosCollectionPedidosproductosToAttach = em.getReference(pedidosproductosCollectionPedidosproductosToAttach.getClass(), pedidosproductosCollectionPedidosproductosToAttach.getIdpedidoProducto());
                attachedPedidosproductosCollection.add(pedidosproductosCollectionPedidosproductosToAttach);
            }
            productos.setPedidosproductosCollection(attachedPedidosproductosCollection);
            em.persist(productos);
            for (Remisionesproductos remisionesproductosCollectionRemisionesproductos : productos.getRemisionesproductosCollection()) {
                Productos oldIdProductoOfRemisionesproductosCollectionRemisionesproductos = remisionesproductosCollectionRemisionesproductos.getIdProducto();
                remisionesproductosCollectionRemisionesproductos.setIdProducto(productos);
                remisionesproductosCollectionRemisionesproductos = em.merge(remisionesproductosCollectionRemisionesproductos);
                if (oldIdProductoOfRemisionesproductosCollectionRemisionesproductos != null) {
                    oldIdProductoOfRemisionesproductosCollectionRemisionesproductos.getRemisionesproductosCollection().remove(remisionesproductosCollectionRemisionesproductos);
                    oldIdProductoOfRemisionesproductosCollectionRemisionesproductos = em.merge(oldIdProductoOfRemisionesproductosCollectionRemisionesproductos);
                }
            }
            for (Pedidosproductos pedidosproductosCollectionPedidosproductos : productos.getPedidosproductosCollection()) {
                Productos oldIdProductoOfPedidosproductosCollectionPedidosproductos = pedidosproductosCollectionPedidosproductos.getIdProducto();
                pedidosproductosCollectionPedidosproductos.setIdProducto(productos);
                pedidosproductosCollectionPedidosproductos = em.merge(pedidosproductosCollectionPedidosproductos);
                if (oldIdProductoOfPedidosproductosCollectionPedidosproductos != null) {
                    oldIdProductoOfPedidosproductosCollectionPedidosproductos.getPedidosproductosCollection().remove(pedidosproductosCollectionPedidosproductos);
                    oldIdProductoOfPedidosproductosCollectionPedidosproductos = em.merge(oldIdProductoOfPedidosproductosCollectionPedidosproductos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Productos productos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos persistentProductos = em.find(Productos.class, productos.getIdProducto());
            Collection<Remisionesproductos> remisionesproductosCollectionOld = persistentProductos.getRemisionesproductosCollection();
            Collection<Remisionesproductos> remisionesproductosCollectionNew = productos.getRemisionesproductosCollection();
            Collection<Pedidosproductos> pedidosproductosCollectionOld = persistentProductos.getPedidosproductosCollection();
            Collection<Pedidosproductos> pedidosproductosCollectionNew = productos.getPedidosproductosCollection();
            List<String> illegalOrphanMessages = null;
            for (Remisionesproductos remisionesproductosCollectionOldRemisionesproductos : remisionesproductosCollectionOld) {
                if (!remisionesproductosCollectionNew.contains(remisionesproductosCollectionOldRemisionesproductos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Remisionesproductos " + remisionesproductosCollectionOldRemisionesproductos + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Remisionesproductos> attachedRemisionesproductosCollectionNew = new ArrayList<Remisionesproductos>();
            for (Remisionesproductos remisionesproductosCollectionNewRemisionesproductosToAttach : remisionesproductosCollectionNew) {
                remisionesproductosCollectionNewRemisionesproductosToAttach = em.getReference(remisionesproductosCollectionNewRemisionesproductosToAttach.getClass(), remisionesproductosCollectionNewRemisionesproductosToAttach.getIdremisionProducto());
                attachedRemisionesproductosCollectionNew.add(remisionesproductosCollectionNewRemisionesproductosToAttach);
            }
            remisionesproductosCollectionNew = attachedRemisionesproductosCollectionNew;
            productos.setRemisionesproductosCollection(remisionesproductosCollectionNew);
            Collection<Pedidosproductos> attachedPedidosproductosCollectionNew = new ArrayList<Pedidosproductos>();
            for (Pedidosproductos pedidosproductosCollectionNewPedidosproductosToAttach : pedidosproductosCollectionNew) {
                pedidosproductosCollectionNewPedidosproductosToAttach = em.getReference(pedidosproductosCollectionNewPedidosproductosToAttach.getClass(), pedidosproductosCollectionNewPedidosproductosToAttach.getIdpedidoProducto());
                attachedPedidosproductosCollectionNew.add(pedidosproductosCollectionNewPedidosproductosToAttach);
            }
            pedidosproductosCollectionNew = attachedPedidosproductosCollectionNew;
            productos.setPedidosproductosCollection(pedidosproductosCollectionNew);
            productos = em.merge(productos);
            for (Remisionesproductos remisionesproductosCollectionNewRemisionesproductos : remisionesproductosCollectionNew) {
                if (!remisionesproductosCollectionOld.contains(remisionesproductosCollectionNewRemisionesproductos)) {
                    Productos oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos = remisionesproductosCollectionNewRemisionesproductos.getIdProducto();
                    remisionesproductosCollectionNewRemisionesproductos.setIdProducto(productos);
                    remisionesproductosCollectionNewRemisionesproductos = em.merge(remisionesproductosCollectionNewRemisionesproductos);
                    if (oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos != null && !oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos.equals(productos)) {
                        oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos.getRemisionesproductosCollection().remove(remisionesproductosCollectionNewRemisionesproductos);
                        oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos = em.merge(oldIdProductoOfRemisionesproductosCollectionNewRemisionesproductos);
                    }
                }
            }
            for (Pedidosproductos pedidosproductosCollectionOldPedidosproductos : pedidosproductosCollectionOld) {
                if (!pedidosproductosCollectionNew.contains(pedidosproductosCollectionOldPedidosproductos)) {
                    pedidosproductosCollectionOldPedidosproductos.setIdProducto(null);
                    pedidosproductosCollectionOldPedidosproductos = em.merge(pedidosproductosCollectionOldPedidosproductos);
                }
            }
            for (Pedidosproductos pedidosproductosCollectionNewPedidosproductos : pedidosproductosCollectionNew) {
                if (!pedidosproductosCollectionOld.contains(pedidosproductosCollectionNewPedidosproductos)) {
                    Productos oldIdProductoOfPedidosproductosCollectionNewPedidosproductos = pedidosproductosCollectionNewPedidosproductos.getIdProducto();
                    pedidosproductosCollectionNewPedidosproductos.setIdProducto(productos);
                    pedidosproductosCollectionNewPedidosproductos = em.merge(pedidosproductosCollectionNewPedidosproductos);
                    if (oldIdProductoOfPedidosproductosCollectionNewPedidosproductos != null && !oldIdProductoOfPedidosproductosCollectionNewPedidosproductos.equals(productos)) {
                        oldIdProductoOfPedidosproductosCollectionNewPedidosproductos.getPedidosproductosCollection().remove(pedidosproductosCollectionNewPedidosproductos);
                        oldIdProductoOfPedidosproductosCollectionNewPedidosproductos = em.merge(oldIdProductoOfPedidosproductosCollectionNewPedidosproductos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = productos.getIdProducto();
                if (findProductos(id) == null) {
                    throw new NonexistentEntityException("The productos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos productos;
            try {
                productos = em.getReference(Productos.class, id);
                productos.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Remisionesproductos> remisionesproductosCollectionOrphanCheck = productos.getRemisionesproductosCollection();
            for (Remisionesproductos remisionesproductosCollectionOrphanCheckRemisionesproductos : remisionesproductosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Productos (" + productos + ") cannot be destroyed since the Remisionesproductos " + remisionesproductosCollectionOrphanCheckRemisionesproductos + " in its remisionesproductosCollection field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Pedidosproductos> pedidosproductosCollection = productos.getPedidosproductosCollection();
            for (Pedidosproductos pedidosproductosCollectionPedidosproductos : pedidosproductosCollection) {
                pedidosproductosCollectionPedidosproductos.setIdProducto(null);
                pedidosproductosCollectionPedidosproductos = em.merge(pedidosproductosCollectionPedidosproductos);
            }
            em.remove(productos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Productos> findProductosEntities() {
        return findProductosEntities(true, -1, -1);
    }

    public List<Productos> findProductosEntities(int maxResults, int firstResult) {
        return findProductosEntities(false, maxResults, firstResult);
    }

    private List<Productos> findProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Productos.class));
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

    public Productos findProductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Productos.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Productos> rt = cq.from(Productos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
