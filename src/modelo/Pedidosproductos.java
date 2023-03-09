/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author germa
 */
@Entity
@Table(name = "pedidosproductos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedidosproductos.findAll", query = "SELECT p FROM Pedidosproductos p")
    , @NamedQuery(name = "Pedidosproductos.findByIdpedidoProducto", query = "SELECT p FROM Pedidosproductos p WHERE p.idpedidoProducto = :idpedidoProducto")
    , @NamedQuery(name = "Pedidosproductos.findByCantidad", query = "SELECT p FROM Pedidosproductos p WHERE p.cantidad = :cantidad")})
public class Pedidosproductos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pedidoProducto")
    private Integer idpedidoProducto;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private int cantidad;
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    @ManyToOne
    private Pedidos idPedido;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne
    private Productos idProducto;

    public Pedidosproductos() {
    }

    public Pedidosproductos(Integer idpedidoProducto) {
        this.idpedidoProducto = idpedidoProducto;
    }

    public Pedidosproductos(Integer idpedidoProducto, int cantidad) {
        this.idpedidoProducto = idpedidoProducto;
        this.cantidad = cantidad;
    }

    public Integer getIdpedidoProducto() {
        return idpedidoProducto;
    }

    public void setIdpedidoProducto(Integer idpedidoProducto) {
        this.idpedidoProducto = idpedidoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Pedidos getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedidos idPedido) {
        this.idPedido = idPedido;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpedidoProducto != null ? idpedidoProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidosproductos)) {
            return false;
        }
        Pedidosproductos other = (Pedidosproductos) object;
        if ((this.idpedidoProducto == null && other.idpedidoProducto != null) || (this.idpedidoProducto != null && !this.idpedidoProducto.equals(other.idpedidoProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Pedidosproductos[ idpedidoProducto=" + idpedidoProducto + " ]";
    }
    
}
