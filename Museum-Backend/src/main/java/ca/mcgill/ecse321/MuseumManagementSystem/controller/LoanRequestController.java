package ca.mcgill.ecse321.MuseumManagementSystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.MuseumManagementSystem.dto.ArtworkDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.LoanRequestDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.RoomDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Room;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.service.ArtworkRoomService;
import ca.mcgill.ecse321.MuseumManagementSystem.service.LoanRequestService;
import ca.mcgill.ecse321.MuseumManagementSystem.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class LoanRequestController {

    @Autowired
    LoanRequestService loanRequestService;
    @Autowired
    UserService userService;
    @Autowired
    ArtworkRoomService artworkRoomService;
    
    /** 
     * @author Enzo Calcagno
     * @param id
     * @return ResponseEntity<?>
     */
    @GetMapping(value = {"/loanRequest/{loanRequestId}", "/loanRequest/{loanRequestId}/"})
    public ResponseEntity<?> getLoanRequestByID(@PathVariable(name = "loanRequestId") Long id) {
        try {
            LoanRequest loanRequest = loanRequestService.getLoanRequestById(id);
            LoanRequestDto loanRequestDto = convertToDto(loanRequest);
            return new ResponseEntity<>(loanRequestDto, HttpStatus.OK); 
        }
        catch (Exception e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    
    /** 
     * @author Enzo Calcagno
     * @param request
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/loanRequest", "/loanRequest/"})
    public ResponseEntity<?> createLoanRequest(@RequestBody LoanRequestDto request) {
        try {
            Visitor requester = userService.getVisitorByUsername(request.getRequester().getUsername());
            Artwork artwork = artworkRoomService.getArtworkbyID(request.getArtwork().getArtworkID());
            LoanRequest createdLoanRequest = loanRequestService.createLoanRequest(request.getRequestedStartDate() , request.getRequestedEndDate(), requester, artwork);
            LoanRequestDto loanRequestDto = convertToDto(createdLoanRequest);
            return new ResponseEntity<>(loanRequestDto, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /** 
     * @author Enzo Calcagno
     * @param id
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/loanRequest/{loanRequestId}/approveLoanRequest", "/loanRequest/{loanRequestId}/approveLoanRequest/"})
    public ResponseEntity<?> approveLoanRequest(@PathVariable(name = "loanRequestId") Long id) {
        LoanRequest loanRequest = null;
        try {

            loanRequest = loanRequestService.getLoanRequestById(id);
            LoanRequest approvedLoanRequest = loanRequestService.approveLoanRequest(loanRequest);
            LoanRequestDto loanRequestDto = convertToDto(approvedLoanRequest);
            return new ResponseEntity<>(loanRequestDto, HttpStatus.OK);
        }
        catch (Exception e) {
            //return non-updated loanRequest
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /** 
     * @author Enzo Calcagno
     * @param id
     * @return ResponseEntity<?>
     */
    @PostMapping(value = {"/loanRequest/{loanRequestId}/denyLoanRequest", "/loanRequest/{loanRequestId}/denyLoanRequest/"})
    public ResponseEntity<?> denyLoanRequest(@PathVariable(name = "loanRequestId") Long id) {
        LoanRequest loanRequest = null;
        try {
            loanRequest = loanRequestService.getLoanRequestById(id);
            LoanRequest deniedLoanRequest = loanRequestService.denyLoanRequest(loanRequest);
            LoanRequestDto loanRequestDto = convertToDto(deniedLoanRequest);
            return new ResponseEntity<>(loanRequestDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            //return non-updated loanRequest
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }

    @GetMapping(value = {"/loanRequests", "/loanRequests/"})
    public ResponseEntity<?> getAllLoanRequests() {
        try {
            List<LoanRequest> loanRequests = loanRequestService.getAllLoanRequests();
            List<LoanRequestDto> loanRequestsDto = new ArrayList<>();
            for (LoanRequest loanRequest: loanRequests) {
                loanRequestsDto.add(convertToDto(loanRequest));
            }
            return new ResponseEntity<>(loanRequestsDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/loanRequests/{username}" , "/loanRequests/{username}/"})
    public ResponseEntity<?> getVisitorLoanRequests(@PathVariable(name = "username") String username) {
        try {
            Visitor requester = userService.getVisitorByUsername(username);
            List<LoanRequest> loanRequests = loanRequestService.getVisitorLoanRequests(requester);
            List<LoanRequestDto> loanRequestsDto = new ArrayList<>();
            for (LoanRequest loanRequest: loanRequests) {
                loanRequestsDto.add(convertToDto(loanRequest));
            }
            return new ResponseEntity<>(loanRequestsDto, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* --------------------------------------- Helper Methods ---------------------------------- */
    private LoanRequestDto convertToDto(LoanRequest loanRequest) {
        if (loanRequest == null) {
            throw new IllegalArgumentException("Loan request does not exist");
        }
        LoanRequestDto loanRequestDto = new LoanRequestDto(loanRequest.getRequestID(), loanRequest.getRequestedStartDate(), loanRequest.getRequestedEndDate(), convertToDto(loanRequest.getRequester()), convertToDto(loanRequest.getArtwork()));
        loanRequestDto.setStatus(loanRequest.getStatus()); // use model's status to set DTO status
        return loanRequestDto;
    }  

    private ArtworkDto convertToDto(Artwork artwork) {
        if (artwork == null) {
            throw new IllegalArgumentException("artwork does not exist");
        }
        ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(), artwork.getArtist(), artwork.getDescription(), artwork.getYear(),artwork.getLoanable(), artwork.getImgUrl(),convertToDto(artwork.getRoom()));
        return artworkDto;
    }  

    private VisitorDto convertToDto(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor does not exist");
        }
        VisitorDto visitorDto = new VisitorDto(visitor.getUsername(), visitor.getPassword());
        return visitorDto;
    }  
    
    private RoomDto convertToDto(Room room) {
		if (room == null) {
			return null;
		}

		return new RoomDto(room.getRoomId(), room.getRoomSize(),room.getRoomType());

	}
    
}