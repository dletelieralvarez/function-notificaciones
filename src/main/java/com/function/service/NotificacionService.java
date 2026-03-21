package com.function.service;
import java.util.logging.Logger;

public class NotificacionService {
    public void enviar(String canal, String destino, String mensaje, Logger log) {

        switch (canal.toLowerCase()) {
            case "email":
                log.info("Enviando EMAIL a " + destino);
                break;
            case "sms":
                log.info("Enviando SMS a " + destino);
                break;
            case "whatsapp":
                log.info("Enviando WHATSAPP a " + destino);
                break;
            default:
                log.warning("Canal no soportado: " + canal);
        }

        log.info("Mensaje: " + mensaje);
    }
}
