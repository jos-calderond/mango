package com.mindhub.homebanking.utilities;

import java.time.LocalDateTime;

public class Indicators {
    private LocalDateTime fecha;
    private Indicator uf;
    private Indicator ivp;
    private Indicator dolar;
    private Indicator dolar_intercambio;
    private Indicator euro;
    private Indicator ipc;
    private Indicator utm;
    private Indicator imacec;
    private Indicator tpm;
    private Indicator libra_cobre;
    private Indicator tasa_desempleo;
    private Indicator bitcoin;

    public Indicators() {
    }

    public Indicators(LocalDateTime fecha, Indicator uf, Indicator ivp, Indicator dolar, Indicator dolar_intercambio, Indicator euro, Indicator ipc, Indicator utm, Indicator imacec, Indicator tpm, Indicator libra_cobre, Indicator tasa_desempleo, Indicator bitcoin) {
        this.uf = uf;
        this.ivp = ivp;
        this.dolar = dolar;
        this.dolar_intercambio = dolar_intercambio;
        this.euro = euro;
        this.ipc = ipc;
        this.utm = utm;
        this.imacec = imacec;
        this.tpm = tpm;
        this.libra_cobre = libra_cobre;
        this.tasa_desempleo = tasa_desempleo;
        this.bitcoin = bitcoin;
    }

    public Indicator getUf() {
        return uf;
    }

    public void setUf(Indicator uf) {
        this.uf = uf;
    }

    public Indicator getIvp() {
        return ivp;
    }

    public void setIvp(Indicator ivp) {
        this.ivp = ivp;
    }

    public Indicator getDolar() {
        return dolar;
    }

    public void setDolar(Indicator dolar) {
        this.dolar = dolar;
    }

    public Indicator getDolar_intercambio() {
        return dolar_intercambio;
    }

    public void setDolar_intercambio(Indicator dolar_intercambio) {
        this.dolar_intercambio = dolar_intercambio;
    }

    public Indicator getEuro() {
        return euro;
    }

    public void setEuro(Indicator euro) {
        this.euro = euro;
    }

    public Indicator getIpc() {
        return ipc;
    }

    public void setIpc(Indicator ipc) {
        this.ipc = ipc;
    }

    public Indicator getUtm() {
        return utm;
    }

    public void setUtm(Indicator utm) {
        this.utm = utm;
    }

    public Indicator getImacec() {
        return imacec;
    }

    public void setImacec(Indicator imacec) {
        this.imacec = imacec;
    }

    public Indicator getTpm() {
        return tpm;
    }

    public void setTpm(Indicator tpm) {
        this.tpm = tpm;
    }

    public Indicator getLibra_cobre() {
        return libra_cobre;
    }

    public void setLibra_cobre(Indicator libra_cobre) {
        this.libra_cobre = libra_cobre;
    }

    public Indicator getTasa_desempleo() {
        return tasa_desempleo;
    }

    public void setTasa_desempleo(Indicator tasa_desempleo) {
        this.tasa_desempleo = tasa_desempleo;
    }

    public Indicator getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(Indicator bitcoin) {
        this.bitcoin = bitcoin;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
