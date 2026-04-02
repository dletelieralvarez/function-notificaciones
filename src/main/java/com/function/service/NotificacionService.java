package com.function.service;
import java.util.logging.Logger;

//AQUI SE DECIDE QUE HACER SEGUN EL CANAL (EMAL; SMS; WHATSAPP)
//SIMULA EL ENVIO
//REGISTRA LOGS
//DEVUELVE ESTADO
public class NotificacionService {
    public String enviar(String canal, String destino, String mensaje, Logger log) {
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
                return "CANAL_NO_SOPORTADO";
        }

        log.info("Mensaje: " + mensaje);
        return "NOTIFICACION_ENVIADA";
    }
}
