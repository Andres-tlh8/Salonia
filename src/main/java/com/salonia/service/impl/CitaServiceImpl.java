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
        Salon salon = findSalonById(salonId);

        Cliente cliente = findClienteEnSalon(request.clienteId(), salonId);
        Estilista estilista = findEstilistaEnSalon(request.estilistaId(), salonId);
        Servicio servicio = findServicioEnSalon(request.servicioId(), salonId);

        validarMismaRelacionDeSalon(cliente.getSalon().getId(), estilista.getSalon().getId(), servicio.getSalon().getId(), salonId);
        validarFechaHora(request.fecha(), request.horaInicio(), servicio.getDuracionMinutos(), estilista, null);

        Cita cita = new Cita();
        cita.setFecha(request.fecha());
        cita.setHoraInicio(request.horaInicio());
        cita.setHoraFin(request.horaInicio().plusMinutes(servicio.getDuracionMinutos()));
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setCliente(cliente);
        cita.setEstilista(estilista);
        cita.setServicio(servicio);
        cita.setSalon(salon);

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public List<CitaResponse> getAllCitas(Long salonId) {
        return getAllCitas(salonId, LocalDate.now());
    }

    @Override
    public List<CitaResponse> getAllCitas(Long salonId, LocalDate fecha) {
        findSalonById(salonId);
        LocalDate fechaFiltro = fecha != null ? fecha : LocalDate.now();

        return citaRepository.findBySalonIdAndFecha(salonId, fechaFiltro)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CitaResponse getCitaById(Long id, Long salonId) {
        return toResponse(findCitaById(id, salonId));
    }

    @Override
    public CitaResponse updateCita(Long id, UpdateCitaRequest request, Long salonId) {
        Cita cita = findCitaById(id, salonId);

        LocalDate fecha = request.fecha() != null ? request.fecha() : cita.getFecha();
        LocalTime horaInicio = request.horaInicio() != null ? request.horaInicio() : cita.getHoraInicio();

        if (request.clienteId() != null) {
            cita.setCliente(findClienteEnSalon(request.clienteId(), salonId));
        }

        if (request.estilistaId() != null) {
            cita.setEstilista(findEstilistaEnSalon(request.estilistaId(), salonId));
        }

        if (request.servicioId() != null) {
            cita.setServicio(findServicioEnSalon(request.servicioId(), salonId));
        }

        if (debeRevalidarHorario(request)) {
            validarFechaHora(fecha, horaInicio, cita.getServicio().getDuracionMinutos(), cita.getEstilista(), cita.getId());
            cita.setFecha(fecha);
            cita.setHoraInicio(horaInicio);
            cita.setHoraFin(horaInicio.plusMinutes(cita.getServicio().getDuracionMinutos()));
        }

        return toResponse(citaRepository.save(cita));
    }

    @Override
    public void deleteCita(Long id, Long salonId) {
        citaRepository.delete(findCitaById(id, salonId));
    }

    @Override
    public CitaResponse confirmarCita(Long id, Long salonId) {
        return cambiarEstado(id, EstadoCita.CONFIRMADA, salonId);
    }

    @Override
    public CitaResponse cancelarCita(Long id, Long salonId) {
        return cambiarEstado(id, EstadoCita.CANCELADA, salonId);
    }

    @Override
    public CitaResponse completarCita(Long id, Long salonId) {
        return cambiarEstado(id, EstadoCita.COMPLETADA, salonId);
    }

    @Override
    public CitaResponse noAsistioCita(Long id, Long salonId) {
        return cambiarEstado(id, EstadoCita.NO_ASISTIO, salonId);
    }

    @Override
    public CitaResponse cambiarEstado(Long id, EstadoCita nuevoEstado, Long salonId) {
        Cita cita = findCitaById(id, salonId);
        validarCambioEstado(cita.getEstado(), nuevoEstado);
        cita.setEstado(nuevoEstado);
        return toResponse(citaRepository.save(cita));
    }

    private Salon findSalonById(Long salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() -> new EntityNotFoundException("Salon no encontrado con id: " + salonId));
    }

    private Cita findCitaById(Long id, Long salonId) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cita no encontrada con id: " + id));

        if (!cita.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Cita no pertenece al salón actual");
        }

        return cita;
    }

    private Cliente findClienteEnSalon(Long clienteId, Long salonId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + clienteId));

        if (!cliente.getSalon().getId().equals(salonId)) {
            throw new IllegalArgumentException("El cliente no pertenece al mismo salón");
        }

        return cliente;
    }

    private Estilista findEstilistaEnSalon(Long estilistaId, Long salonId) {
        Estilista estilista = estilistaRepository.findById(estilistaId)
                .orElseThrow(() -> new EntityNotFoundException("Estilista no encontrado con id: " + estilistaId));

        if (!estilista.getSalon().getId().equals(salonId)) {
            throw new IllegalArgumentException("El estilista no pertenece al mismo salón");
        }

        return estilista;
    }

    private Servicio findServicioEnSalon(Long servicioId, Long salonId) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + servicioId));

        if (!servicio.getSalon().getId().equals(salonId)) {
            throw new IllegalArgumentException("El servicio no pertenece al mismo salón");
        }

        return servicio;
    }

    private void validarMismaRelacionDeSalon(Long clienteSalonId, Long estilistaSalonId, Long servicioSalonId, Long salonId) {
        if (!clienteSalonId.equals(salonId)
                || !estilistaSalonId.equals(salonId)
                || !servicioSalonId.equals(salonId)) {
            throw new IllegalArgumentException("El cliente, estilista y servicio deben pertenecer al mismo salón");
        }
    }

    private boolean debeRevalidarHorario(UpdateCitaRequest request) {
        return request.fecha() != null || request.horaInicio() != null || request.servicioId() != null;
    }

    private void validarCambioEstado(EstadoCita estadoActual, EstadoCita nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }

        if (!estadoActual.puedeCambiarA(nuevoEstado)) {
            throw new IllegalArgumentException(
                    "No se permite cambiar una cita de " + estadoActual + " a " + nuevoEstado
            );
        }
    }

    private void validarFechaHora(
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Estilista estilista,
            Long citaActualId) {

        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se permiten citas en fechas pasadas");
        }

        if (fecha.equals(LocalDate.now()) && horaInicio.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("No se permiten citas en horas ya transcurridas");
        }

        LocalTime horaFin = horaInicio.plusMinutes(duracionMinutos);
        LocalTime horaApertura = LocalTime.of(8, 0);
        LocalTime horaCierre = LocalTime.of(18, 0);

        if (horaInicio.isBefore(horaApertura) || horaFin.isAfter(horaCierre)) {
            throw new IllegalArgumentException("La cita está fuera del horario de atención (08:00 - 18:00)");
        }

        List<Cita> citasDelDia = citaRepository.findByEstilistaIdAndFecha(estilista.getId(), fecha);

        for (Cita cita : citasDelDia) {
            if (!cita.getEstado().ocupaAgenda()) {
                continue;
            }

            if (citaActualId != null && citaActualId.equals(cita.getId())) {
                continue;
            }

            boolean traslape = horaInicio.isBefore(cita.getHoraFin()) && horaFin.isAfter(cita.getHoraInicio());

            if (traslape) {
                throw new IllegalArgumentException("No se permite traslape de citas para el mismo estilista");
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