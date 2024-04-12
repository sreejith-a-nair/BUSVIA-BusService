package com.busservice.service;

import com.busservice.client.DriverClient;
import com.busservice.entity.*;
import com.busservice.model.*;
import com.busservice.repo.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusServiceImp implements  BusService {

    @Autowired
    BusRepo busRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    DriverClient driverClient;
    @Autowired
    RootAndTimeRepo rootAndTimeRepo;

    @Autowired
    RatingRepo ratingRepo;

    @Autowired
    BusStopRepo busStopRepo;

    @Autowired
    SeatsRowRepo seatRowRepo;


    @Override
    public BusEntity addBus(BusEntity busData) {
        busData.setAvailable(true);
        BusEntity bus = busRepo.save(busData);

        if (bus != null) {
            BusRequest busRequest = new BusRequest(
                    bus.getUuid(),
                    bus.getBusName(),
                    bus.getBusNumber(),
                    bus.getBusType(),
                    bus.getTotalSeats(),
                    bus.getAvailableSeats(),
                    bus.getFare(),
                    bus.getCategory(),
                    bus.getDoubleSeatCount(),
                    bus.getThirdRowSeatCount(),
                    bus.getEmail(),
                    bus.getUpperSeat(),
                    bus.getLowerSeat()
            );
            String response = driverClient.addBus(busRequest);
            System.out.println("Response from UserService: " + response);
        }

        return bus;

    }

    @Override
    public List<BusEntity> getALlBus() {

        List<BusEntity> buses = busRepo.findAll();
        System.out.println("All Buses : " + buses);
        return buses;
    }

    @Override
    public List<BusEntity> getALlBusByMail(String email) {
        return busRepo.findAllByEmail(email);
    }

    @Override
    public BusEntity updateBus(BusEntity busData, UUID uuid) {

        Optional<BusEntity> busDetails = busRepo.findById(uuid);
        BusEntity updatedBus = null;
        if (busDetails.isPresent()) {
            BusEntity bus = busDetails.get();
            bus.setBusName(busData.getBusName());
            bus.setBusNumber(busData.getBusNumber());
            bus.setBusType(busData.getBusType());
            bus.setDoubleSeatCount(busData.getDoubleSeatCount());
            bus.setTotalSeats(busData.getTotalSeats());
            bus.setUpperSeat(busData.getUpperSeat());
            bus.setLowerSeat(busData.getLowerSeat());
            bus.setAvailableSeats(busData.getAvailableSeats());
            bus.setThirdRowSeatCount(busData.getThirdRowSeatCount());
            updatedBus = busRepo.save(bus);
            if (updatedBus != null) {
                BusRequest busRequest = new BusRequest(
                        updatedBus.getUuid(),
                        updatedBus.getBusName(),
                        updatedBus.getBusNumber(),
                        updatedBus.getBusType(),
                        updatedBus.getTotalSeats(),
                        updatedBus.getAvailableSeats(),
                        updatedBus.getFare(),
                        updatedBus.getCategory(),
                        updatedBus.getDoubleSeatCount(),
                        updatedBus.getThirdRowSeatCount(),
                        updatedBus.getEmail(),
                        updatedBus.getUpperSeat(),
                        updatedBus.getLowerSeat()
                );
                String response = driverClient.updateBus(busRequest);
                System.out.println("Update bus http client passed data successfully: " + response);
            }
        } else {
            return null;
        }
        return updatedBus;
    }

    @Override
    public BusRootAndTime updateRootAndTime(BusRootAndTime root, UUID uuid) {
        System.out.println("Update root "+root);
        Optional<BusRootAndTime> busRootAndTimeInfo = rootAndTimeRepo.findById(uuid);
        BusRootAndTime updatedRoot = null;
        if (busRootAndTimeInfo.isPresent()) {
            BusRootAndTime busRoot = busRootAndTimeInfo.get();
            busRoot.setDestinationLocation(root.getDestinationLocation());
            busRoot.setSourceLocation(root.getSourceLocation());
            busRoot.setPerdayTrip(root.getPerdayTrip());
            busRoot.setTotalHour(root.getTotalHour());
            busRoot.setDepartureDate(root.getDepartureDate());
            busRoot.setArrivalDate(root.getArrivalDate());
            busRoot.setArrivalTime(root.getArrivalTime());
            busRoot.setDepartureTime(root.getDepartureTime());
            busRoot.setRootType(root.getRootType());
//             updateDepartureDate();
            updatedRoot = rootAndTimeRepo.save(busRoot);
            if (updatedRoot != null) {
                BusRootReq busRootReq = new BusRootReq(
                        updatedRoot.getUuid(),
                        updatedRoot.getSourceLocation(),
                        updatedRoot.getDestinationLocation(),
                        updatedRoot.getDepartureTime(),
                        updatedRoot.getArrivalTime(),
                        updatedRoot.getDepartureDate(),
                        updatedRoot.getArrivalDate(),
                        updatedRoot.getTotalHour(),
                        updatedRoot.getPerdayTrip(),
                        updatedRoot.getRootType()
                );
                String response = driverClient.updateRoot(busRootReq);
                System.out.println("Update bus http client passed data successfully: " + response);
            }
        } else {
            return null;
        }
        return updatedRoot;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDepartureDate() {

        List<BusRootAndTime> regularBusRoots = rootAndTimeRepo.findByRootType("regular");
        System.out.println("REGULAR ROOT BUS * * * "+regularBusRoots);

        for (BusRootAndTime busRoot : regularBusRoots) {

            LocalDate currentDate = LocalDate.now();
            LocalDate departureDate = busRoot.getDepartureDate().toLocalDate();
            LocalDate arrivalDate = busRoot.getArrivalDate().toLocalDate();

            if (currentDate.isAfter(departureDate)) {
                continue;

            }else {
                try {
                    System.out.println("tryy ");
                    LocalDate newDepartureDate = calculateNewDepartureDate(departureDate);
                    LocalDate newArrivalDate = calculateNewArrivalDate(LocalDate.now());
                    System.out.println("newDepartureDate "+newArrivalDate);
                    System.out.println("newDepartureDate "+newDepartureDate);
                    busRoot.setDepartureDate(java.sql.Date.valueOf(LocalDate.now()));
                    busRoot.setArrivalDate(java.sql.Date.valueOf(LocalDate.now()));
                } catch (Exception e) {
                    System.out.println(" Local date convert issues " + e);
                }


                BusRootAndTime updatedRoot=rootAndTimeRepo.save(busRoot);

                if (updatedRoot != null) {
                    BusRootReq busRootReq = new BusRootReq(
                            updatedRoot.getUuid(),
                            updatedRoot.getSourceLocation(),
                            updatedRoot.getDestinationLocation(),
                            updatedRoot.getDepartureTime(),
                            updatedRoot.getArrivalTime(),
                            updatedRoot.getDepartureDate(),
                            updatedRoot.getArrivalDate(),
                            updatedRoot.getTotalHour(),
                            updatedRoot.getPerdayTrip(),
                            updatedRoot.getRootType()
                    );
                    String response = driverClient.updateRoot(busRootReq);
                    System.out.println("AUTOMATIC  UPDATE  DEPARTURE pass  successfully: " + response);
                }

                System.out.println("AUTOMATIC  UPDATE  DEPARTURE  DATE for bus root *  *  * : " + updatedRoot);
            }
        }
    }



    // Define the method to calculate the new departure date
    private LocalDate calculateNewDepartureDate(LocalDate oldDepartureDate) {
        return oldDepartureDate.plusDays(1);
    }
    private LocalDate calculateNewArrivalDate(LocalDate newDepartureDate) {
        return newDepartureDate.plusDays(1);
    }


    @Override
    public void deleteBus(UUID busId) {
        busRepo.deleteById(busId);
    }

    @Override
    public String saveUser(UserRequest userRequest) {

        /* password encrypted in db*/
        OperatorEntity user = new OperatorEntity();
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setUuid(userRequest.getUuid());
        user.setRole(userRequest.getRole());
        user.setFirstName(userRequest.getFirstName());
        user.setAuthorityName(userRequest.getAuthorityName());
        user.setContact(userRequest.getContact());
        userRepo.save(user);
        return "USER Registration success in bus service ";

    }

    @Override
    public BusRootAndTime addBusRootAndTime(BusRootRequest busRoot) {
        BusRootAndTime busRootAndTime = new BusRootAndTime();
        System.out.println("departure time " + busRootAndTime.getDepartureDate());
        System.out.println("arrival time " + busRootAndTime.getArrivalTime());


        UUID uuid = busRoot.getBusId();
        Optional<BusEntity> bus = busRepo.findById(uuid);
        BusEntity busData = bus.get();

        busRootAndTime.setUuid(busRoot.getUuid());
        busRootAndTime.setDepartureTime(busRoot.getDepartureTime());
        busRootAndTime.setArrivalTime(busRoot.getArrivalTime());
        busRootAndTime.setArrivalDate(busRoot.getArrivalDate());
        busRootAndTime.setDepartureDate(busRoot.getDepartureDate());
        busRootAndTime.setSourceLocation(busRoot.getSourceLocation());
        busRootAndTime.setDestinationLocation(busRoot.getDestinationLocation());
        busRootAndTime.setTotalHour(busRoot.getTotalHour());
        busRootAndTime.setPerdayTrip(busRoot.getPerdayTrip());
        busRootAndTime.setBusInfo(busData);
        busRootAndTime.setBusId(busRoot.getBusId());
        busRootAndTime.setRootType(busRoot.getRootType());

        BusRootAndTime busRoots = rootAndTimeRepo.save(busRootAndTime);

        UUID busId = busRoot.getBusId();


        return busRoots;
    }

    @Override
    @Transactional
    public BusStopEntity addBusStop(BusStopRequest busStopRequest) {


        if (busStopRequest != null && busStopRequest.getRouteId() != null) {

            BusRootAndTime busRootAndTime = rootAndTimeRepo.findById(UUID.fromString(busStopRequest.getRouteId())).orElse(null);
            if (busRootAndTime != null) {
                BusStopEntity busStopEntity = new BusStopEntity();
                busStopEntity.setRoute(busRootAndTime);
                busStopEntity.setStopName(busStopRequest.getStopName());
                busStopEntity.setSequenceNumber(busStopRequest.getSequenceNumber());

                busStopRepo.save(busStopEntity);
            } else {

                throw new IllegalArgumentException("Invalid Route ID");
            }
        } else {

            throw new IllegalArgumentException("Invalid request");
        }



        return null;

    }

    @Override
    public List<BusEntity> getSearchedBus(SearchRequest searchRequest) {
        LocalDate currentDate = LocalDate.now().atStartOfDay().toLocalDate();
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<BusRootAndTime> busRootAndTimeList = rootAndTimeRepo.findBySourceLocationAndDestinationLocationAndDepartureDate(searchRequest.getDeparturePlace(),
                searchRequest.getArrivalPlace(),
                searchRequest.getDepartureDate());
        if (!busRootAndTimeList.isEmpty()) {
//            List<BusEntity> busEntities = busRootAndTimeList.stream()
//                    .map(BusRootAndTime::getBusInfo)
//                    .distinct()
//                    .collect(Collectors.toList());
//            List<BusEntity> busEntities = busRootAndTimeList.stream()
//                    .filter(busRootAndTime -> {
//                        LocalDate departureLocalDate = busRootAndTime.getDepartureDate().
//                                toInstant()
//                                .atZone(ZoneId
//                                .systemDefault())
//                                .toLocalDate();
//                        return departureLocalDate.isAfter(currentDate);
//                    })
//                    .map(BusRootAndTime::getBusInfo)
//                    .distinct()
//                    .collect(Collectors.toList());

            List<BusEntity> busEntities = busRootAndTimeList.stream()
                    .filter(busRootAndTime -> {
                        // Convert departure time to LocalDateTime
                        LocalDateTime departureDateTime = LocalDateTime.of(busRootAndTime.getDepartureDate().toLocalDate(),
                                LocalTime.parse(busRootAndTime.getDepartureTime()));

                        // Check if departure time is after current time
                        return departureDateTime.isAfter(currentDateTime);
                    })
                    .map(BusRootAndTime::getBusInfo)
                    .distinct()
                    .collect(Collectors.toList());
            System.out.println("Bus list after filter after current time buses only "+busEntities);

            return busEntities;


//            return busEntities;
        } else {
            List<BusEntity> busEntities = new ArrayList<>();

            return busEntities;
        }

    }

    @Override
    public List<BusSearchData> getSearchedBuses(SearchRequest searchRequest) {
        List<BusRootAndTime> busRootAndTimeList = rootAndTimeRepo.findBySourceLocationAndDestinationLocationAndDepartureDate(
                searchRequest.getDeparturePlace(),
                searchRequest.getArrivalPlace(),
                searchRequest.getDepartureDate());

        LocalDateTime currentDateTime = LocalDateTime.now();




        // Filter buses with departure time after current time
//        List<BusRootAndTime> filteredBuses = busRootAndTimeList.stream()
//                .filter(busRootAndTime -> {
//                    // Convert departure time to LocalDateTime
//                    LocalDateTime departureDateTime = LocalDateTime.of(
//                            busRootAndTime.getDepartureDate().toLocalDate(),
//                            LocalTime.parse(
//                                    busRootAndTime.getDepartureTime(),
//                                    DateTimeFormatter.ofPattern("hh:mm a")
//                            )
//                    );
//
//                    // Convert the time to 24-hour format if it's PM
//                    if (busRootAndTime.getDepartureTime().endsWith("PM")) {
//                        departureDateTime = departureDateTime.plusHours(12);
//                    }
//
//                    // Check if departure time is after current time
//                    return departureDateTime.isAfter(currentDateTime);
//                })
//                .collect(Collectors.toList());
//        System.out.println("Filtered bus list based on after the current time "+filteredBuses);

        List<BusSearchData> busSearchDataList = new ArrayList<>();

        for (BusRootAndTime busRootAndTime : busRootAndTimeList) {
            BusEntity bus = busRootAndTime.getBusInfo();

            BusSearchData busSearchData = new BusSearchData();

            busSearchData.setArrivalTime(busRootAndTime.getArrivalTime());
            busSearchData.setArrivalDate(busRootAndTime.getArrivalDate());
            busSearchData.setDepartureTime(busRootAndTime.getDepartureTime());
            busSearchData.setDepartureDate(busRootAndTime.getDepartureDate());
            busSearchData.setDestinationLocation(busRootAndTime.getDestinationLocation());
            busSearchData.setSourceLocation(busRootAndTime.getSourceLocation());
            busSearchData.setTotalHour(busRootAndTime.getTotalHour());
            busSearchData.setBusName(bus.getBusName());
            busSearchData.setBusNumber(bus.getBusNumber());
            busSearchData.setBusType(bus.getBusType());
            busSearchData.setCategory(bus.getCategory());
            busSearchData.setEmail(bus.getEmail());
            busSearchData.setAvailableSeats(bus.getAvailableSeats());
            busSearchData.setFare(bus.getFare());
            busSearchData.setDoubleSeatCount(bus.getDoubleSeatCount());
            busSearchData.setThirdRowSeatCount(bus.getThirdRowSeatCount());
            busSearchData.setTotalSeats(bus.getTotalSeats());
            busSearchData.setUuid(bus.getUuid());
            busSearchData.setAvailable(bus.isAvailable());
            Rating rating = ratingRepo.findByBusId(bus.getUuid());
            if (rating != null) {
                busSearchData.setRating(rating);
            }
            System.out.println("RATING WHEN SEARCH "+rating);
//            busSearchData.setRating(rating);
            busSearchDataList.add(busSearchData);

        }
        System.out.println();

        return busSearchDataList;
    }

    @Override
    public List<BusSearchData> filterByFeature(FeatureRequestForSearch featureRequestForSearch) {
        String filterValue=featureRequestForSearch.getCategory();
        List<BusEntity> busEntity = busRepo.findByCategory(filterValue);
        System.out.println("BUS DATA  :  "+ busEntity);

        List<BusSearchData> busSearchData = new ArrayList<>();

        if (busEntity != null) {
            for (BusEntity bus : busEntity) {
                BusSearchData busSearchDataList = new BusSearchData();
                busSearchDataList.setUuid(bus.getUuid());
                busSearchDataList.setBusType(bus.getBusType());
                busSearchDataList.setBusName(bus.getBusName());
                busSearchDataList.setBusNumber(bus.getBusNumber());
                busSearchDataList.setFare(bus.getFare());
                busSearchDataList.setEmail(bus.getEmail());
                busSearchDataList.setCategory(bus.getCategory());
                busSearchDataList.setThirdRowSeatCount(bus.getThirdRowSeatCount());
                busSearchDataList.setDoubleSeatCount(bus.getDoubleSeatCount());
                busSearchDataList.setTotalSeats(bus.getTotalSeats());
                busSearchDataList.setAvailableSeats(bus.getAvailableSeats());
                busSearchDataList.setLowerSeat(bus.getLowerSeat());
                busSearchDataList.setUpperSeat(bus.getUpperSeat());

                List<BusRootAndTime> busRootAndTimeList = rootAndTimeRepo.findByBusId(bus.getUuid());
                if (busRootAndTimeList != null && !busRootAndTimeList.isEmpty()) {
                    BusRootAndTime busRootAndTime = busRootAndTimeList.get(0);
                    busSearchDataList.setSourceLocation(busRootAndTime.getSourceLocation());
                    busSearchDataList.setDestinationLocation(busRootAndTime.getDestinationLocation());
                    busSearchDataList.setDepartureTime(busRootAndTime.getDepartureTime());
                    busSearchDataList.setArrivalTime(busRootAndTime.getArrivalTime());
                    busSearchDataList.setArrivalDate(busRootAndTime.getArrivalDate());
                    busSearchDataList.setDepartureDate(busRootAndTime.getDepartureDate());
                    busSearchDataList.setTotalHour(busRootAndTime.getTotalHour());

                    busSearchData.add(busSearchDataList);
                }
            }
        }

        return busSearchData;
    }
    public List<BusSearchData> getBusRootAndTimeBetweenTimePeriod(String startTime, String endTime) {
        List<BusRootAndTime> busRootAndTime=rootAndTimeRepo.findByDepartureTimeBetween(startTime, endTime);
        System.out.println("busRootAndTime "+ busRootAndTime);
        List<BusSearchData> busSearchData = new ArrayList<>();

        if(!busRootAndTime.isEmpty()){
            for (BusRootAndTime busRoot :busRootAndTime) {
                BusEntity bus = busRoot.getBusInfo();
                BusSearchData busResponse = new BusSearchData();

                if (isDepartureTimeValid(busRoot.getDepartureTime(), startTime, endTime)) {

                    busResponse.setDepartureTime(busRoot.getDepartureTime());
                    busResponse.setArrivalTime(busRoot.getArrivalTime());
                    busResponse.setDestinationLocation(busRoot.getDestinationLocation());
                    busResponse.setSourceLocation(busRoot.getSourceLocation());
                    busResponse.setDepartureDate(busRoot.getDepartureDate());
                    busResponse.setArrivalDate(busRoot.getArrivalDate());
                    busResponse.setTotalHour(busRoot.getTotalHour());
                    busResponse.setBusName(bus.getBusName());
                    busResponse.setBusType(bus.getBusType());
                    busResponse.setBusNumber(bus.getBusNumber());
                    busResponse.setFare(bus.getFare());
                    busResponse.setEmail(bus.getEmail());
                    busResponse.setCategory(bus.getCategory());
                    busResponse.setAvailableSeats(bus.getAvailableSeats());
                    busResponse.setDoubleSeatCount(bus.getDoubleSeatCount());
                    busResponse.setThirdRowSeatCount(bus.getThirdRowSeatCount());
                    busResponse.setTotalSeats(bus.getTotalSeats());


                    busSearchData.add(busResponse);
                }

            }
        }
        return busSearchData;
    }

    private boolean isDepartureTimeValid(String departureTime, String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        try {
            Date departure = sdf.parse(departureTime);
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            System.out.println("departure Time : "+departure + "start "+startTime + "end "+end );
            return !departure.before(start) && !departure.after(end);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    //    @Override
//    public BusEntity findBusById(UUID busUuid) {
//      Optional<BusEntity> busEntityList = busRepo.findById(busUuid);
//      BusEntity busData = busEntityList.get();
//        return busData;
//    }
    @Override
    public BusResponse findBusById(UUID busUuid) {
        Optional<BusEntity> busEntityList = busRepo.findById(busUuid);
        BusEntity busData = busEntityList.get();
        BusResponse busResponse = new BusResponse();
        busResponse.setBusNumber(busData.getBusNumber());
        busResponse.setBusName(busData.getBusName());
        busResponse.setCategory(busData.getCategory());
        busResponse.setFare(busData.getFare());
        busResponse.setSeatsRows(busData.getSeatsRows());
        busResponse.setEmail(busData.getEmail());
        busResponse.setTotalSeats(busData.getTotalSeats());
        busResponse.setDoubleSeatCount(busData.getDoubleSeatCount());
        busResponse.setThirdRowSeatCount(busData.getThirdRowSeatCount());
        busResponse.setSeatsRows(busData.getSeatsRows());
        busResponse.setUpperSeat(busData.getUpperSeat());
        busResponse.setLowerSeat(busData.getLowerSeat());


        return busResponse;
    }


    @Override
    public List<SeatsRows> addSeatRows(Test seatRow) {
        UUID busId = UUID.fromString(seatRow.getBusId());
        BusEntity bus = busRepo.findById(busId).orElseThrow();

        List<SeatsRows> seatsRowsList = new ArrayList<>();

        for (SeatRowDto seatRowDto : seatRow.getSeatRows()) {
            SeatsRows seatsRows = new SeatsRows();
            seatsRows.setSeatRow(seatRowDto.getRow());
            seatsRows.setNumberOfSeats(seatRowDto.getNumberOfSeats());

            // Save each seat row individually
            SeatsRows savedSeatRow = seatRowRepo.save(seatsRows);
            seatsRowsList.add(savedSeatRow);
        }

        // Update the bus entity with the list of saved seat rows
        bus.setSeatsRows(seatsRowsList);
        busRepo.save(bus);

        System.out.println("Seat rows in service: " + seatsRowsList);
        System.out.println("Bus entity with updated seat rows: " + bus);

        return seatsRowsList;
    }

    @Override
    public BusRootAndTime findBusRouteByBusId(UUID busUuid) {
        List<BusRootAndTime> busRootAndTimeOptional = rootAndTimeRepo.findByBusId(busUuid);

        // Check if the BusRootAndTime object exists
        if (busRootAndTimeOptional != null) {

            return busRootAndTimeOptional.get(0);

        } else {

            return null;
        }
    }

    @Override
    public void unblockBus(UUID busUuid) {
        Optional<BusEntity>  busData = busRepo.findById(busUuid);
        if (busData.isPresent()) {
            BusEntity bus = busData.get();
            bus.setAvailable(true);
            BusEntity busInfo   =   busRepo.save(bus);
            BusBlockRequest request = new BusBlockRequest(bus.getUuid(), true);

            String response = driverClient.unblockBus(request);
            System.out.println("Response from user service: " + response);

        } else {
            throw new EntityNotFoundException("Bus not found with ID: " + busUuid);
        }

    }

    @Override
    public void blockBus(UUID busUuid) {

        Optional<BusEntity>  busData = busRepo.findById(busUuid);
        if (busData.isPresent()) {
            BusEntity bus = busData.get();
            bus.setAvailable(false);
            BusEntity busInfo   =   busRepo.save(bus);
            BusInfo busInfo1 = new BusInfo();
            busInfo1.setUuid(busInfo.getUuid());

            BusBlockRequest request = new BusBlockRequest(bus.getUuid(), false);
            String response = driverClient.blockBus(request);
            System.out.println("Response from  block bus " + response);

        } else {
            throw new EntityNotFoundException("Bus not found with ID: " + busUuid);
        }


    }

    @Override
    public void updateAvailableSeatAfterBooking(UUID busUuid, int bookedSeatCount) {
        BusEntity bus = busRepo.findById(busUuid)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found with ID: " + busUuid));

        System.out.println("before update booking "+bus.getAvailableSeats());
        int currentAvailableSeats = bus.getAvailableSeats();
        if (currentAvailableSeats >= bookedSeatCount) {
            bus.setAvailableSeats(currentAvailableSeats - bookedSeatCount);
            System.out.println("After set  update "+bus.getAvailableSeats());
         BusEntity busEntity  = busRepo.save(bus);
            System.out.println("After update "+busEntity.getAvailableSeats());
        } else {
            throw new IllegalArgumentException("Not enough available seats");
        }
    }

    @Override
    public void updateAvailableSeatAfterCancel(UUID busUuid, int canceledSeatCount) {
        System.out.println("before update cancel "+canceledSeatCount);
        BusEntity bus = busRepo.findById(busUuid)
                .orElseThrow(() -> new EntityNotFoundException("Bus not found with ID: " + busUuid));
        System.out.println("before update cancel "+bus);

        int currentAvailableSeats = bus.getAvailableSeats();
        if (currentAvailableSeats >= canceledSeatCount) {
            System.out.println("current seat  "+bus.getAvailableSeats());
            System.out.println("updated seat  "+currentAvailableSeats + canceledSeatCount);
            bus.setAvailableSeats(currentAvailableSeats + canceledSeatCount);
            System.out.println("After set  update cancel "+bus.getAvailableSeats());
            BusEntity busEntity  = busRepo.save(bus);
            System.out.println("After update cancel "+busEntity.getAvailableSeats());
        } else {
            throw new IllegalArgumentException("Not enough available seats cancel");
        }
    }

    @Override
    public Rating addRating(int ratingValue, String bookingId, String busId) {
        UUID bookingUuid= UUID.fromString(bookingId);
        System.out.println("BOOKING ID "+bookingUuid);
        UUID busUuid = UUID.fromString(busId);
        Optional<Rating> bookId = ratingRepo.findByBookingId(bookingUuid);

        if(bookId.isPresent()|| busId==null){
            System.out.println("IF ");
//            System.out.println("BOOKING ID IS PRESENT ALREADY ADDED RATING");
            Rating existRating = bookId.get();
            System.out.println("BOOKING ID IS PRESENT ALREADY ADDED RATING "+existRating);

            return existRating;

        }else{
            System.out.println("ELSE ");
            Rating rating = ratingRepo.findByBusId(busUuid);
          if (rating == null) {
            rating = new Rating();
            rating.setRatingValue(ratingValue);
            rating.setRatingCount(1);
            rating.setAvgRating(ratingValue);
//            rating.setUserId(null); // Set the user if needed
            rating.setBusId(busUuid);
            rating.setBookingId(bookingUuid);
            BusEntity bus =busRepo.findById(busUuid).orElseThrow(()-> new RuntimeException("Bus not found "));
            rating.setBus(bus);

            rating = ratingRepo.save(rating);
        } else {

            int totalRatingValue = rating.getRatingValue() * rating.getRatingCount();
            int newTotalRatingValue = totalRatingValue + ratingValue;
            rating.setRatingCount(rating.getRatingCount() + 1);
            rating.setAvgRating((double) newTotalRatingValue / rating.getRatingCount());

            rating = ratingRepo.save(rating);
        }
        return rating;
        }
    }

    @Override
    public Rating getBusRatingByBusId(String busId) {
        UUID busUuid= UUID.fromString(busId);
       Rating rating = ratingRepo.findByBusId(busUuid);
        System.out.println("Rating by busId "+rating);
        return rating;
    }
}






