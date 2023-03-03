package com.example.prueba.model;

public class Personaje {
    private String imagenURL;
    private String nombre;
    private String genero;
    private String frase;
    private String signo;
    private String cumple;
    private String personalidad;
    private String especie;
    private String muletilla;
    private String ropa;

    /**
     * Constructor con imagen, nombre, género, frase, signo, cumple, personalidad, especie, muletilla y ropa
     * @param imagenURL url de la imagen
     * @param nombre nombre del personaje
     * @param genero género del personaje
     * @param frase frase del personaje
     * @param signo signo zodiaco del personaje
     * @param cumple cumpleaños del personaje
     * @param personalidad personalidad del personaje
     * @param especie especie(perro, gato..) del personaje
     * @param muletilla muletilla que utiliza el personaje
     * @param ropa ropa que utiliza el personaje
     */
    public Personaje(String imagenURL, String nombre, String genero, String frase, String signo, String cumple, String personalidad, String especie, String muletilla, String ropa) {
        this.imagenURL = imagenURL;
        this.nombre = nombre;
        this.genero = genero;
        this.frase = frase;
        this.signo = signo;
        this.cumple = cumple;
        this.personalidad = personalidad;
        this.especie = especie;
        this.muletilla = muletilla;
        this.ropa = ropa;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public String getCumple() {
        return cumple;
    }

    public void setCumple(String cumple) {
        this.cumple = cumple;
    }

    public String getPersonalidad() {
        return personalidad;
    }

    public void setPersonalidad(String personalidad) {
        this.personalidad = personalidad;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getMuletilla() {
        return muletilla;
    }

    public void setMuletilla(String muletilla) {
        this.muletilla = muletilla;
    }

    public String getRopa() {
        return ropa;
    }

    public void setRopa(String ropa) {
        this.ropa = ropa;
    }
}