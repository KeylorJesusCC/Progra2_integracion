# Reserva de Laboratorios

## Integrantes

- Keylor Jesus Castrellon Campos 

## Objetivo

Mejorar un sistema de reserva de laboratorios corrigiendo errores, agregando pruebas unitarias y aplicando los patrones Strategy y Observer.

## Problemas detectados

| Situación detectada                                    | Clase o método                          | Mejora realizada                                                                                                  |
| ------------------------------------------------------ | --------------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| Lógica condicional de horario invertida                | `ValidacionBasica.validar()`            | Se invirtió el operador `<`, obligando a que la hora inicial sea estrictamente menor a la final (`>=`).           |
| Falsa validación de superposición                      | `ReservaService.validarSuperposicion()` | El método estaba vacío. Se implementó la lógica de comparación de rangos de horas para mismo laboratorio y fecha. |
| Alto acoplamiento en mensajes de consola               | `ReservaService`                        | Se desacopló la impresión en pantalla delegando las notificaciones mediante el patrón Observer.                   |
| Falta de validación del rango de horario institucional | `ValidacionBasica`                      | Se extrajo dicha regla a una estrategia independiente para permitir horarios definidos dinámicamente.             |
| Mutabilidad de lista expuesta                          | `ReservaService.listarReservas()`       | Se utilizó `List.copyOf()` para devolver una vista inmutable de las reservas y proteger el estado del servicio.   |

## Patrón Strategy

Se utilizó el patrón **Strategy** para encapsular cada regla de validación de reservas en clases independientes (`ValidadorReserva`). Esto permite agregar nuevas reglas sin modificar la clase `ReservaService` (Principio Open/Closed).

- **Tercera Strategy implementada:** `ValidacionHorarioPermitido`.
- **Regla que valida:** Comprueba que la hora de inicio y de fin se encuentren dentro de un horario operativo permitido (por ejemplo, de 07:00 a 21:00 horas).

## Patrón Observer

Se utilizó el patrón **Observer** para notificar eventos cuando se registra una nueva reserva, permitiendo reaccionar ante cambios sin acoplar `ReservaService` a componentes específicos de salida.

- **Observers utilizados:**
  1. `ConsolaObserver`: Muestra una alerta simple en consola informando el código de la reserva creada.
  2. `ResumenObserver`: Genera un reporte detallado con el laboratorio, fecha y un contador incremental con el número total de reservas procesadas en sesión.

## Excepción personalizada

Se utiliza la excepción **`ReservaInvalidaException`**, la cual se lanza desde las distintas estrategias de validación y desde el servicio (`ReservaService`) cuando no se cumple alguna regla de negocio (código duplicado, horas inválidas, superposición de horarios o exceso de capacidad).

## Regla de superposición

Se resolvió comparando si dos reservas coinciden en el mismo laboratorio y la misma fecha, validando la solapación de intervalos con la condición:

`nueva.getHoraInicio() < existente.getHoraFinal() && existente.getHoraInicio() < nueva.getHoraFinal()`

**Decisión sobre reservas canceladas:** Las reservas con estado `CANCELADA` son ignoradas durante la validación de superposición (`continue`), permitiendo que el intervalo de tiempo quede disponible para que otros usuarios puedan reservarlo.

## Pruebas

Se implementaron las siguientes pruebas unitarias con JUnit 5 en `ReservaServiceTest`:

1. `debeAceptarReservaValida()`: Verifica el registro correcto de una reserva con todos los datos válidos.
2. `debeRechazarCantidadPersonasCero()`: Comprueba que `ValidacionCapacidad` lance la excepción si la capacidad es menor o igual a cero.
3. `debeRechazarReservasSuperpuestas()`: Confirms que la segunda reserva en el mismo horario/laboratorio/fecha sea rechazada y no se registre.
4. `debeAceptarReservasConsecutivas()`: Valida que dos reservas seguidas en el mismo laboratorio (ej. 8-10 y 10-12) sean aceptadas sin conflicto.

## Error detectado con depuración

- **Dónde estaba:** En la clase `ValidacionBasica.java`, dentro del método `validar()`.
- **Qué ocurría:** La condición evaluaba si `horaInicio < horaFinal`. En lugar de permitir el paso, lanzaba la excepción `ReservaInvalidaException` cuando el rango era correcto (por ejemplo, inicio 8 y final 10).
- **Cómo se corrigió:** Se invirtió la comparación a `horaInicio >= horaFinal`, garantizando que la excepción únicamente se dispare si la hora de inicio es igual o posterior a la hora final. Asimismo, se agregó import cr.ac.una.reservas.model.EstadoReserva; en ReservaService.java para resolver el error de compilación.
  (no lo tenia no se si fue parte del error preescrito)
## Ejecución

Requisitos: JDK 21 o posterior y Maven.

```bash
mvn clean compile
mvn test
mvn exec:java
```