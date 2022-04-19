package com.example.etas_server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name="translations")
public class Translation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    @Expose
    private Long id;

    @Column(name="fl_value")
    @Expose
    private String flValue;

    @Column(name="sl_value")
    @Expose
    private String slValue;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="dict_id")
    private Dictionary dictionary;

    public Translation() {

    }

    public Translation(String flValue, String slValue) {
        this.flValue = flValue;
        this.slValue = slValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlValue() {
        return flValue;
    }

    public void setFlValue(String flValue) {
        this.flValue = flValue;
    }

    public String getSlValue() {
        return slValue;
    }

    public void setSlValue(String slValue) {
        this.slValue = slValue;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString()
    {
        return String.format("id: %d, flv: %s, slv: %s", id, flValue, slValue);
    }

}
