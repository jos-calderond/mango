package com.mindhub.homebanking.utilities;

import java.time.LocalDateTime;

public class Indicator {
    private String codigo;
    private String nombre;
    private String unidad_medida;
    private LocalDateTime fecha;
    private Double valor;

    public Indicator() {
    }

    public Indicator(String codigo, String nombre, String unidad_medida, LocalDateTime fecha, Double valor) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidad_medida = unidad_medida;
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
