/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Pedidos;
import modelo.Pedidosproductos;
import modelo.Productos;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author germa
 */
public class PedidosproductosJpaController implements Serializable {

    public PedidosproductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public PedidosproductosJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedidosproductos pedidosproductos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidos idPedido = pedidosproductos.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdPedido());
                pedidosproductos.setIdPedido(idPedido);
            }
            Productos idProducto = pedidosproductos.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                pedidosproductos.setIdProducto(idProducto);
            }
            em.persist(pedidosproductos);
            if (idPedido != null) {
                idPedido.getPedidosproductosCollection().add(pedidosproductos);
                idPedido = em.merge(idPedido);
            }
            if (idProducto != null) {
                idProducto.getPedidosproductosCollection().add(pedidosproductos);
                idProducto = em.merge(idProducto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedidosproductos pedidosproductos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidosproductos persistentPedidosproductos = em.find(Pedidosproductos.class, pedidosproductos.getIdpedidoProducto());
            Pedidos idPedidoOld = persistentPedidosproductos.getIdPedido();
            Pedidos idPedidoNew = pedidosproductos.getIdPedido();
            Productos idProductoOld = persistentPedidosproductos.getIdProducto();
            Productos idProductoNew = pedidosproductos.getIdProducto();
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdPedido());
                pedidosproductos.setIdPedido(idPedidoNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                pedidosproductos.setIdProducto(idProductoNew);
            }
            pedidosproductos = em.merge(pedidosproductos);
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getPedidosproductosCollection().remove(pedidosproductos);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getPedidosproductosCollection().add(pedidosproductos);
                idPedidoNew = em.merge(idPedidoNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getPedidosproductosCollection().remove(pedidosproductos);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getPedidosproductosCollection().add(pedidosproductos);
                idProductoNew = em.merge(idProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidosproductos.getIdpedidoProducto();
                if (findPedidosproductos(id) == null) {
                    throw new NonexistentEntityException("The pedidosproductos with id " + id + " no longer exists.");
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
            Pedidosproductos pedidosproductos;
            try {
                pedidosproductos = em.getReference(Pedidosproductos.class, id);
                pedidosproductos.getIdpedidoProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidosproductos with id " + id + " no longer exists.", enfe);
            }
            Pedidos idPedido = pedidosproductos.getIdPedido();
            if (idPedido != null) {
                idPedido.getPedidosproductosCollection().remove(pedidosproductos);
                idPedido = em.merge(idPedido);
            }
            Productos idProducto = pedidosproductos.getIdProducto();
            if (idProducto != null) {
                idProducto.getPedidosproductosCollection().remove(pedidosproductos);
                idProducto = em.merge(idProducto);
            }
            em.remove(pedidosproductos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedidosproductos> findPedidosproductosEntities() {
        return findPedidosproductosEntities(true, -1, -1);
    }

    public List<Pedidosproductos> findPedidosproductosEntities(int maxResults, int firstResult) {
        return findPedidosproductosEntities(false, maxResults, firstResult);
    }

    private List<Pedidosproductos> findPedidosproductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedidosproductos.class));
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

    public Pedidosproductos findPedidosproductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedidosproductos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidosproductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedidosproductos> rt = cq.from(Pedidosproductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
