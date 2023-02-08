package ca.mcgill.ecse321.MuseumManagementSystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.MuseumManagementSystem.dao.ArtworkRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.LoanRequestRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.VisitorRepository;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.ArtworkDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.LoanRequestDto;
import ca.mcgill.ecse321.MuseumManagementSystem.dto.VisitorDto;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoanRequestIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private static final Date REQUESTED_START_DATE = Date.valueOf("2023-12-05");
    private static final Date REQUESTED_END_DATE = Date.valueOf("2023-12-15");
    
    @AfterEach
    @BeforeEach
    public void clearDatabase() {
        loanRequestRepository.deleteAll();
		artworkRepository.deleteAll();
		visitorRepository.deleteAll();
    }

    /* create loan requests tests */
    @Test
    public void testCreateLoanRequest() {
        testCreateLoanRequestSuccessfulTemplate();
    }

    @Test
    public void testCreateLoanRequestInvalidVisitor() {
        //Note that below I am using the LoanRequestDto constructor that does NOT take in the shift ID
        VisitorDto testVisitor =  createVisitorDto();
        ArtworkDto testArtwork = createArtworkDtoNotLoanable();

        LoanRequestDto loanRequestDto = new LoanRequestDto(REQUESTED_START_DATE, REQUESTED_END_DATE, testVisitor, testArtwork);
        ResponseEntity<String> response = client.postForEntity("/loanRequest", loanRequestDto, String.class);
        
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertEquals("Artwork is not loanable", response.getBody().trim());
    }

    /* approve loan request tests */
    @Test
    public void testCreateAndApproveLoanRequest() {
        Long loanRequestId = testCreateLoanRequestSuccessfulTemplate();
        testApproveLoanRequest(loanRequestId);
    }

    @Test
    public void testCreateAndApproveLoanRequestAlreadyApproved() {
        Long loanRequestId = testCreateLoanRequestSuccessfulTemplate();
        testApproveLoanRequest(loanRequestId);
        ResponseEntity<String> response = client.postForEntity("/loanRequest/" + loanRequestId + "/approveLoanRequest", null,String.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertEquals("Fate of this loan request has already been decided\nArtwork is reserved for that period of time", response.getBody().trim());
    }

    /* deny loan requests tests */
    @Test
    public void testCreateAndDenyLoanRequest() {
        Long loanRequestId = testCreateLoanRequestSuccessfulTemplate();
        ResponseEntity<LoanRequestDto> response = client.postForEntity("/loanRequest/" + loanRequestId + "/denyLoanRequest", null,LoanRequestDto.class);
        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(loanRequestId, response.getBody().getRequestID(), "Response has correct loanRequest Id");
        assertEquals(Status.Rejected, response.getBody().getStatus(), "Response has correct status");  
    }

    @Test
    public void testCreateAndDenyLoanRequestNonExistingLoanRequest() {
        ResponseEntity<String> response = client.postForEntity("/loanRequest/" + 0 + "/denyLoanRequest", null, String.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
        assertEquals("Must specify an actual request", response.getBody().trim());
    }

    /* get loan request test */
    @Test
    public void testGetLoanRequestById() {
        long id = testCreateLoanRequestSuccessfulTemplate();
        ResponseEntity<LoanRequestDto> response = client.getForEntity("/loanRequest/"+ id, LoanRequestDto.class);
        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(REQUESTED_START_DATE.toString(), response.getBody().getRequestedStartDate().toString(), "Response has correct start date");
        assertEquals(REQUESTED_END_DATE.toString(), response.getBody().getRequestedEndDate().toString(), "Response has correct end date");
        assertEquals(id, response.getBody().getRequestID(), "Response has correct ID");
    }

    @Test
    public void testCreateAndGetAllLoanRequests() {
        long id = testCreateLoanRequestSuccessfulTemplate();
        ResponseEntity<LoanRequestDto[]> response = client.getForEntity("/loanRequests/", LoanRequestDto[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertEquals(1, response.getBody().length);
        assertEquals(id, response.getBody()[0].getRequestID());
    }

    @Test
    public void testCreateAndGetAVisitorsLoanRequests() {
        long id = testCreateLoanRequestSuccessfulTemplate();
        ResponseEntity<LoanRequestDto[]> response = client.getForEntity("/loanRequests/" + "mms_visitor", LoanRequestDto[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertEquals(1, response.getBody().length);
        assertEquals(id, response.getBody()[0].getRequestID());
    }

    @Test
    public void testCreateAndGetAVisitorsLoanRequestsInvalid() {
        long id = testCreateLoanRequestSuccessfulTemplate();
        ResponseEntity<String> response = client.getForEntity("/loanRequests/" + (id - 1), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    }

    /* Helper Methods */

    private VisitorDto createVisitorDto() {
        Visitor visitor = new Visitor();
        visitor.setUsername("mms_visitor");
        visitor.setPassword("password");
        visitor = visitorRepository.save(visitor);
        VisitorDto visitorDto = new VisitorDto(visitor.getUsername(), visitor.getPassword());
        return visitorDto;
    }
    
    private ArtworkDto createArtworkDto() {
        Artwork artwork = new Artwork();
        artwork.setLoanPrice(5.42);
        artwork.setName("vase");
        artwork.setArtist("picass");
        artwork.setDescription("based vase");
        artwork.setYear("2005");
        artwork.setLoanable(true);
        artwork = artworkRepository.save(artwork);
        ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(), artwork.getArtist(), artwork.getDescription(), artwork.getYear(), artwork.getLoanable(),artwork.getImgUrl(),null);
        return artworkDto;
    }

    private ArtworkDto createArtworkDtoNotLoanable() {
        Artwork artwork = new Artwork();
        artwork.setLoanPrice(5.42);
        artwork.setName("vase");
        artwork.setArtist("picass");
        artwork.setDescription("based vase");
        artwork.setYear("2005");
        artwork.setLoanable(false);
        artwork = artworkRepository.save(artwork);
        ArtworkDto artworkDto = new ArtworkDto(artwork.getArtworkID(), artwork.getLoanPrice(), artwork.getName(), artwork.getArtist(), artwork.getDescription(), artwork.getYear(), artwork.getLoanable(), artwork.getImgUrl(),null);
        return artworkDto;
    }

    private long testCreateLoanRequestSuccessfulTemplate() {
        VisitorDto testVisitor =  createVisitorDto();
        String testVisitorName = testVisitor.getUsername();
        
        ArtworkDto testArtwork = createArtworkDto();
        long artworkId = testArtwork.getArtworkID();

        LoanRequestDto loanRequestDto = new LoanRequestDto(REQUESTED_START_DATE, REQUESTED_END_DATE, testVisitor, testArtwork);
        ResponseEntity<LoanRequestDto> response = client.postForEntity("/loanRequest", loanRequestDto, LoanRequestDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");

        assertEquals(REQUESTED_START_DATE.toString(), response.getBody().getRequestedStartDate().toString(), "Response has correct requested Start date");
        assertEquals(REQUESTED_END_DATE.toString(), response.getBody().getRequestedEndDate().toString(), "Response has correct requested Start date");
        assertEquals(artworkId, response.getBody().getArtwork().getArtworkID(), "Response has correct requested Start date");
        assertEquals(testVisitorName, response.getBody().getRequester().getUsername(), "Response has correct requested Start date");
    
        assertTrue(response.getBody().getRequestID() > 0, "Response has valid ID");
        return response.getBody().getRequestID();
    }

    private void testApproveLoanRequest(Long requestId) {
        ResponseEntity<LoanRequestDto> response = client.postForEntity("/loanRequest/" + requestId + "/approveLoanRequest", null, LoanRequestDto.class);
        assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(requestId, response.getBody().getRequestID(), "Response has correct loanRequest Id");
        assertEquals(Status.Approved, response.getBody().getStatus(), "Response has correct status");
       
    }
}