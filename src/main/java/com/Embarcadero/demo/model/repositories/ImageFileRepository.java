package com.Embarcadero.demo.model.repositories;

import com.Embarcadero.demo.model.entities.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile , Integer> {
}
