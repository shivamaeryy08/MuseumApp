package ca.mcgill.ecse321.MuseumManagementSystem.dto;

import java.sql.Date;
import ca.mcgill.ecse321.MuseumManagementSystem.model.LoanRequest.Status;;

public class LoanRequestDto {
  
    private Long requestID;
    private Status status;
    private Date requestedStartDate;
    private Date requestedEndDate;

    private VisitorDto requester;
    private ArtworkDto artwork;

    public LoanRequestDto(Date aRequestedStartDate, Date aRequestedEndDate, VisitorDto aRequester, ArtworkDto aArtwork)
  {
    requestedStartDate = aRequestedStartDate;
    requestedEndDate = aRequestedEndDate;
    requester = aRequester;
    artwork = aArtwork;
  }

  public LoanRequestDto(Long aRequestId, Date aRequestedStartDate, Date aRequestedEndDate, VisitorDto aRequester, ArtworkDto aArtwork)
  {
    requestedStartDate = aRequestedStartDate;
    requestedEndDate = aRequestedEndDate;
    requester = aRequester;
    artwork = aArtwork;
    requestID = aRequestId;
  }

    public LoanRequestDto() {}


    public void setStatus(Status aStatus)
    {
      status = aStatus;
    }
    
    public void setRequestedStartDate(Date aRequestedStartDate)
    {
      requestedStartDate = aRequestedStartDate;
    }
  
    public void setRequestedEndDate(Date aRequestedEndDate)
    {
      requestedEndDate = aRequestedEndDate;
    }
  
    public void setRequestID(Long aRequestID)
    {
      requestID = aRequestID;
    }
  
    public Status getStatus()
    {
      return status;
    }
  
    public Date getRequestedStartDate()
    {
      return requestedStartDate;
    }
  
    public Date getRequestedEndDate()
    {
      return requestedEndDate;
    }
  
    public Long getRequestID()
    {
      return requestID;
    }
    
    public VisitorDto getRequester()
    {
      return requester;
    }
    
    public ArtworkDto getArtwork()
    {
      return artwork;
    }
  
    public void setRequester(VisitorDto aNewRequester) {
        requester = aNewRequester;
    }
    
    public void setArtwork(ArtworkDto aNewArtwork)
    {
        artwork = aNewArtwork;
    }
}
  
