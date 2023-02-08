/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/
package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.*;

// line 2 "model.ump"
// line 121 "model.ump"
@Entity
public class Employee extends Staff
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Employee() {}

  public Employee(String aUsername, String aPassword)
  {
    super(aUsername, aPassword);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}



