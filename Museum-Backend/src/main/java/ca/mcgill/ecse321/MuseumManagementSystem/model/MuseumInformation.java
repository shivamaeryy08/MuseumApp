/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;

// line 21 "model.ump"
// line 184 "model.ump"
@Entity
public class MuseumInformation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //MuseumInformation Attributes
  @Id
  private String museumName;
  private double visitorFee;
  private String mondayHours;
  private String tuesdayHours;
  private String wednesdayHours;
  private String thursdayHours;
  private String fridayHours;
  private String saturdayHours;
  private String sundayHours;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public MuseumInformation(){}

  public MuseumInformation(double aVisitorFee, String aMuseumName, String aMondayHours, String aTuesdayHours, String aWednesdayHours, String aThursdayHours, String aFridayHours, String aSaturdayHours, String aSundayHours)
  {
    visitorFee = aVisitorFee;
    museumName = aMuseumName;
    mondayHours = aMondayHours;
    tuesdayHours = aTuesdayHours;
    wednesdayHours = aWednesdayHours;
    thursdayHours = aThursdayHours;
    fridayHours = aFridayHours;
    saturdayHours = aSaturdayHours;
    sundayHours = aSundayHours;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setVisitorFee(double aVisitorFee)
  {
    boolean wasSet = false;
    visitorFee = aVisitorFee;
    wasSet = true;
    return wasSet;
  }

  public boolean setMuseumName(String aMuseumName)
  {
    boolean wasSet = false;
    museumName = aMuseumName;
    wasSet = true;
    return wasSet;
  }

  public boolean setMondayHours(String aMondayHours)
  {
    boolean wasSet = false;
    mondayHours = aMondayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setTuesdayHours(String aTuesdayHours)
  {
    boolean wasSet = false;
    tuesdayHours = aTuesdayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setWednesdayHours(String aWednesdayHours)
  {
    boolean wasSet = false;
    wednesdayHours = aWednesdayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setThursdayHours(String aThursdayHours)
  {
    boolean wasSet = false;
    thursdayHours = aThursdayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setFridayHours(String aFridayHours)
  {
    boolean wasSet = false;
    fridayHours = aFridayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setSaturdayHours(String aSaturdayHours)
  {
    boolean wasSet = false;
    saturdayHours = aSaturdayHours;
    wasSet = true;
    return wasSet;
  }

  public boolean setSundayHours(String aSundayHours)
  {
    boolean wasSet = false;
    sundayHours = aSundayHours;
    wasSet = true;
    return wasSet;
  }

  public double getVisitorFee()
  {
    return visitorFee;
  }

  public String getMuseumName()
  {
    return museumName;
  }

  public String getMondayHours()
  {
    return mondayHours;
  }

  public String getTuesdayHours()
  {
    return tuesdayHours;
  }

  public String getWednesdayHours()
  {
    return wednesdayHours;
  }

  public String getThursdayHours()
  {
    return thursdayHours;
  }

  public String getFridayHours()
  {
    return fridayHours;
  }

  public String getSaturdayHours()
  {
    return saturdayHours;
  }

  public String getSundayHours()
  {
    return sundayHours;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "visitorFee" + ":" + getVisitorFee()+ "," +
            "museumName" + ":" + getMuseumName()+ "," +
            "mondayHours" + ":" + getMondayHours()+ "," +
            "tuesdayHours" + ":" + getTuesdayHours()+ "," +
            "wednesdayHours" + ":" + getWednesdayHours()+ "," +
            "thursdayHours" + ":" + getThursdayHours()+ "," +
            "fridayHours" + ":" + getFridayHours()+ "," +
            "saturdayHours" + ":" + getSaturdayHours()+ "," +
            "sundayHours" + ":" + getSundayHours()+ "]";
  }
}



