
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;


// line 35 "model.ump"
// line 138 "model.ump"
@Entity
public class Room
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum RoomType { Display, Storage }
  public enum RoomSize { Small, Large }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Attributes
  @Id
  @GeneratedValue
  private Long roomId;
  private RoomType roomType;
  private RoomSize roomSize;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room() {}

  public Room(RoomType aRoomType)
  {
    roomType = aRoomType;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRoomId(Long aRoomId)
  {
    boolean wasSet = false;
    roomId = aRoomId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomType(RoomType aRoomType)
  {
    boolean wasSet = false;
    roomType = aRoomType;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomSize(RoomSize aRoomSize)
  {
    boolean wasSet = false;
    roomSize = aRoomSize;
    wasSet = true;
    return wasSet;
  }

  public Long getRoomId()
  {
    return roomId;
  }

  public RoomType getRoomType()
  {
    return roomType;
  }

  public RoomSize getRoomSize()
  {
    return roomSize;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "roomId" + ":" + getRoomId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "roomType" + "=" + (getRoomType() != null ? !getRoomType().equals(this)  ? getRoomType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "roomSize" + "=" + (getRoomSize() != null ? !getRoomSize().equals(this)  ? getRoomSize().toString().replaceAll("  ","    ") : "this" : "null");
  }
}



