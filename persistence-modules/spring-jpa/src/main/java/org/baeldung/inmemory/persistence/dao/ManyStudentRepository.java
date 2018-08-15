package org.baeldung.inmemory.persistence.dao;

import org.baeldung.inmemory.persistence.model.ManyStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManyStudentRepository extends JpaRepository<ManyStudent, Long> {
    List<ManyStudent> findByManyTags_Name(String name);
}
