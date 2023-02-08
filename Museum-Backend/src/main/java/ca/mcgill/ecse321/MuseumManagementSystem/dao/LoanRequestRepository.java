package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;

public interface LoanRequestRepository extends CrudRepository<LoanRequest, Long> {
    LoanRequest findLoanRequestByRequestID(Long requestID);
    List<LoanRequest> findByRequester(Visitor requester);
    void deleteAllByRequester(Visitor requester);
    List<LoanRequest> findLoanRequestByArtwork (Artwork artwork);
    List<LoanRequest> findByRequesterAndArtwork(Visitor requester, Artwork artwork);
    List<LoanRequest> findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(Status status, Artwork artwork, Date currentDate, Date todayDate);
    List<LoanRequest> findAllByRequesterAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(Visitor requester, Artwork artwork, Date currentDate, Date todayDate);

}