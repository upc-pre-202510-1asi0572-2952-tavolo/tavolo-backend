@AcceptanceTest
Feature: Autenticación de usuarios
  Scenario: Registro exitoso de un nuevo usuario
    Given el usuario proporciona datos válidos para registro
    When se envía la solicitud de registro
    Then el sistema crea el usuario y devuelve un estado 201
  Scenario: Fallo en el registro por datos inválidos
    Given el usuario proporciona datos inválidos
    When se envía la solicitud de registro
    Then el sistema devuelve un error de validación 