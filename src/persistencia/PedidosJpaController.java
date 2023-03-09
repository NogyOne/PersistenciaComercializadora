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
import modelo.Clientes;
import modelo.Pedidosproductos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Pedidos;
import modelo.Ventas;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author germa
 */
public class PedidosJpaController implements Serializable {

    public PedidosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public PedidosJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedidos pedidos) {
        if (pedidos.getPedidosproductosCollection() == null) {
            pedidos.setPedidosproductosCollection(new ArrayList<Pedidosproductos>());
        }
        if (pedidos.getVentasCollection() == null) {
            pedidos.setVentasCollection(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clientes idCliente = pedidos.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                pedidos.setIdCliente(idCliente);
            }
            Collection<Pedidosproductos> attachedPedidosproductosCollection = new ArrayList<Pedidosproductos>();
            for (Pedidosproductos pedidosproductosCollectionPedidosproductosToAttach : pedidos.getPedidosproductosCollection()) {
                pedidosproductosCollectionPedidosproductosToAttach = em.getReference(pedidosproductosCollectionPedidosproductosToAttach.getClass(), pedidosproductosCollectionPedidosproductosToAttach.getIdpedidoProducto());
                attachedPedidosproductosCollection.add(pedidosproductosCollectionPedidosproductosToAttach);
            }
            pedidos.setPedidosproductosCollection(attachedPedidosproductosCollection);
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : pedidos.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVenta());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            pedidos.setVentasCollection(attachedVentasCollection);
            em.persist(pedidos);
            if (idCliente != null) {
                idCliente.getPedidosCollection().add(pedidos);
                idCliente = em.merge(idCliente);
            }
            for (Pedidosproductos pedidosproductosCollectionPedidosproductos : pedidos.getPedidosproductosCollection()) {
                Pedidos oldIdPedidoOfPedidosproductosCollectionPedidosproductos = pedidosproductosCollectionPedidosproductos.getIdPedido();
                pedidosproductosCollectionPedidosproductos.setIdPedido(pedidos);
                pedidosproductosCollectionPedidosproductos = em.merge(pedidosproductosCollectionPedidosproductos);
                if (oldIdPedidoOfPedidosproductosCollectionPedidosproductos != null) {
                    oldIdPedidoOfPedidosproductosCollectionPedidosproductos.getPedidosproductosCollection().remove(pedidosproductosCollectionPedidosproductos);
                    oldIdPedidoOfPedidosproductosCollectionPedidosproductos = em.merge(oldIdPedidoOfPedidosproductosCollectionPedidosproductos);
                }
            }
            for (Ventas ventasCollectionVentas : pedidos.getVentasCollection()) {
                Pedidos oldIdPedidoOfVentasCollectionVentas = ventasCollectionVentas.getIdPedido();
                ventasCollectionVentas.setIdPedido(pedidos);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdPedidoOfVentasCollectionVentas != null) {
                    oldIdPedidoOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdPedidoOfVentasCollectionVentas = em.merge(oldIdPedidoOfVentasCollectionVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedidos pedidos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedidos persistentPedidos = em.find(Pedidos.class, pedidos.getIdPedido());
            Clientes idClienteOld = persistentPedidos.getIdCliente();
            Clientes idClienteNew = pedidos.getIdCliente();
            Collection<Pedidosproductos> pedidosproductosCollectionOld = persistentPedidos.getPedidosproductosCollection();
            Collection<Pedidosproductos> pedidosproductosCollectionNew = pedidos.getPedidosproductosCollection();
            Collection<Ventas> ventasCollectionOld = persistentPedidos.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = pedidos.getVentasCollection();
            List<String> illegalOrphanMessages = null;
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idPedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                pedidos.setIdCliente(idClienteNew);
            }
            Collection<Pedidosproductos> attachedPedidosproductosCollectionNew = new ArrayList<Pedidosproductos>();
            for (Pedidosproductos pedidosproductosCollectionNewPedidosproductosToAttach : pedidosproductosCollectionNew) {
                pedidosproductosCollectionNewPedidosproductosToAttach = em.getReference(pedidosproductosCollectionNewPedidosproductosToAttach.getClass(), pedidosproductosCollectionNewPedidosproductosToAttach.getIdpedidoProducto());
                attachedPedidosproductosCollectionNew.add(pedidosproductosCollectionNewPedidosproductosToAttach);
            }
            pedidosproductosCollectionNew = attachedPedidosproductosCollectionNew;
            pedidos.setPedidosproductosCollection(pedidosproductosCollectionNew);
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVenta());
                attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
            }
            ventasCollectionNew = attachedVentasCollectionNew;
            pedidos.setVentasCollection(ventasCollectionNew);
            pedidos = em.merge(pedidos);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getPedidosCollection().remove(pedidos);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getPedidosCollection().add(pedidos);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Pedidosproductos pedidosproductosCollectionOldPedidosproductos : pedidosproductosCollectionOld) {
                if (!pedidosproductosCollectionNew.contains(pedidosproductosCollectionOldPedidosproductos)) {
                    pedidosproductosCollectionOldPedidosproductos.setIdPedido(null);
                    pedidosproductosCollectionOldPedidosproductos = em.merge(pedidosproductosCollectionOldPedidosproductos);
                }
            }
            for (Pedidosproductos pedidosproductosCollectionNewPedidosproductos : pedidosproductosCollectionNew) {
                if (!pedidosproductosCollectionOld.contains(pedidosproductosCollectionNewPedidosproductos)) {
                    Pedidos oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos = pedidosproductosCollectionNewPedidosproductos.getIdPedido();
                    pedidosproductosCollectionNewPedidosproductos.setIdPedido(pedidos);
                    pedidosproductosCollectionNewPedidosproductos = em.merge(pedidosproductosCollectionNewPedidosproductos);
                    if (oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos != null && !oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos.equals(pedidos)) {
                        oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos.getPedidosproductosCollection().remove(pedidosproductosCollectionNewPedidosproductos);
                        oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos = em.merge(oldIdPedidoOfPedidosproductosCollectionNewPedidosproductos);
                    }
                }
            }
            for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                    Pedidos oldIdPedidoOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdPedido();
                    ventasCollectionNewVentas.setIdPedido(pedidos);
                    ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                    if (oldIdPedidoOfVentasCollectionNewVentas != null && !oldIdPedidoOfVentasCollectionNewVentas.equals(pedidos)) {
                        oldIdPedidoOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                        oldIdPedidoOfVentasCollectionNewVentas = em.merge(oldIdPedidoOfVentasCollectionNewVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedidos.getIdPedido();
                if (findPedidos(id) == null) {
                    throw new NonexistentEntityException("The pedidos with id " + id + " no longer exists.");
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
            Pedidos pedidos;
            try {
                pedidos = em.getReference(Pedidos.class, id);
                pedidos.getIdPedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ventas> ventasCollectionOrphanCheck = pedidos.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedidos (" + pedidos + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idPedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clientes idCliente = pedidos.getIdCliente();
            if (idCliente != null) {
                idCliente.getPedidosCollection().remove(pedidos);
                idCliente = em.merge(idCliente);
            }
            Collection<Pedidosproductos> pedidosproductosCollection = pedidos.getPedidosproductosCollection();
            for (Pedidosproductos pedidosproductosCollectionPedidosproductos : pedidosproductosCollection) {
                pedidosproductosCollectionPedidosproductos.setIdPedido(null);
                pedidosproductosCollectionPedidosproductos = em.merge(pedidosproductosCollectionPedidosproductos);
            }
            em.remove(pedidos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedidos> findPedidosEntities() {
        return findPedidosEntities(true, -1, -1);
    }

    public List<Pedidos> findPedidosEntities(int maxResults, int firstResult) {
        return findPedidosEntities(false, maxResults, firstResult);
    }

    private List<Pedidos> findPedidosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedidos.class));
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

    public Pedidos findPedidos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedidos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedidos> rt = cq.from(Pedidos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
