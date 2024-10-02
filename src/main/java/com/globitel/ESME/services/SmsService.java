package com.globitel.ESME.services;

import com.globitel.ESME.enums.Status;
import com.globitel.ESME.exceptions.ResourceNotFoundException;
import com.globitel.ESME.models.SMS;
import com.globitel.ESME.repos.SmsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class SmsService {

    @Autowired
    private SmsRepo smsRepo;

    @Autowired
    private SmppService smppService;

    @Autowired
    private ApplicationContext context;

    public List<SMS> getAllSms(){
        List<SMS> smsList = smsRepo.findAll();
        if (smsList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No messages found in the database");

        return smsList;
    }

    public void sendAll(List<SMS> smsList){
        for (SMS sms : smsList) {
            try {
                smppService.sendSms(
                        sms.getSourceAddress(),
                        sms.getDestinationAddress(),
                        sms.getTextBody()
                );
                sms.setStatus(Status.SENT);
                smsRepo.save(sms);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void addSms(SMS sms){
        sms.setCreatedAt(new Date());
        smsRepo.save(sms);
    }

    public void sendAllSms() {
        List<SMS> smsList = smsRepo.findAll();

        if (smsList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No messages found in the database");

        sendAll(smsList);
    }

    public void sendAllPendingSms(){
        List<SMS> smsList = smsRepo.findByStatus(Status.PENDING);

        if (smsList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No messages found in the database");

        sendAll(smsList);
    }

    public void sendSmsById(Long id){
        SMS sms = smsRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No sms found with id: " + id));

        try {
            smppService.sendSms(
                    sms.getSourceAddress(),
                    sms.getDestinationAddress(),
                    sms.getTextBody()
            );
            sms.setStatus(Status.SENT);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void terminateSession() {
//        smppService.disconnect();

        SpringApplication.exit(context, () -> 0);

        System.exit(0);
    }
}
