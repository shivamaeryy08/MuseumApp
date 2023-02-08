/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;

import java.sql.Date;
import javax.persistence.Entity;

// line 89 "model.ump"
// line 170 "model.ump"
@Entity
public class LoanRequest
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Status { Pending, Approved, Rejected }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //LoanRequest Attributes
  @Id
  @GeneratedValue
  private Long requestID;
  private Status status;
  private Date requestedStartDate;
  private Date requestedEndDate;
  //LoanRequest Associations

  @ManyToOne(optional = false)
  private Visitor requester;
  @ManyToOne(optional = false)
  private Artwork artwork;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public LoanRequest(){}

  public LoanRequest(Date aRequestedStartDate, Date aRequestedEndDate, Visitor aRequester, Artwork aArtwork)
  {
    resetStatus();
    requestedStartDate = aRequestedStartDate;
    requestedEndDate = aRequestedEndDate;
    if (!setRequester(aRequester))
    {
      throw new RuntimeException("Unable to create LoanRequest due to aRequester. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setArtwork(aArtwork))
    {
      throw new RuntimeException("Unable to create LoanRequest due to aArtwork. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  /* Code from template attribute_SetDefaulted */
  public boolean setStatus(Status aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean resetStatus()
  {
    boolean wasReset = false;
    status = getDefaultStatus();
    wasReset = true;
    return wasReset;
  }

  public boolean setRequestedStartDate(Date aRequestedStartDate)
  {
    boolean wasSet = false;
    requestedStartDate = aRequestedStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestedEndDate(Date aRequestedEndDate)
  {
    boolean wasSet = false;
    requestedEndDate = aRequestedEndDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestID(Long aRequestID)
  {
    boolean wasSet = false;
    requestID = aRequestID;
    wasSet = true;
    return wasSet;
  }

  public Status getStatus()
  {
    return status;
  }
  /* Code from template attribute_GetDefaulted */
  public Status getDefaultStatus()
  {
    return Status.Pending;
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
  /* Code from template association_GetOne */
  public Visitor getRequester()
  {
    return requester;
  }
  /* Code from template association_GetOne */
  public Artwork getArtwork()
  {
    return artwork;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setRequester(Visitor aNewRequester)
  {
    boolean wasSet = false;
    if (aNewRequester != null)
    {
      requester = aNewRequester;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setArtwork(Artwork aNewArtwork)
  {
    boolean wasSet = false;
    if (aNewArtwork != null)
    {
      artwork = aNewArtwork;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    requester = null;
    artwork = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "requestID" + ":" + getRequestID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestedStartDate" + "=" + (getRequestedStartDate() != null ? !getRequestedStartDate().equals(this)  ? getRequestedStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "requestedEndDate" + "=" + (getRequestedEndDate() != null ? !getRequestedEndDate().equals(this)  ? getRequestedEndDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "requester = "+(getRequester()!=null?Integer.toHexString(System.identityHashCode(getRequester())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "artwork = "+(getArtwork()!=null?Integer.toHexString(System.identityHashCode(getArtwork())):"null") + System.getProperties().getProperty("line.separator");
  }
}



