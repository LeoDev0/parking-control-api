package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, UUID> {
    boolean existsByLicensePlateCar(String licensePlateCar);
    boolean existsByParkingSpotNumber(String parkingSpotNumber);
    boolean existsByApartmentAndBlock(String apartment, String block);

    @Query(value = "SELECT CASE WHEN COUNT (ps) > 0 THEN true ELSE false END FROM ParkingSpot ps WHERE ps.licensePlateCar = ?1 AND ps.id NOT IN (?2)")
    boolean existsByLicensePlateCarToUpdate(String licensePlateCar, UUID id);

    @Query(value = "SELECT CASE WHEN COUNT (ps) > 0 THEN true ELSE false END FROM ParkingSpot ps WHERE ps.parkingSpotNumber = ?1 AND ps.id NOT IN (?2)")
    boolean existsByParkingSpotNumberToUpdate(String parkingSpotNumber, UUID id);

    @Query(value = "SELECT CASE WHEN COUNT (ps) > 0 THEN true ELSE false END FROM ParkingSpot ps WHERE ps.apartment = ?1 AND ps.block = ?2 AND ps.id NOT IN (?3)")
    boolean existsByApartmentAndBlockToUpdate(String apartment, String block, UUID id);
}
