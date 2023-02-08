package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;



// line 44 "model.ump"
// line 143 "model.ump"
@Entity
public class Artwork
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Artwork Attributes
  private double loanPrice;
  private boolean loanable;
  private String name;
  private String artist;
  private String description;
  private String year;
  private String imgUrl;

  @Id
  @GeneratedValue
  private Long artworkID;

  //Artwork Associations
  @ManyToOne(optional = true)
  private Room room;

  //------------------------
  // CONSTRUCTOR
  //------------------------
 
  public Artwork(){}
  
  public Artwork(double aLoanPrice, String aName, String aArtist, String aDescription, String aYear,String imgUrl)
  {
    loanPrice = aLoanPrice;
    resetLoanable();
    name = aName;
    artist = aArtist;
    description = aDescription;
    year = aYear;
    this.imgUrl = imgUrl;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }
  
  public boolean setLoanPrice(double aLoanPrice)
  {
    boolean wasSet = false;
    loanPrice = aLoanPrice;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setLoanable(boolean aLoanable)
  {
    boolean wasSet = false;
    loanable = aLoanable;
    wasSet = true;
    return wasSet;
  }

  public boolean resetLoanable()
  {
    boolean wasReset = false;
    loanable = getDefaultLoanable();
    wasReset = true;
    return wasReset;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setArtist(String aArtist)
  {
    boolean wasSet = false;
    artist = aArtist;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setYear(String aYear)
  {
    boolean wasSet = false;
    year = aYear;
    wasSet = true;
    return wasSet;
  }

  public boolean setArtworkID(Long aArtworkID)
  {
    boolean wasSet = false;
    artworkID = aArtworkID;
    wasSet = true;
    return wasSet;
  }

  public double getLoanPrice()
  {
    return loanPrice;
  }

  public boolean getLoanable()
  {
    return loanable;
  }
  /* Code from template attribute_GetDefaulted */
  public boolean getDefaultLoanable()
  {
    return false;
  }

  public String getName()
  {
    return name;
  }

  public String getArtist()
  {
    return artist;
  }

  public String getDescription()
  {
    return description;
  }

  public String getYear()
  {
    return year;
  }

  public Long getArtworkID()
  {
    return artworkID;
  }
  /* Code from template association_GetOne */
  public Room getRoom()
  {
    return room;
  }

  public boolean hasRoom()
  {
    boolean has = room != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setRoom(Room aNewRoom)
  {
    boolean wasSet = false;
    room = aNewRoom;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    room = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "loanPrice" + ":" + getLoanPrice()+ "," +
            "loanable" + ":" + getLoanable()+ "," +
            "name" + ":" + getName()+ "," +
            "artist" + ":" + getArtist()+ "," +
            "description" + ":" + getDescription()+ "," +
            "year" + ":" + getYear()+ "," +
            "artworkID" + ":" + getArtworkID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "room = "+(getRoom()!=null?Integer.toHexString(System.identityHashCode(getRoom())):"null");
  }
}



