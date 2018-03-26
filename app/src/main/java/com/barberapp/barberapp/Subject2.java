package com.barberapp.barberapp;

/**
 * Created by Christian on 11-12-2017.
 */

public class Subject2 {

    String barberoId = null;
    String tipo = null;
    String descripcion = null;

    public Subject2(String barberoId, String tipo, String descripcion) {

        super();

        this.barberoId = barberoId;

        this.tipo = tipo;

        this.descripcion = descripcion;
    }

    public String getBarberoId() {

        return barberoId;

    }
    public void setBarberoId(String code) {

        this.barberoId = code;

    }

    public String getTipo() {

        return tipo;

    }
    public void setTipo(String name) {

        this.tipo = name;

    }

    public String getDescripcion() {

        return descripcion;

    }
    public void setDescripcion(String desc) {

        this.descripcion = desc;

    }

    @Override
    public String toString() {

        return  barberoId + " " + tipo + " " + descripcion ;

    }

}
