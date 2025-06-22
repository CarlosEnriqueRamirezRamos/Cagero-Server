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
    private String Denominacion;
            
    @Column(name = "cantidad")
    private String Cantidad;

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

    public String getDenominacion() {
        return Denominacion;
    }

    public void setDenominacion(String Denominacion) {
        this.Denominacion = Denominacion;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
    }
    
    
}
