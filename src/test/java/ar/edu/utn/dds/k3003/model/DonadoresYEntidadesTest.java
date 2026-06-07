package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.app.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class DonadoresYEntidadesTest {

  @Autowired
  Fachada instancia;

  @Test
  void testSiempreTrue() {
    Assertions.assertTrue(true);
  }

  @Test
  void testSiempreEquals() {
    Assertions.assertEquals(1, 1);
  }
}
