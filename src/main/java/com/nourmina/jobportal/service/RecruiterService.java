package com.nourmina.jobportal.service;

import com.nourmina.jobportal.dto.RecruiterDTO;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter getRecruiterById(String id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + id));
    }

    public Recruiter getRecruiterByEmail(String email) {
        return recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with email: " + email));
    }

    public List<Recruiter> getRecruitersByCompany(String company) {
        return recruiterRepository.findByCompany(company);
    }

    public Recruiter createRecruiter(RecruiterDTO recruiterDTO) {
        // Check if recruiter with email already exists
        if (recruiterRepository.findByEmail(recruiterDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + recruiterDTO.getEmail());
        }

        // Create recruiter entity from DTO
        Recruiter recruiter = new Recruiter(
                recruiterDTO.getFirstName(),
                recruiterDTO.getLastName(),
                recruiterDTO.getEmail(),
                passwordEncoder.encode(recruiterDTO.getPassword()),
                recruiterDTO.getCompany(),
                recruiterDTO.getContactInfo()
        );

        return recruiterRepository.save(recruiter);
    }

    public Recruiter updateRecruiter(String id, RecruiterDTO recruiterDTO) {
        Recruiter recruiter = getRecruiterById(id);

        // Update fields
        recruiter.setFirstName(recruiterDTO.getFirstName());
        recruiter.setLastName(recruiterDTO.getLastName());
        recruiter.setCompany(recruiterDTO.getCompany());
        recruiter.setContactInfo(recruiterDTO.getContactInfo());

        // Only update password if it's provided
        if (recruiterDTO.getPassword() != null && !recruiterDTO.getPassword().isEmpty()) {
            recruiter.setPassword(passwordEncoder.encode(recruiterDTO.getPassword()));
        }

        return recruiterRepository.save(recruiter);
    }

    public void deleteRecruiter(String id) {
        Recruiter recruiter = getRecruiterById(id);
        recruiterRepository.delete(recruiter);
    }
}