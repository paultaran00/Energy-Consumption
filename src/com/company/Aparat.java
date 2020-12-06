package com.company;

import java.io.Serializable;

/**
 * Aparat.java - clasa defineste un aparat
 * @author Paul
 * @version 1.0
 * @since 2020
 */

public class Aparat implements Serializable {
    private static final long serialVersionUID = 1L;

    Tip tip;
    String nume;
    int consum;  //w/h
    int ore;    //ore
    String zi_luna;
    String descriere;

    public Aparat() {
        this.tip = Tip.Necunoscut;
    }

    /**
     * Constructor explicit al clasei Aparat
     * @param tip
     * @param nume
     * @param consum
     * @param ore
     * @param zi_luna
     * @param descriere
     */

    public Aparat(Tip tip, String nume, int consum, int ore,String zi_luna , String descriere) {
        this.tip = tip;
        this.nume = nume;
        this.consum = consum;
        this.ore = ore;
        this.zi_luna = zi_luna;
        this.descriere = descriere;
    }

    /**
     * Functia toString
     * @return String
     */

    @Override
    public String toString() {
        return "Tip: "+ tip + "\nNume: " + nume + "\nConsum: " + consum + " (W/H)" + "\nTimp utilizare: " + ore + " ore/" +zi_luna + "\nDescriere: " + descriere;
    }

    /**
     * Getter-uri si Setter-uri
     */
    public Tip getTip() {
        return tip;
    }

    public String getNume() {
        return nume;
    }

    public int getConsum() {
        return consum;
    }

    public int getOre() {
        return ore;
    }

    public String getZi_luna() {
        return zi_luna;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setTip(Tip tip) {
        this.tip = tip;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setConsum(int consum) {
        this.consum = consum;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }

    public void setZi_luna(String zi_luna) {
        this.zi_luna = zi_luna;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
