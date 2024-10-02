package com.globitel.ESME.repos;

import com.globitel.ESME.enums.Status;
import com.globitel.ESME.models.SMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsRepo extends JpaRepository <SMS, Long> {
    List<SMS> findByStatus(Status status);

}
