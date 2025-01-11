package com.example.pupilmed.service;

        import com.example.pupilmed.models.database.owner.Owner;
        import com.example.pupilmed.models.database.pet.Pet;
        import com.example.pupilmed.models.database.user.Role;
        import com.example.pupilmed.models.database.user.User;
        import com.example.pupilmed.models.database.vet.Vet;
        import com.example.pupilmed.models.database.visit.Visit;
        import com.example.pupilmed.repositories.VisitRepository;
        import com.example.pupilmed.models.server.visit.VetVisitDetails;
        import com.example.pupilmed.models.server.visit.VetVisitRequest;
        import com.example.pupilmed.security.jwt.JwtUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;

        import java.sql.Time;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Optional;

@Service
public class VisitService{

    private VisitRepository visitRepository;
    private UserService userService;
    private VetService vetService;
    private final PetService petService;
    private final OwnerService ownerService;
    private final JwtUtils jwtUtils;

    @Autowired
    public VisitService(VisitRepository visitRepository, UserService userService, VetService vetService, PetService petService, OwnerService ownerService, JwtUtils jwtUtils) {
        this.visitRepository = visitRepository;
        this.userService = userService;
        this.vetService = vetService;
        this.petService = petService;
        this.ownerService = ownerService;
        this.jwtUtils = jwtUtils;
    }

//    public Page<Visit> getAllVisits(Pageable pageable){
//        return visitRepository.getAllVisits(pageable);
//    }

    public List<Visit> getAllVisits(){
        return visitRepository.findAll();
    }
    public List<Visit> getVisitsByPetIDAndOwner(int id, Owner owner) {
        return visitRepository.getVisitsByPet_IdAndPet_Owner(id,owner);
    }
    public List<Visit> getVisitsByVetID(int id) {
        return visitRepository.getVisitsByVet_Id(id);
    }

    public void save(Visit visit) {
        visitRepository.save(visit);
    }
    public Optional<Visit> getVisitByID(int id) {
        return visitRepository.getVisitById(id);
    }
    public boolean existsByID(int id){return visitRepository.existsById((long)id);}
    public boolean existsByDateAndVetAndHour(Date date, Vet vet, Time hour){return visitRepository.existsByDateAndVetAndHour(date,vet,hour);}
    public List<Visit> getExistingByDateAndVetAndHour(Date date, Vet vet, Time hour){return visitRepository.getVisitsByVetAndDateAndHour(vet,date,hour);}
    public Optional<Visit> getVisitByIDAndOwner(int id,Owner owner){return visitRepository.getVisitByIdAndPet_Owner(id,owner);}

//    public List<Visit> getVisitsByUsername(String username) {
//        User user = userService.getUserByUsername(username);
//        if(user != null){
//            Vet vet = vetService.getVetByUser(user);
//
//            return visitRepository.getVisitsByVet_Id(vet.getId());
//        }
//        return new ArrayList<>();
//    }

    public ResponseEntity<String> modifyVisit(VetVisitRequest payload) {
            if(payload.visitType() == null || payload.date() == null || payload.hour() == null || payload.price() == null || payload.petName() == null) {
                return new ResponseEntity<>("The fields are missing.", HttpStatus.BAD_REQUEST);
            }

            Optional<Visit> dbV = getVisitByID(payload.id());
            if(dbV.isPresent()) {
                Visit dbVisit = dbV.get();
                Vet vet = dbVisit.getVet();

                ResponseEntity<String> validation = validateVisitData(vet, payload);

                if (validation.getStatusCode() == HttpStatus.OK) {

                    Owner owner = ownerService.getOwnerByUsername(payload.ownerPhoneNumber());
                    Pet pet = petService.getPetByNameAndOwner(payload.petName(), owner);

                    dbVisit.setDate(parseDate(payload.date()));
                    dbVisit.setHour(parseTime(payload.hour()));
                    dbVisit.setPet(pet);
                    dbVisit.setVisitType(payload.visitType());
                    dbVisit.setPrice(payload.price());

                    visitRepository.save(dbVisit);

                    return new ResponseEntity<>("Visit modified successfully.", HttpStatus.OK);
                }
                return new ResponseEntity<>(validation.getBody(), validation.getStatusCode());
            }
        return new ResponseEntity<>("Visit does not exist in database.", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteVisit(Integer visitID){
        if(existsByID(visitID)){
            visitRepository.deleteById((long)visitID);
            return new ResponseEntity<>("Visit deleted successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Visit does not exist.", HttpStatus.NOT_FOUND);
    }
    public List<Visit> getVisitsByUsernameBetweenDates(String username, Date startDate, Date endDate) {
        User user = userService.getUserByUsername(username);
        if(user != null){
            Vet vet = vetService.getVetByUser(user);
            if(vet != null)
                return visitRepository.getVisitsByVet_IdAndDateBetween(vet.getId(),startDate,endDate);

            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public List<Visit> getOwnerVisitsByUsername(String authHeader){
        String username = jwtUtils.getUsernameFromHeader(authHeader);

        Owner owner = ownerService.getOwnerByUsername(username);

        if(owner != null){
            return visitRepository.getVisitsByPet_Owner(owner);
        }
        return new ArrayList<>();
    }

    public List<Visit> getVisitsByOwnerUsernameBetweenDates(String header, String startDate, String endDate) throws ParseException {

        String username = jwtUtils.getUsernameFromHeader(header);

        Date start = parseDate(startDate);
        Date end = parseDate(endDate);

        User user = userService.getUserByUsername(username);
        if (user != null) {
            Owner owner = ownerService.getOwnerByUser(user);
            if (owner != null) {
                List<Pet> pets = petService.getPetsByOwner(owner);

                List<Visit> visits = new ArrayList<>();
                for (Pet pet : pets) {
                    visits.addAll(visitRepository.getVisitsByPet_IdAndDateBetween(pet.getId(), start, end));
                }
                return visits;
            }
        }
        return new ArrayList<>();
    }

    public List<Visit> getVisitSBetweenDates(Date startDate, Date endDate) {
        if(startDate.before(endDate))
            return visitRepository.getVisitByDateBetween(startDate,endDate);
        return null;
    }

    public VetVisitDetails getVisitDetails(int visitID, String header){
        String username = jwtUtils.getUsernameFromHeader(header);
        User user = userService.getUserByUsername(username);
        Optional<Visit> optVisit = Optional.empty();


        if(user.getRole()==Role.ADMIN)
            optVisit = getVisitByID(visitID);
        if(user.getRole()==Role.VET) {
            Vet vet = vetService.getVetByUsername(username);
            if(vet == null)return null;
            optVisit = visitRepository.getVisitsByIdAndVet(visitID,vet);
        }
        if(user.getRole()==Role.OWNER) {
            Owner owner = ownerService.getOwnerByUsername(username);
            if(owner == null)return null;
            optVisit = getVisitByIDAndOwner(visitID,owner);
        }


        if(optVisit.isPresent()) {
            Visit visit = optVisit.get();
            Pet pet = visit.getPet();
            Vet vet = visit.getVet();
            Owner owner = pet.getOwnerID();
            User ownerUser = owner.getUser();
            User vetUser = owner.getUser();
            return new VetVisitDetails(visit.getDate(), visit.getHour(), visit.getVisitType(), owner.getName(),
                    owner.getSurname(), vet.getName(), vet.getSurname(), ownerUser.getUsername(),
                    vetUser.getUsername(), visit.getPrice(), pet.getName(), pet.getType(), pet.getKind(), pet.getAge(), visit.getRecommendation());
        }
        return null;
    }

    public ResponseEntity<String> addVisit(Vet vet, VetVisitRequest payload) {

        ResponseEntity<String> validation = validateVisitData(vet,payload);

        if((validation.hasBody() && validation.getBody().equals("Visit does not exist."))||validation.getStatusCode()==HttpStatus.OK){
            Owner owner = ownerService.getOwnerByUsername(payload.ownerPhoneNumber());
            Pet pet = petService.getPetByNameAndOwner(payload.petName(),owner);
            Visit visit = new Visit(parseDate(payload.date()), parseTime(payload.hour()), payload.visitType(), payload.price(), vet, pet);
            visitRepository.save(visit);
            return new ResponseEntity<>("Visit added successfully.", HttpStatus.OK);
        }
        return validation;
    }

    private ResponseEntity<String> validateVisitData(Vet vet, VetVisitRequest payload) {
        String phoneNumber = payload.ownerPhoneNumber();

        if (!userService.existsByUsernameAndRole(phoneNumber, Role.OWNER)) {
            return new ResponseEntity<>("Owner does not exist.", HttpStatus.NOT_FOUND);
        }
        String petName = payload.petName();
        Owner owner = ownerService.getOwnerByUsername(payload.ownerPhoneNumber());

        if (!petService.existsByNameAndUser(petName, owner))
            return new ResponseEntity<>("Pet is not related to the owner.", HttpStatus.BAD_REQUEST);


        Date date = parseDate(payload.date());
        Time time = parseTime(payload.hour());

        if (date.before(new Date()))
            return new ResponseEntity<>("You can't set date before current date.", HttpStatus.BAD_REQUEST);

        if(payload.id()!= null) {
            Optional<Visit> dbV = getVisitByID(payload.id());
            if (dbV.isEmpty()) return new ResponseEntity<>("Visit does not exist.", HttpStatus.NOT_FOUND);
            if(getExistingByDateAndVetAndHour(date,vet,time).size()>1)
                return new ResponseEntity<>("Date and time are already taken by another visit.", HttpStatus.CONFLICT);
        }
        else if (payload.id() == null){
            if (existsByDateAndVetAndHour(date, vet, time))
                return new ResponseEntity<>("Date and time are already taken by another visit.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }///TODO teraz mozna dodac wizyte w terminie zajetym, gdy podajemy tez id

    private java.sql.Date parseDate(String date) {
        return java.sql.Date.valueOf(date);
    }

    private Time parseTime(String hour){
        try {
            if (hour.matches("^\\d{2}:\\d{2}$")) {
                hour = hour + ":00";
            }
            return Time.valueOf(hour);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid time format. Expected HH:mm or HH:mm:ss.");
        }
    }

}
