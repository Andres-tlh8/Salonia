package com.salonia.service.impl;

import com.salonia.dto.request.CreateCitaRequest;
import com.salonia.dto.request.UpdateCitaRequest;
import com.salonia.dto.response.CitaResponse;
import com.salonia.entity.Cita;
import com.salonia.entity.Cliente;
import com.salonia.entity.Estilista;
import com.salonia.entity.Salon;
import com.salonia.entity.Servicio;
import com.salonia.enums.EstadoCita;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.CitaRepository;
import com.salonia.repository.ClienteRepository;
import com.salonia.repository.EstilistaRepository;
import com.salonia.repository.SalonRepository;
import com.salonia.repository.ServicioRepository;
import com.salonia.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final EstilistaRepository estilistaRepository;
    private final ServicioRepository servicioRepository;
    private final SalonRepository salonRepository;

    @Override
    public CitaResponse createCita(CreateCitaRequest request, Long salonId) {

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cliente no encontrado con id: " + request.clienteId()));

        Estilista estilista = estilistaRepository.findById(request.estilistaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Estilista no encontrado con id: " + request.estilistaId()));

        Servicio servicio = servicioRepository.findById(request.servicioId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Servicio no encontrado con id: " + request.servicioId()));

        if (!cliente.getSalon().getId().equals(salonId)
                || !estilista.getSalon().getId().equals(salonId)
                || !servicio.getSalon().getId().equals(salonId)) {

            throw new IllegalArgumentException(
                    "El cliente, estilista y servicio deben pertenecer al mismo salón");
        }

        validarFechaHora(
                request.fecha(),
                request.horaInicio(),
                servicio.getDuracionMinutos(),
                estilista,
                null
        );

        Cita cita = new Cita();

        cita.setFecha(request.fecha());
        cita.setHoraInicio(request.horaInicio());
        cita.setHoraFin(
                request.horaInicio()
                        .plusMinutes(servicio.getDuracionMinutos())
        );

        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setCliente(cliente);
        cita.setEstilista(estilista);
        cita.setServicio(servicio);
        cita.setSalon(cliente.getSalon());

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public List<CitaResponse> getAllCitas(Long salonId) {

        findSalonById(salonId);

        return citaRepository.findBySalonId(salonId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CitaResponse getCitaById(Long id, Long salonId) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cita no encontrada con id: " + id));

        if (!cita.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException(
                    "Cita no pertenece al salón actual");
        }

        return toResponse(cita);
    }

    @Override
    public CitaResponse updateCita(
            Long id,
            UpdateCitaRequest request,
            Long salonId) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cita no encontrada con id: " + id));

        if (!cita.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException(
                    "Cita no pertenece al salón actual");
        }

        LocalDate fecha =
                request.fecha() != null
                        ? request.fecha()
                        : cita.getFecha();

        LocalTime horaInicio =
                request.horaInicio() != null
                        ? request.horaInicio()
                        : cita.getHoraInicio();

        if (request.clienteId() != null) {

            Cliente cliente = clienteRepository.findById(request.clienteId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Cliente no encontrado con id: "
                                    + request.clienteId()));

            if (!cliente.getSalon().getId().equals(salonId)) {
                throw new IllegalArgumentException(
                        "El cliente no pertenece al mismo salón");
            }

            cita.setCliente(cliente);
        }

        if (request.estilistaId() != null) {

            Estilista estilista =
                    estilistaRepository.findById(request.estilistaId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Estilista no encontrado con id: "
                                            + request.estilistaId()));

            if (!estilista.getSalon().getId().equals(salonId)) {
                throw new IllegalArgumentException(
                        "El estilista no pertenece al mismo salón");
            }

            cita.setEstilista(estilista);
        }

        if (request.servicioId() != null) {

            Servicio servicio =
                    servicioRepository.findById(request.servicioId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Servicio no encontrado con id: "
                                            + request.servicioId()));

            if (!servicio.getSalon().getId().equals(salonId)) {
                throw new IllegalArgumentException(
                        "El servicio no pertenece al mismo salón");
            }

            cita.setServicio(servicio);
        }

        if (request.fecha() != null
                || request.horaInicio() != null
                || request.servicioId() != null) {

            validarFechaHora(
                    fecha,
                    horaInicio,
                    cita.getServicio().getDuracionMinutos(),
                    cita.getEstilista(),
                    cita.getId()
            );

            cita.setFecha(fecha);
            cita.setHoraInicio(horaInicio);
            cita.setHoraFin(
                    horaInicio.plusMinutes(
                            cita.getServicio().getDuracionMinutos()
                    )
            );
        }

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public void deleteCita(Long id, Long salonId) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cita no encontrada con id: " + id));

        if (!cita.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException(
                    "Cita no pertenece al salón actual");
        }

        citaRepository.delete(cita);
    }

    @Override
    public CitaResponse confirmarCita(Long id, Long salonId) {

        Cita cita = findCitaById(id, salonId);
        cita.setEstado(EstadoCita.CONFIRMADA);

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public CitaResponse cancelarCita(Long id, Long salonId) {

        Cita cita = findCitaById(id, salonId);
        cita.setEstado(EstadoCita.CANCELADA);

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public CitaResponse completarCita(Long id, Long salonId) {

        Cita cita = findCitaById(id, salonId);
        cita.setEstado(EstadoCita.COMPLETADA);

        return toResponse(citaRepository.save(cita));
    }

    private Cita findCitaById(Long id, Long salonId) {

        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cita no encontrada con id: " + id));

        if (!cita.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException(
                    "Cita no pertenece al salón actual");
        }

        return cita;
    }

    private Salon findSalonById(Long salonId) {

        return salonRepository.findById(salonId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Salon no encontrado con id: " + salonId));
    }

    private void validarFechaHora(
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Estilista estilista,
            Long citaActualId) {

        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "No se permiten citas en fechas pasadas");
        }

        if (fecha.equals(LocalDate.now())
                && horaInicio.isBefore(LocalTime.now())) {

            throw new IllegalArgumentException(
                    "No se permiten citas en horas ya transcurridas");
        }

        LocalTime horaFin =
                horaInicio.plusMinutes(duracionMinutos);

        LocalTime horaApertura =
                LocalTime.of(8, 0);

        LocalTime horaCierre =
                LocalTime.of(18, 0);

        if (horaInicio.isBefore(horaApertura)
                || horaFin.isAfter(horaCierre)) {

            throw new IllegalArgumentException(
                    "La cita está fuera del horario de atención (08:00 - 18:00)");
        }

        List<Cita> citasDelDia =
                citaRepository.findByEstilistaIdAndFecha(
                        estilista.getId(),
                        fecha
                );

        for (Cita cita : citasDelDia) {

            if (cita.getEstado() == EstadoCita.CANCELADA) {
                continue;
            }

            if (citaActualId != null
                    && cita.getId().equals(citaActualId)) {
                continue;
            }

            boolean traslape =
                    horaInicio.isBefore(cita.getHoraFin())
                            && horaFin.isAfter(cita.getHoraInicio());

            if (traslape) {
                throw new IllegalArgumentException(
                        "No se permite traslape de citas para el mismo estilista");
            }
        }
    }

    private CitaResponse toResponse(Cita cita) {

        return new CitaResponse(
                cita.getId(),
                cita.getFecha(),
                cita.getHoraInicio(),
                cita.getHoraFin(),
                cita.getEstado(),
                cita.getCliente().getId(),
                cita.getEstilista().getId(),
                cita.getServicio().getId(),
                cita.getSalon().getId()
        );
    }
}