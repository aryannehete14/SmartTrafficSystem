package SmartTrafficSystem.demo;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class SmartTrafficSystem {

    // ==========================================
    // Data Model
    // ==========================================
    static class VehicleEvent {
        String vehicleId;
        double speed;
        String zone;
        boolean isEmergencyVehicle;
        long timestamp;

        public VehicleEvent(String vehicleId, double speed, String zone, boolean isEmergencyVehicle, long timestamp) {
            this.vehicleId = vehicleId;
            this.speed = speed;
            this.zone = zone;
            this.isEmergencyVehicle = isEmergencyVehicle;
            this.timestamp = timestamp;
        }
    }

    static class ViolationRecord {
        String vehicleId;
        double speed;
        String zone;
        int fine;

        public ViolationRecord(String vehicleId, double speed, String zone, int fine) {
            this.vehicleId = vehicleId;
            this.speed = speed;
            this.zone = zone;
            this.fine = fine;
        }

        @Override
        public String toString() {
            return "Vehicle: " + vehicleId + " | Speed: " + speed +
                    " | Zone: " + zone + " | Fine: Rs. " + fine;
        }
    }

    // ==========================================
    // Dynamic Rule Engine
    // ==========================================
    static class TrafficRules {

        // Speed limit rule (can be changed dynamically)
        static Predicate<VehicleEvent> violationFilter = event -> event.speed > 80 && !event.isEmergencyVehicle;

        // Fine calculation logic (pluggable)
        static Function<Double, Integer> fineCalculator = speed -> {
            if (speed > 120)
                return 5000;
            else if (speed > 100)
                return 2000;
            else
                return 1000;
        };
    }

    // ==========================================
    // Main Processing
    // ==========================================
    public static void main(String[] args) {

        List<VehicleEvent> events = Arrays.asList(
                new VehicleEvent("MH12AB1234", 95, "Pune-West", false, System.currentTimeMillis()),
                new VehicleEvent("MH14XY5678", 130, "Pune-East", false, System.currentTimeMillis()),
                new VehicleEvent(null, 110, null, false, System.currentTimeMillis()),
                new VehicleEvent("MH01ZZ9999", 70, "Mumbai", false, System.currentTimeMillis()),
                new VehicleEvent("MH12EM1111", 140, "Pune-West", true, System.currentTimeMillis()));

        // Processing Pipeline
        // ==========================================
        List<ViolationRecord> violations = events
                .parallelStream() // parallel for real-time simulation
                .filter(Objects::nonNull)
                .filter(TrafficRules.violationFilter) // Predicate
                .map(event -> {
                    // Optional for null safety
                    String vehicleId = Optional.ofNullable(event.vehicleId)
                            .orElse("UNKNOWN");

                    String zone = Optional.ofNullable(event.zone)
                            .orElse("UNKNOWN_ZONE");

                    int fine = TrafficRules.fineCalculator.apply(event.speed);
                    return new ViolationRecord(vehicleId, event.speed, zone, fine);
                })
                .collect(Collectors.toList());

        // ==========================================
        // Consumer (Logging)
        // ==========================================
        Consumer<ViolationRecord> logger = System.out::println;
        violations.forEach(logger);

        // Basic Aggregates
        int totalFine = violations.stream()
                .map(v -> v.fine)
                .reduce(0, Integer::sum);

        long totalViolations = violations.stream().count();

        System.out.println("\nTotal Violations: " + totalViolations);
        System.out.println("Total Fine Collected: Rs. " + totalFine);

        // ==========================================
        // Zone-wise Analytics
        // ==========================================
        Map<String, Long> violationsByZone = violations.stream()
                .collect(Collectors.groupingBy(v -> v.zone, Collectors.counting()));

        System.out.println("\nViolations by Zone:");
        violationsByZone.forEach((zone, count) -> System.out.println(zone + " -> " + count));

        // ==========================================
        // Supplier (Demo: Generate Default Event)
        // ==========================================
//         Supplier<VehicleEvent> defaultEventSupplier = () -> new VehicleEvent("TEST-000", 0.0, "BASE-ZONE", false,
//                 System.currentTimeMillis());

//         VehicleEvent demoEvent = defaultEventSupplier.get();
//         System.out.println("\nGenerated Default Event for ID: " + demoEvent.vehicleId);
    }
}