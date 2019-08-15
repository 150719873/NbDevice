package com.hust.nb.Dao;

import com.hust.nb.Entity.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WarningDao extends JpaRepository<Warning, Warning>, JpaSpecificationExecutor<Warning> {

    List<Warning> findByEnprNoAndCommunityNameAndWarningDate(String enprNo, String communityName, Timestamp currentDay);

    List<Warning> findByEnprNoAndWarningDate(String enprNo, Timestamp currentDay);
}
