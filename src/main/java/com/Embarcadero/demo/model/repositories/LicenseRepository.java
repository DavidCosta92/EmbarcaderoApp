package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.License;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {
    Page<License> findAllByLicenseCodeContains(String licenseCode, Pageable pageable);

    Boolean existsByLicenseCode (String licenseCode);
}
