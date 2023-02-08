package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Artwork;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest;
import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;

@SpringBootTest
public class LoanRequestRepositoryTests {

	@Autowired
	private LoanRequestRepository loanRequestRepository;
	@Autowired
	private ArtworkRepository artworkRepository;
	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private VisitorRepository visitorRepository9;

	@Autowired
	private ArtworkRepository artworkRepository33;

	
	@BeforeEach
	@AfterEach
	public void clearDatabase() {
		loanRequestRepository.deleteAll();
		artworkRepository.deleteAll();
		visitorRepository.deleteAll();
	}
	
	@Test
	void contextLoads() {
	}

	@Test
	public void testPersistAndLoadLoanRequest() {

		String username22 = "user22";
		String password22 = "pass";
		Visitor visitor22 = new Visitor();
		visitor22.setUsername(username22);
		visitor22.setPassword(password22);

		visitorRepository9.save(visitor22);

		//Test object
		double loanPrice = 500.1;
		boolean loanable = false;
		String name = "art";
		String artist = "picasso";
		String description = "paint";
		String year = "2000";
		Artwork artworkLR = new Artwork();

		artworkLR.setArtist(artist);
		artworkLR.setDescription(description);
		artworkLR.setLoanPrice(loanPrice);
		artworkLR.setLoanable(loanable);
		artworkLR.setName(name);
		artworkLR.setYear(year);

		artworkRepository33.save(artworkLR);

	    //Test object
        Date requestedStartDate = new Date(500);
        Date requestedEndDate = new Date(9000);
        Status status = Status.Pending;
		LoanRequest loanRequest = new LoanRequest();

		loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(status);
		loanRequest.setRequester(visitor22);
		loanRequest.setArtwork(artworkLR);

		loanRequestRepository.save(loanRequest);

		long id = loanRequest.getRequestID();

		loanRequest = null;
		loanRequest = loanRequestRepository.findLoanRequestByRequestID(id);

		assertNotNull(loanRequest);
		assertEquals(Status.Pending, loanRequest.getStatus());

	}

	/*
	 * Testing crud method FindLoanRequestByArtwork
	 * 
	 * @author Shivam Aery
	 */

	@Test
	public void testFindLoanRequestByArtwork() {

        String username22 = "user67";
		String password22 = "pass";
		Visitor visitor22 = new Visitor();

		visitor22.setUsername(username22);
		visitor22.setPassword(password22);

		visitorRepository9.save(visitor22);

		LoanRequest loanRequest = new LoanRequest();
		loanRequest.setRequestedEndDate(new Date(500));
		loanRequest.setRequestedStartDate(new Date(900));
		loanRequest.setStatus(Status.Pending);

		LoanRequest loanRequest2 = new LoanRequest();
		loanRequest2.setRequestedEndDate(new Date(400));
		loanRequest2.setRequestedStartDate(new Date(300));
		loanRequest2.setStatus(Status.Pending);

		LoanRequest loanRequest3 = new LoanRequest();
		loanRequest3.setRequestedEndDate(new Date(200));
		loanRequest3.setRequestedStartDate(new Date(200));
		loanRequest3.setStatus(Status.Pending);

		Artwork artwork = new Artwork();
		artwork = artworkRepository33.save(artwork);

		loanRequest.setArtwork(artwork);
		loanRequest2.setArtwork(artwork);
		loanRequest3.setArtwork(artwork);
        loanRequest.setRequester(visitor22);
		loanRequest2.setRequester(visitor22);
		loanRequest3.setRequester(visitor22);
		loanRequest = loanRequestRepository.save(loanRequest);
		loanRequest2 = loanRequestRepository.save(loanRequest2);
		loanRequest3 = loanRequestRepository.save(loanRequest3);

		assertNotNull(loanRequestRepository.findLoanRequestByArtwork(artwork));
		assertEquals(loanRequest.getRequestID(),
				loanRequestRepository.findLoanRequestByArtwork(artwork).get(0).getRequestID());
		assertEquals(loanRequest2.getRequestID(),
				loanRequestRepository.findLoanRequestByArtwork(artwork).get(1).getRequestID());
		assertEquals(loanRequest3.getRequestID(),
				loanRequestRepository.findLoanRequestByArtwork(artwork).get(02).getRequestID());

	}

	/*
	 * Test to check if we can find if a artwork is on loan or not
	 * meaning it has a loan request that is approved
	 * and that the current date is >= requestedStart date of loan request
	 * and that the current date is <= requestedEndDate of loan request
	 * 
	 * @author Shivam Aery
	 */
	@Test
	public void testFindOnLoan() {
        String username22 = "user68";
		String password22 = "pass";
		Visitor visitor22 = new Visitor();

		visitor22.setUsername(username22);
		visitor22.setPassword(password22);

		visitorRepository9.save(visitor22);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Calendar c3 = Calendar.getInstance();
		c1.set(2022, Calendar.OCTOBER, 16, 9, 00, 0);
		c2.set(2022, Calendar.DECEMBER, 16, 9, 00, 0);
		c3.set(2021, Calendar.DECEMBER, 16, 9, 00, 0);
		Date requestedStartDate = new Date(c1.getTimeInMillis());
		Date requestedEndDate = new Date(c2.getTimeInMillis());
		LoanRequest loanRequest = new LoanRequest();
		loanRequest.setRequestedEndDate(requestedEndDate);
		loanRequest.setRequestedStartDate(requestedStartDate);
		loanRequest.setStatus(Status.Approved);

		Calendar c4 = Calendar.getInstance();
		Calendar c5 = Calendar.getInstance();
		Calendar c6 = Calendar.getInstance();
		c4.set(2020, Calendar.JANUARY, 16, 9, 00, 0);
		c5.set(2020, Calendar.APRIL, 16, 9, 00, 0);
		c6.set(2019, Calendar.DECEMBER, 16, 9, 00, 0);
		Date requestedStartDate2 = new Date(c4.getTimeInMillis());
		Date requestedEndDate2 = new Date(c5.getTimeInMillis());
		LoanRequest loanRequest2 = new LoanRequest();
		loanRequest2.setRequestedEndDate(requestedEndDate2);
		loanRequest2.setRequestedStartDate(requestedStartDate2);
		loanRequest2.setStatus(Status.Approved);

		Artwork artwork = new Artwork();
		artwork.setName("knife");
		artwork = artworkRepository33.save(artwork);


		loanRequest.setArtwork(artwork);
		loanRequest2.setArtwork(artwork);
        loanRequest.setRequester(visitor22);
		loanRequest2.setRequester(visitor22);
		loanRequest = loanRequestRepository.save(loanRequest);
		loanRequest2 = loanRequestRepository.save(loanRequest2);
		Date currentDate = new Date(System.currentTimeMillis());
		List<LoanRequest> approvedLoanRequest = loanRequestRepository
				.findAllByStatusAndArtworkAndRequestedStartDateLessThanEqualAndRequestedEndDateGreaterThanEqual(
						Status.Approved,
						artwork, currentDate, currentDate);

		assertEquals(1, approvedLoanRequest.size());
		assertEquals(loanRequest.getRequestID(), approvedLoanRequest.get(0).getRequestID());

	}
    


	@Test
	public void testPersistAndLoadLoanRequestByArtworkAndVisitor() {

		// Artwork:
		Artwork vase = new Artwork();
		vase.setLoanPrice(5);
		vase.setName("Vase of Sao Paolo");
		vase.setArtist("Pablo Picasso");
		vase.setDescription("Vase with brazileiro flow");
		vase.setYear("2004");
		vase = artworkRepository.save(vase);

		// Visitor:
	    String username1 = "user1";
        String password1 = "pass";
		Visitor visitor1 = new Visitor();

		visitor1.setUsername(username1);
		visitor1.setPassword(password1);

		visitor1 = visitorRepository.save(visitor1);

	    //Test object loan request
        Date requestedStartDate = new Date(500);
        Date requestedEndDate = new Date(9000);
        Status status = Status.Pending;
		LoanRequest loanRequest = new LoanRequest();

		loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(status);

		loanRequest.setRequester(visitor1);
		loanRequest.setArtwork(vase);
		loanRequestRepository.save(loanRequest);
		long id = loanRequest.getRequestID();

		loanRequest = null;
		loanRequest = loanRequestRepository.findLoanRequestByRequestID(id);

        assertNotNull(loanRequest);
		assertEquals(Status.Pending, loanRequest.getStatus());

    }

	@Test
	public void testLoanable() {

		String username69 = "user69";
		String password22 = "pass";
		Visitor visitor22 = new Visitor();
		visitor22.setUsername(username69);
		visitor22.setPassword(password22);

		visitorRepository9.save(visitor22);

		//Test object
		double loanPrice = 500.1;
		boolean loanable = true;
		String name = "canoli";
		String artist = "picasso";
		String description = "paint";
		String year = "2000";
		Artwork artworkLR = new Artwork();

		artworkLR.setArtist(artist);
		artworkLR.setDescription(description);
		artworkLR.setLoanPrice(loanPrice);
		artworkLR.setLoanable(loanable);
		artworkLR.setName(name);
		artworkLR.setYear(year);

		artworkRepository33.save(artworkLR);

	    //Test object
        Date requestedStartDate = Date.valueOf("2025-12-05");
        Date requestedEndDate = Date.valueOf("2026-12-05");
        Status status = Status.Pending;
		LoanRequest loanRequest = new LoanRequest();

		loanRequest.setRequestedEndDate(requestedEndDate);
        loanRequest.setRequestedStartDate(requestedStartDate);
        loanRequest.setStatus(status);
		loanRequest.setRequester(visitor22);
		loanRequest.setArtwork(artworkLR);

		loanRequestRepository.save(loanRequest);

		long id = loanRequest.getRequestID();

		loanRequest = null;
		loanRequest = loanRequestRepository.findLoanRequestByRequestID(id);

		assertNotNull(loanRequest);
		assertEquals(Status.Pending, loanRequest.getStatus());

	}
}
