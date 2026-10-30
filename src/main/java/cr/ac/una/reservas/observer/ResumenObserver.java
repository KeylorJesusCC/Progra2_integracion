package cr.ac.una.reservas.observer;

import cr.ac.una.reservas.model.Reserva;

public class ResumenObserver implements ReservaObserver {

    private int contadorReservas = 0;

    @Override
    public void reservaRegistrada(Reserva reserva) {
        contadorReservas++;
        System.out.println(
                "[RESUMEN] Reserva #" + contadorReservas + " asignada al " 
                + reserva.getLaboratorio() + " para el " + reserva.getFecha() 
                + " (" + reserva.getHoraInicio() + ":00-" + reserva.getHoraFinal() + ":00)"
        );
    }
}
