package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Pedidos;
import modelo.Productos;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Pedidosproductos.class)
public class Pedidosproductos_ { 

    public static volatile SingularAttribute<Pedidosproductos, Integer> idpedidoProducto;
    public static volatile SingularAttribute<Pedidosproductos, Integer> cantidad;
    public static volatile SingularAttribute<Pedidosproductos, Productos> idProducto;
    public static volatile SingularAttribute<Pedidosproductos, Pedidos> idPedido;

}