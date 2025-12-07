package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.dto.RideResponse;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.exception.UnauthorizedException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    
    private User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }
    
    public RideResponse createRide(CreateRideRequest request) {
        User user = getCurrentUser();
        
        // Only ROLE_USER can request rides
        if (!"ROLE_USER".equals(user.getRole())) {
            throw new BadRequestException("Only users with ROLE_USER can request rides");
        }
        
        Ride ride = new Ride(user.getId(), request.getPickupLocation(), request.getDropLocation());
        Ride savedRide = rideRepository.save(ride);
        
        return RideResponse.fromRide(savedRide);
    }
    
    public List<RideResponse> getPendingRides() {
        User user = getCurrentUser();
        
        // Only drivers can view pending rides
        if (!"ROLE_DRIVER".equals(user.getRole())) {
            throw new UnauthorizedException("Only drivers can view pending ride requests");
        }
        
        List<Ride> rides = rideRepository.findByStatus("REQUESTED");
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
    
    public RideResponse acceptRide(String rideId) {
        User driver = getCurrentUser();
        
        // Only drivers can accept rides
        if (!"ROLE_DRIVER".equals(driver.getRole())) {
            throw new UnauthorizedException("Only drivers can accept rides");
        }
        
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        
        // Check if ride is in REQUESTED status
        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not available for acceptance");
        }
        
        // Assign driver and update status
        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        
        Ride updatedRide = rideRepository.save(ride);
        return RideResponse.fromRide(updatedRide);
    }
    
    public RideResponse completeRide(String rideId) {
        User currentUser = getCurrentUser();
        
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));
        
        // Check if ride is in ACCEPTED status
        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride must be in ACCEPTED status to be completed");
        }
        
        // Verify user is either the passenger or the driver
        if (!currentUser.getId().equals(ride.getUserId()) && 
            !currentUser.getId().equals(ride.getDriverId())) {
            throw new UnauthorizedException("You are not authorized to complete this ride");
        }
        
        ride.setStatus("COMPLETED");
        Ride updatedRide = rideRepository.save(ride);
        
        return RideResponse.fromRide(updatedRide);
    }
    
    public List<RideResponse> getUserRides() {
        User user = getCurrentUser();
        
        // Only ROLE_USER can view their rides
        if (!"ROLE_USER".equals(user.getRole())) {
            throw new UnauthorizedException("Only users with ROLE_USER can view their rides");
        }
        
        List<Ride> rides = rideRepository.findByUserId(user.getId());
        return rides.stream()
                .map(RideResponse::fromRide)
                .collect(Collectors.toList());
    }
}
