package ca.mcgill.ecse321.MuseumManagementSystem.dto;

public class MuseumInformationDto {
  private String museumName;
  private double visitorFee;
  private String mondayHours;
  private String tuesdayHours;
  private String wednesdayHours;
  private String thursdayHours;
  private String fridayHours;
  private String saturdayHours;
  private String sundayHours;

  public MuseumInformationDto () {}

  public MuseumInformationDto (double visitorFee, String museumName, String mondayHours, String tuesdayHours, String wednesdayHours, String thursdayHours, String fridayHours, String saturdayHours, String sundayHours) {
    this.visitorFee = visitorFee;
    this.museumName = museumName;
    this.mondayHours = mondayHours;
    this.tuesdayHours = tuesdayHours;
    this.wednesdayHours = wednesdayHours;
    this.thursdayHours = thursdayHours;
    this.fridayHours = fridayHours;
    this.saturdayHours = saturdayHours;
    this.sundayHours = sundayHours;
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
}
