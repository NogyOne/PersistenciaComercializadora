package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Clientes;
import modelo.Pedidosproductos;
import modelo.Ventas;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Pedidos.class)
public class Pedidos_ { 

    public static volatile CollectionAttribute<Pedidos, Pedidosproductos> pedidosproductosCollection;
    public static volatile SingularAttribute<Pedidos, Clientes> idCliente;
    public static volatile CollectionAttribute<Pedidos, Ventas> ventasCollection;
    public static volatile SingularAttribute<Pedidos, Integer> idPedido;
    public static volatile SingularAttribute<Pedidos, Integer> precioTotal;

}