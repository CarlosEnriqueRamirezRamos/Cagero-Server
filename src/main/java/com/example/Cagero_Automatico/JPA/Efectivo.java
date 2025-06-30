package com.example.Cagero_Automatico.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Efectivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idefectivo")
    private int IdEfectivo;
    
    @Column(name = "tipo")
    private String Tipo;
    
    @Column(name = "denominacion")
    private double denominacion;
            
    @Column(name = "cantidad")
    private int cantidad;

    public int getIdEfectivo() {
        return IdEfectivo;
    }

    public void setIdEfectivo(int IdEfectivo) {
        this.IdEfectivo = IdEfectivo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public double getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(double denominacion) {
        this.denominacion = denominacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

   
    
}
