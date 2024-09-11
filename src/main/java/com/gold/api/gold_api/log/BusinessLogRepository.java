package com.gold.api.gold_api.log;

import com.gold.api.gold_api.log.entity.BusinessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessLogRepository extends JpaRepository<BusinessLog, Long> {

}
