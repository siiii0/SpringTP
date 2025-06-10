package com.dita.repository;

import com.dita.domain.Submissions;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Integer> {

}
