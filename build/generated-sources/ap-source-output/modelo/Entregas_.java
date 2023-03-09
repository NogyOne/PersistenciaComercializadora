package modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Ventas;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Entregas.class)
public class Entregas_ { 

    public static volatile SingularAttribute<Entregas, Date> fecha;
    public static volatile SingularAttribute<Entregas, Integer> idEntrega;
    public static volatile SingularAttribute<Entregas, Ventas> idVenta;

}