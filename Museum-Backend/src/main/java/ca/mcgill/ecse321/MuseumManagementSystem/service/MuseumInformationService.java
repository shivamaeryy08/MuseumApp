package ca.mcgill.ecse321.MuseumManagementSystem.service;

import ca.mcgill.ecse321.MuseumManagementSystem.model.MuseumInformation;
import ca.mcgill.ecse321.MuseumManagementSystem.dao.MuseumInformationRepository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MuseumInformationService {
    @Autowired
    MuseumInformationRepository museumInformationRepository;

   /*-------------------------------Museum Methods------------------------------------------------------------------------------------------------------------- */  


    /**
     * @author Pratham Bansal
     * @brief Method to update visitor fee
     * @param museumInformation
     * @param visitorFee
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationFee (MuseumInformation museumInformation, double visitorFee){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if(visitorFee < 0){
            throw new IllegalArgumentException ("Enter a positive fee \n");
        }
        
        museumInformation.setVisitorFee(visitorFee);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Monday hours
     * @param museumInformation
     * @param mondayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationMondayHours (MuseumInformation museumInformation, String mondayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (mondayHours == null || (mondayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setMondayHours(mondayHours);
        return museumInformationRepository.save(museumInformation);
    }
    
    /**
     * @author Pratham Bansal
     * @brief Updates Tuesday hours
     * @param museumInformation
     * @param tuesdayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationTuesdayHours (MuseumInformation museumInformation, String tuesdayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (tuesdayHours == null || (tuesdayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }

        museumInformation.setTuesdayHours(tuesdayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Wednesday hours
     * @param museumInformation
     * @param wednesdayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationWednesdayHours (MuseumInformation museumInformation, String wednesdayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (wednesdayHours == null || (wednesdayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setWednesdayHours(wednesdayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Thursday hours
     * @param museumInformation
     * @param thursdayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationThursdayHours (MuseumInformation museumInformation, String thursdayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (thursdayHours == null || (thursdayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setThursdayHours(thursdayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Friday hours
     * @param museumInformation
     * @param fridayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationFridayHours (MuseumInformation museumInformation, String fridayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (fridayHours == null || (fridayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setFridayHours(fridayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Saturday hours
     * @param museumInformation
     * @param saturdayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationSaturdayHours (MuseumInformation museumInformation, String saturdayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (saturdayHours == null || (saturdayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setSaturdayHours(saturdayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Updates Sunday hours
     * @param museumInformation
     * @param sundayHours
     * @return
     */
    @Transactional
    public MuseumInformation updateMuseumInformationSundayHours (MuseumInformation museumInformation, String sundayHours){
        if (museumInformation == null) {
            throw new IllegalArgumentException ("Enter valid museum information \n");
        }
        if (sundayHours == null || (sundayHours.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}") == false))  {
            throw new IllegalArgumentException("Enter hours as HH:MM-HH:MM \n");
        }
        museumInformation.setSundayHours(sundayHours);
        return museumInformationRepository.save(museumInformation);
    }

    /**
     * @author Pratham Bansal
     * @brief Retrieves museum information
     * @return
     */
    @Transactional
    public MuseumInformation getMuseumInformation() {
        MuseumInformation museumInformation = toList(museumInformationRepository.findAll()).get(0);
        return museumInformation;
    }

    /*--------------------------------------------- Helper Methods----------------------------------------------------------------------------------------- */
    private <T> List<T> toList(Iterable<T> iterable){
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}

}

