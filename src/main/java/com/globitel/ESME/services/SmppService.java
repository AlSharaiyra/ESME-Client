package com.globitel.ESME.services;

import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmppService {

    private static final Logger logger = LoggerFactory.getLogger(SmppService.class);
    private SMPPSession session;

    @Value("${smpp.smsc.ip}")
    private String smscIp;

    @Value("${smpp.smsc.port}")
    private int smscPort;

    @Value("${smpp.system_id}")
    private String systemId;

    @Value("${smpp.password}")
    private String password;

    @Value("${smpp.system_type:Logica}") // Optional default value
    private String systemType;

    private final String SERVICE_TYPE = "CMT";


    public void connectToSmsc() throws Exception {
        session = new SMPPSession();
        try {
            logger.info("Attempting to connect and bind to SMSC at IP: {} and Port: {}", smscIp, smscPort);
            session.connectAndBind(
                    smscIp, smscPort,
                    BindType.BIND_TRX,
                    systemId, password,
                    "", TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    null
            );

            logger.info("Successfully bound to SMSC.");
        } catch (Exception e) {
            logger.error("Failed to bind to SMSC: {}", e.getMessage());
        }
    }

    public void sendSms(String sourceAddress, String destinationAddress, String textBody) {

        TypeOfNumber sourceAddTon = getSourceAddTon(sourceAddress);

        try {
            RegisteredDelivery registeredDelivery = new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT);

            SubmitSm submitSm = new SubmitSm();
            submitSm.setSourceAddr(sourceAddress);
            submitSm.setDestAddress(destinationAddress);

            byte[] message = textBody.getBytes();
            submitSm.setShortMessage(message);
//            session.submitShortMessage(submitSm);

            session.submitShortMessage(SERVICE_TYPE,
                    sourceAddTon, NumberingPlanIndicator.UNKNOWN, sourceAddress,
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, destinationAddress,
                    new ESMClass(), (byte) 0, (byte) 1, null, null, registeredDelivery, (byte) 0,
                    new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte) 0,
                    message, new OptionalParameter[0]);


            logger.info("SMS sent successfully to {}", destinationAddress);
        } catch (Exception e) {
            logger.error("Failed to send SMS: {}", e.getMessage());
        }
    }

    public void disconnect() throws Exception {
        if (session != null) {
            session.unbindAndClose();
            logger.info("Successfully unbound from SMSC.");
        }
    }

    private TypeOfNumber getSourceAddTon(String sourceAddress) {
        if (sourceAddress.matches("[0-9]+")) {
            return TypeOfNumber.UNKNOWN;
        } else {
            return TypeOfNumber.ALPHANUMERIC;
        }
    }

}
