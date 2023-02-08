package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    Owner findOwnerByUsername(String username);
}