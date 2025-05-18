package com.nourmina.jobportal.service;

import com.nourmina.jobportal.dto.CandidateDTO;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
    }

    public Candidate getCandidateByEmail(String email) {
        return candidateRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with email: " + email));
    }

    public List<Candidate> getCandidatesBySkill(String skill) {
        return candidateRepository.findBySkillsContaining(skill);
    }

    public Candidate createCandidate(CandidateDTO candidateDTO) {
        // Check if candidate with email already exists
        if (candidateRepository.findByEmail(candidateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + candidateDTO.getEmail());
        }

        // Create candidate entity from DTO
        Candidate candidate = new Candidate(
                candidateDTO.getFirstName(),
                candidateDTO.getLastName(),
                candidateDTO.getEmail(),
                passwordEncoder.encode(candidateDTO.getPassword()),
                candidateDTO.getResume(),
                candidateDTO.getSkills()
        );

        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(String id, CandidateDTO candidateDTO) {
        Candidate candidate = getCandidateById(id);

        // Update fields
        candidate.setFirstName(candidateDTO.getFirstName());
        candidate.setLastName(candidateDTO.getLastName());
        candidate.setResume(candidateDTO.getResume());
        candidate.setSkills(candidateDTO.getSkills());

        // Only update password if it's provided
        if (candidateDTO.getPassword() != null && !candidateDTO.getPassword().isEmpty()) {
            candidate.setPassword(passwordEncoder.encode(candidateDTO.getPassword()));
        }

        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(String id) {
        Candidate candidate = getCandidateById(id);
        candidateRepository.delete(candidate);
    }
}