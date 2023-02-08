package ca.mcgill.ecse321.MuseumManagementSystem.service;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;

@Service
public class LoanRequestService {
    
    @Autowired
    LoanRequestRepository loanRequestRepository;
    @Autowired
    VisitorRepository visitorRepository;

 
    /**
     * @author Enzo Calcagno
     * @brief method to create a loan request
     * @param aRequestedStartDate
     * @param aRequestedEndDate
     * @param aRequester
     * @param aArtwork
     * @return LoanRequest - created LoanRequest
     */
    @Transactional
    public LoanRequest createLoanRequest(Date aRequestedStartDate, Date aRequestedEndDate, Visitor aRequester, Artwork aArtwork) {
        
        String err = "";

        if (aRequester == null) {
            err += "Must select a visitor\n";
        } 
        
        if (aRequestedEndDate == null || aRequestedStartDate == null) {
            err += "Must specify an end and start date for the request\n";
        } else { // both dates non null
            if (aRequestedEndDate.before(aRequestedStartDate)) { // invalid dates
                err += "End is before start date\n";
            } 
            if (aRequestedEndDate.before(new Date(System.currentTimeMillis())) || aRequestedStartDate.before(new Date(System.currentTimeMillis()))) { // invalid dates
                err += "Loan request dates cannot be past!\n";
            }
        }

        if (aArtwork == null) {
            err += "Must select an artwork\n";
        } else { // if not loanable
            if (!aArtwork.getLoanable()) {
                err += "Artwork is not loanable\n";
            }
        } 
        
        if (err.length() > 0) {
            throw new IllegalArgumentException(err);
        }

        // check if same requester has overlapping 
        List<LoanRequest> existingLoanRequests= loanRequestRepository
        .findAllByRequesterAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(aRequester, aArtwork, aRequestedEndDate, aRequestedStartDate); //
        
        if (existingLoanRequests.size() != 0) {           
            err += "You have overlapping loan requests for same artwork\n";
        }
        
        // loanRequest already exists (not priority)
        
        if (err.length() > 0) {
            throw new IllegalArgumentException(err);
        }

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setRequester(aRequester);
        loanRequest.setArtwork(aArtwork);
        loanRequest.setRequestedEndDate(aRequestedEndDate);
        loanRequest.setRequestedStartDate(aRequestedStartDate);
        loanRequest.setStatus(Status.Pending);
        return loanRequestRepository.save(loanRequest);
    }


    /**
     * @author Enzo Calcagno
     * @brief method to approve a loan request
     * @param approvalDate
     * @param loanRequest
     * @return LoanRequest-The approved LoanRequest
     */
    @Transactional
    public LoanRequest approveLoanRequest(LoanRequest loanRequest) {
        String err = "";
        
        if (loanRequest == null) {
            err += "Must approve an exisiting loan Request\n";
        } else {
            if (!loanRequest.getStatus().equals(Status.Pending)) {
                err += "Fate of this loan request has already been decided\n";
            } 
            if (!loanRequest.getArtwork().getLoanable()) { // if not loanable
                err += "Artwork is not loanable\n";
            } 
            Date requestedStartDate = loanRequest.getRequestedStartDate();
            if ((new Date(System.currentTimeMillis())).after(requestedStartDate)) {
                err += "Approval date had to occur before requested date. Must be denied.\n";
            } 
            // check if there are approved overlapping requests
            List<LoanRequest> overlappingRequests = loanRequestRepository.findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual
            (Status.Approved, loanRequest.getArtwork(), loanRequest.getRequestedEndDate() , loanRequest.getRequestedStartDate());
            if (overlappingRequests.size() != 0) {
                err += "Artwork is reserved for that period of time\n";
            }
            
            if (err.length() > 0) {
                throw new IllegalArgumentException(err);
            }
            loanRequest.setStatus(Status.Approved);
            return loanRequestRepository.save(loanRequest);
        }
        throw new IllegalArgumentException(err);                  
    }

    /**
     * @author Enzo Calcagno
     * @brief denies given loan request
     * @param loanRequest
     * @return LoanRequest-The denied LoanRequest
     */
    @Transactional
    public LoanRequest denyLoanRequest(LoanRequest loanRequest) {
        String err = "";
        if (loanRequest == null) {
            err += "Must specify an actual request\n";
        } else {
            if (!loanRequest.getStatus().equals(Status.Pending)) {
                err += "Fate of this loan request has already been decided\n";
            }
            if (err.length() > 0) {
                throw new IllegalArgumentException(err);
            }
            loanRequest.setStatus(Status.Rejected);
            return loanRequestRepository.save(loanRequest);
        }
        throw new IllegalArgumentException(err);  
    }

    /**
     * @author Enzo Calcagno
     * @brief gets all loan requests
     * @return list of all persisted loan requests
     */
    @Transactional
    public List<LoanRequest> getAllLoanRequests() {
        return toList(loanRequestRepository.findAll());
    }

    /* Helper Methods */
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

    /**
     * @author Enzo Calcagno
     * @brief gets all loan requests
     * @return list of all persisted loan requests
     */
    @Transactional
    public List<LoanRequest> getVisitorLoanRequests(Visitor requester) {
        return toList(loanRequestRepository.findByRequester(requester));
    }

    @Transactional
    public LoanRequest getLoanRequestById(Long id) {
        LoanRequest loanRequest = loanRequestRepository.findLoanRequestByRequestID(id);
        return loanRequest;
    }
}