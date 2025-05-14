package com.tavolo.platform.booking.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tavolo.platform.booking.application.internal.commandservices.TableCommandServiceImpl;
import com.tavolo.platform.booking.domain.model.commands.CreateTableCommand;
import com.tavolo.platform.booking.domain.model.aggregates.Table;
import com.tavolo.platform.booking.domain.model.valueobjects.TableDetails;
import com.tavolo.platform.booking.domain.model.valueobjects.HeadquarterId;
import com.tavolo.platform.booking.application.internal.outboundedservices.acl.ExternalHeadquarterService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

public class TableCommandServiceTest {
    @Mock
    private com.tavolo.platform.booking.infrastructure.persistence.jpa.repositories.TableRepository tableRepository;

    @Mock
    private ExternalHeadquarterService externalHeadquarterService;

    @InjectMocks
    private TableCommandServiceImpl tableCommandService;

  @BeforeEach
  public void setUp() {
      MockitoAnnotations.openMocks(this);

      // Inyección manual de los mocks
      ReflectionTestUtils.setField(tableCommandService, "tableRepository", tableRepository);
      ReflectionTestUtils.setField(tableCommandService, "externalHeadquarterService", externalHeadquarterService);
  }

    @Test
    public void testHandleCreateTableCommand() {
        // Crear un comando de ejemplo con los parámetros correctos
        Integer tableNumber = 1;
        Integer capacity = 4;
        Long headquarterId = 1L;

        // Configuración del mock para que retorne true cuando se verifica si existe el headquarter
        when(externalHeadquarterService.existsHeadquarter(headquarterId)).thenReturn(true);

        CreateTableCommand command = new CreateTableCommand(tableNumber, capacity, headquarterId);
        Table mockTable = new Table(new TableDetails(tableNumber, capacity), new HeadquarterId(headquarterId));
        when(tableRepository.save(any(Table.class))).thenReturn(mockTable);

        Optional<Table> result = tableCommandService.handle(command);

        assertTrue(result.isPresent());  // Verificar que se crea la mesa
    }
}