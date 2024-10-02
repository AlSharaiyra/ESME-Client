package com.globitel.ESME;

import com.globitel.ESME.services.SmppService;
import com.globitel.ESME.services.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EsmeClient implements CommandLineRunner {

    @Autowired
    private SmppService smppService;

    @Autowired
    private SmsService smsService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(EsmeClient.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        smppService.connectToSmsc();

//        smsService.sendAllSms();
//        smppService.disconnect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                smppService.disconnect();
            } catch (Exception e) {
                System.err.println("Error during disconnect: " + e.getMessage());
            }
        }));
    }

}
