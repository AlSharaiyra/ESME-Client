package com.globitel.ESME.controllers;

import com.globitel.ESME.models.SMS;
import com.globitel.ESME.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/messages")
    public List<SMS> getAllSms() {
        return smsService.getAllSms();
    }

    @PostMapping("/add")
    public void addSms(@RequestBody SMS sms) {
        smsService.addSms(sms);
    }

//    @PostMapping("/send/all")
//    public void sendAllSms(){
//        smsService.sendAllSms();
//    }

    @PostMapping("/send/all")
    public ResponseEntity<String> sendAllSms() {
        try {
            smsService.sendAllSms();
            return ResponseEntity.ok("All pending SMS messages sent successfully.");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS messages.");
        }
    }

    @PostMapping("/send/pending")
    public ResponseEntity<String> sendAllPendingSms() {
        try {
            smsService.sendAllPendingSms();
            return ResponseEntity.ok("All pending SMS messages sent successfully.");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send SMS messages.");
        }    }

    @PostMapping("/send/by-id/{id}")
    public void sendSmsById(@PathVariable Long id) {
        smsService.sendSmsById(id);
    }

    @PostMapping("/terminate")
    public void terminateSession() throws Exception {
        smsService.terminateSession();
//        return ResponseEntity.ok().body("Application and session terminated successfully.");
    }
}
