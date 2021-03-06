package model;

import java.util.Objects;

/**
 * @author Herremans Pieter & Adriaenssens Zeno
 */
public class Artikel implements Comparable{
    private String code, omschrijving, groep;
    private double prijs;
    private int voorraad;



    public Artikel(String code, String omschrijving, String groep, double prijs, int voorraad) {
        this.setCode(code);
        this.setOmschrijving(omschrijving);
        this.setGroep(groep);
        this.setPrijs(prijs);
        this.setVoorraad(voorraad);
    }

    public void verminderVoorraad(){this.setVoorraad(this.getVoorraad() - 1);}

    @Override
    public int compareTo(Object o) {
        if(o instanceof Artikel){
            Artikel artikel = (Artikel) o;
            return this.getOmschrijving().compareTo(artikel.getOmschrijving());
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artikel artikel = (Artikel) o;
        return Double.compare(artikel.getPrijs(), prijs) == 0 && voorraad == artikel.getVoorraad() && Objects.equals(code, artikel.getCode()) && Objects.equals(omschrijving, artikel.omschrijving) && Objects.equals(groep, artikel.getGroep());
    }

    @Override
    public int hashCode(){ return Objects.hash(code, omschrijving, groep, prijs, voorraad);}

    public String getCode() {
        return code;
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    private void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public String getGroep() {
        return groep;
    }

    private void setGroep(String groep) {
        this.groep = groep;
    }

    public double getPrijs() {
        return prijs;
    }

    private void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public int getVoorraad() {
        return voorraad;
    }

    private void setVoorraad(int voorraad) {
        this.voorraad = voorraad;
    }

    @Override
    public String toString() {
        String output = "Code: " + code + ",  ";
        output += "Omschrijving: " + omschrijving + ",  ";
        output += "Groep: " + groep + ",  ";
        output += "Prijs: " + prijs + ",  ";
        output += "Voorraad: " + voorraad;
        return output;
    }

}