package com.fisei.athanasiaapp.models;

import java.util.List;

public class SaleRequest_DIAS {
    public int UserClientID;
    public List<SaleDetails_DIAS> SaleDetails_DIAS;

    public SaleRequest_DIAS(int id, List<SaleDetails_DIAS> details){
        this.UserClientID = id;
        this.SaleDetails_DIAS = details;
    }
    public SaleRequest_DIAS(){}
}