package modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Remisionesproductos;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Remisiones.class)
public class Remisiones_ { 

    public static volatile SingularAttribute<Remisiones, Integer> idRemision;
    public static volatile SingularAttribute<Remisiones, Integer> precioCompra;
    public static volatile SingularAttribute<Remisiones, Remisionesproductos> remisionesproductos;
    public static volatile SingularAttribute<Remisiones, Date> fechaEntrada;
    public static volatile SingularAttribute<Remisiones, Integer> cantidad;

}