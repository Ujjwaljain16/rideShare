package org.example.rideshare.dto;

import java.util.Date;

import org.example.rideshare.model.Ride;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private String id;
    private String userId;
    private String driverId;
    private String pickupLocation;
    private String dropLocation;
    private String status;
    private Date createdAt;
    
    public static RideResponse fromRide(Ride ride) {
        return new RideResponse(
            ride.getId(),
            ride.getUserId(),
            ride.getDriverId(),
            ride.getPickupLocation(),
            ride.getDropLocation(),
            ride.getStatus(),
            ride.getCreatedAt()
        );
    }
}
