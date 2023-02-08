package ca.mcgill.ecse321.MuseumManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;

@ExtendWith(MockitoExtension.class)
public class TestLoanRequestService {
   
    @Mock
    private LoanRequestRepository loanRequestRepository;

    @InjectMocks
    private LoanRequestService loanRequestService;

    private static final String UNIQUE_USERNAME = "uniqueuser";
    private static final String VISITOR_USERNAME = "testvisitor";
    private static final String PASSWORD = "ILoveEcse321";
    private static final String OVERLAPPING_REQUEST_USERNAME = "spammer";
    private static final String OVERLAPPING_REQUEST_ARTWORK = "overlapping vase";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(loanRequestRepository.findByRequesterAndArtwork(any(Visitor.class), any(Artwork.class))).thenAnswer(
            (InvocationOnMock invocation) -> {
                Object oUser = invocation.getArgument(0);
                Visitor user = (Visitor) oUser;
                String username = user.getUsername();

                Object oArtwork = invocation.getArgument(1);
                Artwork artwork = (Artwork) oArtwork;
               
                if (username.equals(OVERLAPPING_REQUEST_USERNAME)) {

                    List<LoanRequest> requests = new ArrayList<>();
                    
                    LoanRequest request1 = new LoanRequest();
                    request1.setRequester(user);
                    request1.setArtwork(artwork);
                    request1.setRequestedStartDate(new Date(System.currentTimeMillis()));
                    request1.setRequestedEndDate(new Date(System.currentTimeMillis() + 10000000));
                    request1.setStatus(Status.Pending);
                    
                    requests.add(request1);
                    return requests;
                } else {
                    return new ArrayList<LoanRequest>();
                }
            }
        );
        
        lenient().when(loanRequestRepository.findAllByRequesterAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(any(Visitor.class), any(Artwork.class), any(Date.class), any(Date.class))).thenAnswer(
            (InvocationOnMock invocation) -> {
                Object oUser = invocation.getArgument(0);
                Visitor user = (Visitor) oUser;
                String username = user.getUsername();
                Object oArtwork = invocation.getArgument(1);
                Artwork artwork = (Artwork) oArtwork;
                if (username.equals(OVERLAPPING_REQUEST_USERNAME)) { // simply return non empty list
                    List<LoanRequest> requests = new ArrayList<>();
                    LoanRequest request1 = new LoanRequest();
                    request1.setRequester(user);
                    request1.setArtwork(artwork);
                    request1.setRequestedStartDate(new Date(System.currentTimeMillis()));
                    request1.setRequestedEndDate(new Date(System.currentTimeMillis() + 10));
                    request1.setStatus(Status.Pending);
                    requests.add(request1);
                    return requests;
                } else {
                    return new ArrayList<LoanRequest>();
                }
            }
        );
        

        lenient().when(loanRequestRepository.findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual
        (any(Status.class), any(Artwork.class), any(Date.class), any(Date.class))).thenAnswer(
            (InvocationOnMock invocation) -> {
                Object oArtwork = invocation.getArgument(1);
                Artwork artwork = (Artwork) oArtwork;
                if (artwork.getName().equals(OVERLAPPING_REQUEST_ARTWORK)) { // simply return non empty list
                    List<LoanRequest> requests = new ArrayList<>();
                    LoanRequest request1 = new LoanRequest();
                    request1.setArtwork(artwork);
                    request1.setRequestedStartDate(new Date(System.currentTimeMillis()));
                    request1.setRequestedEndDate(new Date(System.currentTimeMillis() + 10));
                    request1.setStatus(Status.Pending);
                    requests.add(request1);
                    return requests;
                } else {
                    return new ArrayList<>();
                }
            }
        );

         Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
        lenient().when(loanRequestRepository.save(any(LoanRequest.class))).thenAnswer(returnParameterAsAnswer);
    }

    /* -----------------------------Create Loan Request Tests ------------------------------------------------------*/
    @Test
    public void testCreateLoanRequest() {
        LoanRequest loanRequest = null;

        Visitor aRequester = new Visitor();
        aRequester.setPassword(PASSWORD);
        aRequester.setUsername(VISITOR_USERNAME);

        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(true);

        Date requestedStartDate = new Date(System.currentTimeMillis() + 400);
        Date requestedEndDate =  new Date(System.currentTimeMillis() + 500);

        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(loanRequest);
        assertEquals(requestedStartDate.toString(), loanRequest.getRequestedStartDate().toString());
        assertEquals(requestedEndDate.toString(), loanRequest.getRequestedEndDate().toString());
        assertEquals(vase.getName(), loanRequest.getArtwork().getName());
        assertEquals(Status.Pending, loanRequest.getStatus());
    }

    @Test
    public void testCreateLoanRequestNulluser() {
        String error = "";
        LoanRequest loanRequest = null;
        
        Visitor aRequester = null;
        
        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(true);

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(2023, Calendar.NOVEMBER, 20, 9, 00, 0);        
        c2.set(2023, Calendar.NOVEMBER, 30, 9, 00, 0);

        Date requestedStartDate =  new Date(c1.getTimeInMillis());
        Date requestedEndDate = new Date(c2.getTimeInMillis());
        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Must select a visitor", error);
        assertEquals(null, loanRequest);
    }

    @Test
    public void testCreateLoanRequestOverlappingRequestSameUser() {
        String error = "";
        LoanRequest loanRequest = null;

        Visitor aRequester = new Visitor();
        aRequester.setPassword(PASSWORD);
        aRequester.setUsername(OVERLAPPING_REQUEST_USERNAME);

        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(true);


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(2023, Calendar.NOVEMBER, 20, 9, 00, 0);        
        c2.set(2023, Calendar.NOVEMBER, 30, 9, 00, 0);

        Date requestedStartDate =  new Date(c1.getTimeInMillis());
        Date requestedEndDate = new Date(c2.getTimeInMillis());
       
        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);

        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("You have overlapping loan requests for same artwork", error);
        assertEquals(null, loanRequest);
    }

    @Test
    public void testCreateLoanRequestNotLoanable() {
        String error = "";
        LoanRequest loanRequest = null;

        Visitor aRequester = new Visitor();
        aRequester.setPassword(PASSWORD);
        aRequester.setUsername("tr");

        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(false);


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(2023, Calendar.NOVEMBER, 20, 9, 00, 0);        
        c2.set(2023, Calendar.NOVEMBER, 30, 9, 00, 0);

        Date requestedStartDate =  new Date(c1.getTimeInMillis());
        Date requestedEndDate = new Date(c2.getTimeInMillis());
       
        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Artwork is not loanable", error);
        assertEquals(null, loanRequest);
    }

    @Test
    public void testCreateLoanRequestDatesInPast() {
        String error = "";
        LoanRequest loanRequest = null;

        Visitor aRequester = new Visitor();
        aRequester.setPassword(PASSWORD);
        aRequester.setUsername("try");

        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(true);


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(2021, Calendar.NOVEMBER, 20, 9, 00, 0);        
        c2.set(2021, Calendar.NOVEMBER, 30, 9, 00, 0);

        Date requestedStartDate =  new Date(c1.getTimeInMillis());
        Date requestedEndDate = new Date(c2.getTimeInMillis());
       
        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Loan request dates cannot be past!", error);
        assertEquals(null, loanRequest);
    }
    
    @Test
    public void testCreateLoanRequestEndBeforeStart() {
        LoanRequest loanRequest = null;
        String error = "";

        Visitor aRequester = new Visitor();
        aRequester.setPassword(PASSWORD);
        aRequester.setUsername(UNIQUE_USERNAME);

        Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
        vase.setLoanable(true);


        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(2023, Calendar.NOVEMBER, 30, 9, 00, 0);        
        c2.set(2023, Calendar.NOVEMBER, 29, 9, 00, 0);

        Date requestedStartDate =  new Date(c1.getTimeInMillis());
        Date requestedEndDate = new Date(c2.getTimeInMillis());
       
        try {
           loanRequest = loanRequestService.createLoanRequest(requestedStartDate, requestedEndDate, aRequester, vase);
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("End is before start date", error);
        assertEquals(null, loanRequest);
    }
    /* -----------------------------Approve Loan Request Tests ------------------------------------------------------*/
    @Test
    public void testApproveLoanRequest() {
        LoanRequest loanRequest = new LoanRequest();

        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        
        c2.set(2023, Calendar.JANUARY, 16, 9, 00, 0);
        Date requestedStartDate =  new Date(c2.getTimeInMillis());

        c3.set(2023, Calendar.FEBRUARY, 16, 9, 00, 0);
        Date requestedEndDate =  new Date(c3.getTimeInMillis());

        loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(Status.Pending);

        Artwork artwork = new Artwork();
        artwork.setName("vase");
        artwork.setLoanable(true);

        loanRequest.setArtwork(artwork);
        
        try {
           loanRequest = loanRequestService.approveLoanRequest(loanRequest);  
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertEquals(Status.Approved, loanRequest.getStatus());
    }

    @Test
    public void testApproveLoanRequestArtworkReservedDuringGivenDates() {
        LoanRequest loanRequest = new LoanRequest();
        String error = "";

        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        
        c2.set(2023, Calendar.JANUARY, 16, 9, 00, 0);
        Date requestedStartDate =  new Date(c2.getTimeInMillis());

        c3.set(2023, Calendar.FEBRUARY, 16, 9, 00, 0);
        Date requestedEndDate =  new Date(c3.getTimeInMillis());

        loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(Status.Pending);

        Artwork artwork = new Artwork();
        artwork.setName(OVERLAPPING_REQUEST_ARTWORK);
        artwork.setLoanable(true);

        loanRequest.setArtwork(artwork);
        
        try {
           loanRequest = loanRequestService.approveLoanRequest(loanRequest); 
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Artwork is reserved for that period of time", error);
        assertEquals(Status.Pending, loanRequest.getStatus());
    }

    @Test
    public void testApproveLoanRequestInvalidApprovalDate() {
        String error = "";
        LoanRequest loanRequest = new LoanRequest();

        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        
        c2.set(2022, Calendar.JANUARY, 16, 9, 00, 0);
        Date requestedStartDate =  new Date(c2.getTimeInMillis());
        
        c3.set(2022, Calendar.FEBRUARY, 16, 9, 00, 0);
        Date requestedEndDate =  new Date(c3.getTimeInMillis());

        loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(Status.Pending);

        Artwork artwork = new Artwork();
        artwork.setName("vase");
        artwork.setLoanable(true);

        loanRequest.setArtwork(artwork);
        
        try {
        loanRequest = loanRequestService.approveLoanRequest(loanRequest);  
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Approval date had to occur before requested date. Must be denied.", error);
        assertEquals(Status.Pending, loanRequest.getStatus());
    }

    @Test
    public void testApproveLoanRequestAlreadyNotPending() {
        String error = "";
        LoanRequest loanRequest = new LoanRequest();

        Calendar c2 = Calendar.getInstance();
        Calendar c3 = Calendar.getInstance();
        
        c2.set(2023, Calendar.JANUARY, 16, 9, 00, 0);
        Date requestedStartDate =  new Date(c2.getTimeInMillis());

        c3.set(2023, Calendar.FEBRUARY, 16, 9, 00, 0);
        Date requestedEndDate =  new Date(c3.getTimeInMillis());

        loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(Status.Rejected);

        Artwork artwork = new Artwork();
        artwork.setName("vase");
        artwork.setLoanable(true);

        loanRequest.setArtwork(artwork);
        
        try {
            loanRequest = loanRequestService.approveLoanRequest(loanRequest);  
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();            
        }
        assertEquals("Fate of this loan request has already been decided", error);
        assertEquals(Status.Rejected, loanRequest.getStatus());
    }
    /* -----------------------------Deny Loan Request Tests ------------------------------------------------------*/
    @Test
    public void testDenyLoanRequest() {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setStatus(Status.Pending);
        
        try {
            loanRequest = loanRequestService.denyLoanRequest(loanRequest);  
        } catch (IllegalArgumentException e) {
            fail(e.getMessage().trim());
        }
        assertEquals(Status.Rejected, loanRequest.getStatus());
    }

    @Test
    public void testDenyLoanRequestAlreadyDecidedOutcome() {
        String error = "";
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setStatus(Status.Rejected);
        
        try {
            loanRequest = loanRequestService.denyLoanRequest(loanRequest);  
        } catch (IllegalArgumentException e) {
            error = e.getMessage().trim();
        }
        assertEquals("Fate of this loan request has already been decided", error);
        assertEquals(Status.Rejected, loanRequest.getStatus());
    }
}
