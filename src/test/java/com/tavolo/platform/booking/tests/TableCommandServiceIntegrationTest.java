package com.tavolo.platform.booking.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import com.tavolo.platform.booking.application.internal.commandservices.TableCommandServiceImpl;
import com.tavolo.platform.booking.domain.model.commands.CreateTableCommand;
import com.tavolo.platform.booking.domain.model.aggregates.Table;
import java.util.Optional;
import com.tavolo.platform.booking.domain.model.valueobjects.TableDetails;

@SpringBootTest
@Transactional
public class TableCommandServiceIntegrationTest {
    @Autowired
    private TableCommandServiceImpl tableCommandService;
    
    @Test
    public void testHandleCreateTableCommandIntegration() {
        // Crear un comando de ejemplo con los parámetros correctos
        Integer tableNumber = 1; // Número de mesa como Integer
        Integer capacity = 4;    // Capacidad como Integer
        Long headquarterId = 1L; // ID de la sede

        CreateTableCommand command = new CreateTableCommand(tableNumber, capacity, headquarterId);
        Optional<Table> result = tableCommandService.handle(command);
        assertTrue(result.isPresent());  // Verificar que se crea la mesa
    }
} 