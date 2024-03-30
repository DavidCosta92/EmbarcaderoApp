package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.License;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Integer> {
    Page<License> findAllByLicenseCodeContains(String licenseCode, Pageable pageable);
    Optional<License> findByLicenseCode(String licenseCode);

    Boolean existsByLicenseCode (String licenseCode);

    @Query("SELECT l FROM License l " +
            "JOIN Person p ON p.id = l.owner.id " +
            "JOIN RegisteredBoat b ON b.id = l.registeredBoat.id " +
            "WHERE ( l.licenseCode LIKE %:searchValue% " +
            "OR p.name LIKE %:searchValue%  " +
            "OR p.lastName LIKE %:searchValue% " +
            "OR b.name LIKE %:searchValue%)")
    Page<License> getAllBySearchValueContains (String searchValue, Pageable pageable);
}
