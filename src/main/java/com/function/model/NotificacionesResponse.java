package com.function.model;

//MODELO DE SALIDA
//EL CLIENTE PIDE CAMPOS ESPECIFICOS, PERO EL MODELO DEBE CONTENER TODOS LOS CAMPOS DE LA BASE DE DATOS
public class NotificacionesResponse {
    private String evento;
    private String canal;
    private String destino;
    private String mensaje;
    private String estado;

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
