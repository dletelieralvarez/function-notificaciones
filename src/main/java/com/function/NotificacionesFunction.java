package com.function;

import java.util.Optional;
import java.util.logging.Logger;
import com.function.model.NotificacionRequest;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class NotificacionesFunction {
    @FunctionName("NotificacionesPrototipo")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "notificaciones/enviar"
        ) HttpRequestMessage<Optional<NotificacionRequest>> request,
        final ExecutionContext context
    ) {
        Logger log = context.getLogger();

        try {
            NotificacionRequest body = request.getBody().orElse(null);

            if (body == null) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .body("El body no puede venir vacío.")
                        .build();
            }

            if (isNullOrEmpty(body.getEvento()) ||
                isNullOrEmpty(body.getCanal()) ||
                isNullOrEmpty(body.getDestino()) ||
                isNullOrEmpty(body.getMensaje())) {

                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .body("Faltan campos obligatorios: evento, canal, destino, mensaje.")
                        .build();
            }

            // Simulación
            log.info("=== PROTOTIPO NOTIFICACION ===");
            log.info("Evento  : " + body.getEvento());
            log.info("Canal   : " + body.getCanal());
            log.info("Destino : " + body.getDestino());
            log.info("Mensaje : " + body.getMensaje());

            String resultado = String.format(
                "Notificación simulada enviada. Evento=%s, Canal=%s, Destino=%s",
                body.getEvento(), body.getCanal(), body.getDestino()
            );

            return request.createResponseBuilder(HttpStatus.OK)
                    .body(resultado)
                    .build();

        } catch (Exception e) {
            log.severe("Error en la función de notificaciones: " + e.getMessage());

            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error procesando la notificación.")
                    .build();
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
