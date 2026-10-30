package cr.ac.una.reservas.strategy;

import cr.ac.una.reservas.exception.ReservaInvalidaException;
import cr.ac.una.reservas.model.Reserva;

public class ValidacionHorarioPermitido implements ValidadorReserva {

    private final int horaApertura;
    private final int horaCierre;

    public ValidacionHorarioPermitido(int horaApertura, int horaCierre) {
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    @Override
    public void validar(Reserva reserva) throws ReservaInvalidaException {
        if (reserva.getHoraInicio() < horaApertura || reserva.getHoraFinal() > horaCierre) {
            throw new ReservaInvalidaException(
                    "El horario permitido para reservas es de " 
                    + horaApertura + ":00 a " + horaCierre + ":00"
            );
        }
    }
}
