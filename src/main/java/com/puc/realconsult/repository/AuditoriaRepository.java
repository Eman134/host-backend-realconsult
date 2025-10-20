package com.puc.realconsult.repository;

import com.puc.realconsult.model.AuditoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaModel, Long> {


}