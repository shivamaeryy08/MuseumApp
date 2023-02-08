package ca.mcgill.ecse321.MuseumManagementSystem.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumManagementSystem.model.Visitor;

public interface VisitorRepository extends CrudRepository<Visitor, String> {
    Visitor findVisitorByUsername(String username);
}