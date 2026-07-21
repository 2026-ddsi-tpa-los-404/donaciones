package ar.edu.utn.dds.k3003.model.generator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * Genera IDs secuenciales numericos (respaldados por una secuencia nativa de la base de
 * datos, atomica y segura ante concurrencia) expuestos como String, para no romper el
 * contrato de IDs String usado en las entidades, DTOs y repositorios de este servicio.
 *
 * <p>Se implementa a mano en lugar de usar {@code GenerationType.SEQUENCE} porque Hibernate
 * exige un tipo numerico (Long/Integer) para esa estrategia, y el atributo id de estas
 * entidades es String por contrato externo (DTOs compartidos con otros microservicios).
 */
public class SequentialStringIdGenerator implements IdentifierGenerator, Configurable {

  private String sequenceName;

  @Override
  public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
    this.sequenceName = params.getProperty("sequence_name");
    if (sequenceName == null || sequenceName.isBlank()) {
      throw new HibernateException(
          "SequentialStringIdGenerator requiere el parametro 'sequence_name'");
    }
  }

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object) {
    Connection connection = session.getJdbcCoordinator().getLogicalConnection().getPhysicalConnection();
    try (Statement statement = connection.createStatement()) {
      statement.execute(
          "CREATE SEQUENCE IF NOT EXISTS " + sequenceName + " START WITH 1 INCREMENT BY 1");
      try (ResultSet resultSet = statement.executeQuery("SELECT NEXTVAL('" + sequenceName + "')")) {
        resultSet.next();
        return String.valueOf(resultSet.getLong(1));
      }
    } catch (SQLException e) {
      throw new HibernateException("No se pudo generar el id desde la secuencia " + sequenceName, e);
    }
  }
}
