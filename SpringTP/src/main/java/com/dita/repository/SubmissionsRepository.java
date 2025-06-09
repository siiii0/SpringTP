package com.dita.repository;

import com.dita.domain.Q_Language;
import com.dita.domain.Question;
import com.dita.domain.Submissions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionsRepository extends JpaRepository<Submissions, Integer> {

}
