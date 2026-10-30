package cr.ac.una.reservas;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;
import cr.ac.una.reservas.service.ReservaService;
import cr.ac.una.reservas.strategy.ValidacionBasica;
import cr.ac.una.reservas.strategy.ValidacionCapacidad;
import cr.ac.una.reservas.strategy.ValidacionHorarioPermitido;

class ReservaServiceTest {

    private ReservaService service;

    @BeforeEach
    void setUp() {

        service = new ReservaService();

        service.agregarValidador(
                new ValidacionBasica()
        );

        service.agregarValidador(
                new ValidacionCapacidad(30)
        );
        service.agregarValidador(
                new ValidacionHorarioPermitido(7, 21)
        );
    }

    @Test
    void debeAceptarReservaValida()
            throws Exception {

        Reserva reserva =
                new Reserva(
                        "R-001",
                        "Ana",
                        "Lab 1",
                        LocalDate.now(),
                        8,
                        10,
                        20
                );

        service.registrarReserva(reserva);

        assertEquals(
                1,
                service.listarReservas().size()
        );
    }

    @Test
    void debeRechazarCantidadPersonasCero() {

        Reserva reserva =
                new Reserva(
                        "R-002",
                        "Luis",
                        "Lab 1",
                        LocalDate.now(),
                        10,
                        12,
                        0
                );

        assertThrows(
                ReservaInvalidaException.class,
                () -> service.registrarReserva(
                        reserva
                )
        );
    }
    @Test
    void debeRechazarReservasSuperpuestas() throws Exception {
        Reserva r1 = new Reserva(
                "R-001",
                "Ana",
                "Lab 1",
                LocalDate.now(),
                8,
                10,
                20
        );
        Reserva r2 = new Reserva(
                "R-002",
                "Pedro",
                "Lab 1",
                LocalDate.now(),
                9,
                11,
                15
        );

        service.registrarReserva(r1);

        assertThrows(
                ReservaInvalidaException.class,
                () -> service.registrarReserva(r2)
        );
        assertEquals(1, service.listarReservas().size());
    }
    
    @Test
    void debeAceptarReservasConsecutivas() throws Exception {
        Reserva r1 = new Reserva(
                "R-001",
                "Ana",
                "Lab 1",
                LocalDate.now(),
                8,
                10,
                20
        );
        Reserva r2 = new Reserva(
                "R-002",
                "Carlos",
                "Lab 1",
                LocalDate.now(),
                10,
                12,
                15
        );

        service.registrarReserva(r1);
        service.registrarReserva(r2);

        assertEquals(2, service.listarReservas().size());
    }
    
    
}
