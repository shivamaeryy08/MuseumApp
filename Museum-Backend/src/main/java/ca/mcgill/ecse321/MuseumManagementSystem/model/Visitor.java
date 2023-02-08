/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse321.MuseumManagementSystem.model;

import javax.persistence.Entity;

// line 75 "model.ump"
// line 161 "model.ump"
@Entity
public class Visitor extends Person
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Visitor() {}

  public Visitor(String aUsername, String aPassword)
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



