package com.barberapp.barberapp;

/**
 * Created by Christian on 11-12-2017.
 */

public class Subject {


    String fecha = null;
    String hora = null;
    String solicitud = null;
    String idCliente = null;
    String idBarbero = null;
    String asiento = null;

    public Subject(String fecha, String hora, String solicitud, String idCliente, String idBarbero, String asiento) {

        super();


        this.fecha = fecha;

        this.hora = hora;

        this.solicitud = solicitud;

        this.idCliente = idCliente;

        this.idBarbero = idBarbero;

        this.asiento = asiento;
    }

    public String getFecha() {

        return fecha;

    }
    public void setFecha(String code) {

        this.fecha = code;

    }

    public String getHora() {

        return hora;

    }
    public void setHora(String name) {

        this.hora = name;

    }

    public String getSolicitud() {

        return solicitud;

    }
    public void setSolicitud(String desc) {

        this.solicitud = desc;

    }

    public String getIdCliente() {

        return idCliente;

    }
    public void setIdCliente(String desc) {

        this.idCliente = desc;

    }

    public String getIdBarbero() {

        return idBarbero;

    }
    public void setIdBarbero(String desc) {

        this.idBarbero = desc;

    }

    public String getAsiento() {

        return asiento;

    }
    public void setAsiento(String desc) {

        this.asiento = desc;

    }

    @Override
    public String toString() {

        return  fecha + " " + hora + " " + solicitud + " " + idCliente + " " + idBarbero +" "+ asiento;

    }

}
