package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpot;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exceptions.ConflictException;
import com.api.parkingcontrol.services.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional  // Métodos de construção ou destruição devem acompanhar essa annotation para rollback em caso de erro
    public ParkingSpot save(ParkingSpotDto parkingSpotDto) {
        if(parkingSpotRepository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            throw new ConflictException("License Plate Car is already in use!");
        }

        if(parkingSpotRepository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            throw new ConflictException("Parking Spot is already in use!");
        }

        if(parkingSpotRepository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            throw new ConflictException("Parking Spot already registered for this apartment/block!");
        }

        var parkingSpotModel = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkingSpotRepository.save(parkingSpotModel);
    }

    @Transactional
    public ParkingSpot update(UUID id, ParkingSpotDto parkingSpotDto) {
        Optional<ParkingSpot> parkingSpotModelOptional = parkingSpotRepository.findById(id);

        if (!parkingSpotModelOptional.isPresent()) {
            throw new NotFoundException("Parking Spot of id " + id + " not found.");
        }

        if(parkingSpotRepository.existsByLicensePlateCarToUpdate(parkingSpotDto.getLicensePlateCar(), id)) {
            throw new ConflictException("License Plate Car is already in use!");
        }

        if(parkingSpotRepository.existsByParkingSpotNumberToUpdate(parkingSpotDto.getParkingSpotNumber(), id)) {
            throw new ConflictException("Parking Spot is already in use!");
        }

        if(parkingSpotRepository.existsByApartmentAndBlockToUpdate(parkingSpotDto.getApartment(), parkingSpotDto.getBlock(), id)) {
            throw new ConflictException("Parking Spot already registered for this apartment/block!");
        }

        var parkingSpotModel = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return parkingSpotRepository.save(parkingSpotModel);
    }

    public Page<ParkingSpot> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    public Optional<ParkingSpot> findById(UUID id) {
        var parkingSpot = parkingSpotRepository.findById(id);

//        if (!parkingSpot.isPresent()) {
//            throw new NotFoundException("Parking Spot of id " + id + " not found.");
//        }
//        return parkingSpot;

        return Optional.ofNullable(parkingSpot.orElseThrow(() -> new NotFoundException(
                "Parking Spot of id " + id + " not found."
        )));
    }

    @Transactional
    public void delete(UUID id) {
        Optional<ParkingSpot> parkingSpotModelOptional = parkingSpotRepository.findById(id);

        if (!parkingSpotModelOptional.isPresent()) {
            throw new NotFoundException("Parking Spot of id " + id + " not found.");
        }

        parkingSpotRepository.deleteById(id);
    }
}
