package org.example.rideshare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rides")
public class Ride {
    
    @Id
    private String id;
    
    private String userId; // Passenger (FK)
    
    private String driverId; // Driver (FK)
    
    private String pickupLocation;
    
    private String dropLocation;
    
    private String status; // REQUESTED / ACCEPTED / COMPLETED
    
    private Date createdAt;
    
    public Ride(String userId, String pickupLocation, String dropLocation) {
        this.userId = userId;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.status = "REQUESTED";
        this.createdAt = new Date();
    }
}
