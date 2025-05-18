package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.dto.SearchCriteriaDTO;
import com.nourmina.jobportal.exception.BadRequestException;
import org.springframework.stereotype.Component;

/**
 * Validator for SearchCriteriaDTO
 */
@Component
public class SearchCriteriaValidator {

    /**
     * Validate search criteria
     *
     * @param criteria The search criteria to validate
     * @throws BadRequestException if the criteria is invalid
     */
    public void validate(SearchCriteriaDTO criteria) throws BadRequestException {
        // Check if any criteria is provided
        boolean hasCriteria = false;

        if (criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
            hasCriteria = true;
        }

        if (criteria.getLocation() != null && !criteria.getLocation().trim().isEmpty()) {
            hasCriteria = true;
        }

        if (criteria.getCompany() != null && !criteria.getCompany().trim().isEmpty()) {
            hasCriteria = true;
        }

        if (criteria.getSkills() != null && !criteria.getSkills().isEmpty()) {
            hasCriteria = true;
        }

        if (criteria.getMinSalary() != null || criteria.getMaxSalary() != null) {
            hasCriteria = true;
        }

        if (criteria.getStatus() != null && !criteria.getStatus().trim().isEmpty()) {
            hasCriteria = true;
        }

        if (!hasCriteria) {
            throw new BadRequestException("At least one search criteria must be provided");
        }

        // Validate salary range
        if (criteria.getMinSalary() != null && criteria.getMaxSalary() != null) {
            if (criteria.getMinSalary() > criteria.getMaxSalary()) {
                throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
            }
        }

        // Validate status
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            String status = criteria.getStatus().toUpperCase();
            if (!status.equals("ACTIVE") && !status.equals("CLOSED")) {
                throw new BadRequestException("Invalid status. Allowed values: ACTIVE, CLOSED");
            }
        }
    }
}