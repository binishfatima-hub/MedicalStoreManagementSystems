package com.example.medicarebackend.repository;

import com.example.medicarebackend.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {

    /** Search box: matches medicine name OR company name. */
    List<Medicine> findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCase(
            String name, String company);

    List<Medicine> findAllByOrderByNameAsc();
}
