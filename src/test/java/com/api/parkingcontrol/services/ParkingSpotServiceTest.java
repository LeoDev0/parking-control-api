package com.api.parkingcontrol.services;

import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import com.api.parkingcontrol.services.exceptions.NotFoundException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotServiceTest {

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    private AutoCloseable closeable;

    @Test
    @DisplayName("Should return error when id doesn't exist")
    public void testFindParkingSpotByIdNotFoundError() {
        var uuid = UUID.randomUUID();
        Mockito.doReturn(Optional.ofNullable(null))
                .when(parkingSpotRepository)
                .findById(ArgumentMatchers.<UUID>any());

//        Mockito.when(parkingSpotRepository.findById(ArgumentMatchers.<UUID>any()))
//                .thenReturn(Optional.ofNullable(null));

        try {
            parkingSpotService.findById(uuid);
        } catch (NotFoundException e) {
            Assertions.assertTrue(e.getMessage().startsWith("Parking Spot of id"));
            Assertions.assertThrows(NotFoundException.class, () -> parkingSpotService.findById(uuid));
        }
    }
}
