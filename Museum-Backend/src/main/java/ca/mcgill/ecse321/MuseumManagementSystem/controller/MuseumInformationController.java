package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.MuseumInformationDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.service.MuseumInformationService;

@CrossOrigin(origins = "*")
@RestController
public class MuseumInformationController {

  @Autowired
  MuseumInformationService museumInformationService;

  @GetMapping (value = {"/museumInformation", "/museumInformation/"})
  public ResponseEntity <?> getMuseumInformation() {
      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        MuseumInformationDto museumInformationDto = convertToDto (museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateVisitorFee","/museumInformation/updateVisitorFee/" })
  public ResponseEntity<?> updateVisitorFee (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationFee(museumInformation, request.getVisitorFee());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateMondayHours","/museumInformation/updateMondayHours/" })
  public ResponseEntity<?> updateMondayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationMondayHours(museumInformation, request.getMondayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }


  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateTuesdayHours","/museumInformation/updateTuesdayHours/" })
  public ResponseEntity<?> updateTuesdayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationTuesdayHours(museumInformation, request.getTuesdayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateWednesdayHours","/museumInformation/updateWednesdayHours/" })
  public ResponseEntity<?> updateWednesdayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationWednesdayHours(museumInformation, request.getWednesdayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateThursdayHours","/museumInformation/updateThursdayHours/" })
  public ResponseEntity<?> updateThursdayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationThursdayHours(museumInformation, request.getThursdayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateFridayHours","/museumInformation/updateFridayHours/" })
  public ResponseEntity<?> updateFridayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationFridayHours(museumInformation, request.getFridayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateSaturdayHours","/museumInformation/updateSaturdayHours/" })
  public ResponseEntity<?> updateSaturdayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationSaturdayHours(museumInformation, request.getSaturdayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /**
   * @author Pratham Bansal
   * @param request
   * @return ResponseEntity<?>
   */
  @PostMapping (value = {"/museumInformation/updateSundayHours","/museumInformation/updateSundayHours/" })
  public ResponseEntity<?> updateSundayHours (@RequestBody MuseumInformationDto request) {

      try {
        MuseumInformation museumInformation = museumInformationService.getMuseumInformation();
        museumInformation = museumInformationService.updateMuseumInformationSundayHours(museumInformation, request.getSundayHours());
        MuseumInformationDto museumInformationDto = convertToDto(museumInformation);
        return new ResponseEntity<>(museumInformationDto, HttpStatus.OK);
      }
      catch (IllegalArgumentException e) 
      {
          return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }

  /* --------------------------------------- Helper Methods ---------------------------------- */
    
  private MuseumInformationDto convertToDto(MuseumInformation museumInformation) {
    if (museumInformation == null) {
          return null;
    }
    return new MuseumInformationDto(museumInformation.getVisitorFee(), museumInformation.getMuseumName(), museumInformation.getMondayHours(),
    museumInformation.getTuesdayHours(), museumInformation.getWednesdayHours(), museumInformation.getThursdayHours(), museumInformation.getFridayHours(), museumInformation.getSaturdayHours(), museumInformation.getSundayHours() );
  }
}
