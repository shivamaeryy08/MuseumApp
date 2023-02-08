package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;


public interface MuseumInformationRepository extends CrudRepository<MuseumInformation, String> {
  MuseumInformation findMuseumInformationByMuseumName(String museum_name);
}