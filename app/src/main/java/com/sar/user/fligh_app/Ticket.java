package com.sar.user.fligh_app;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Ticket {
     String from;
     String to;

   @SerializedName("flight_number")
    String flightNumber;
     String departure;
     String arrival;
     String duration;

    Airline airline;

    Price price;

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    private String instructions;
    @SerializedName("stops")
    private int stops;

    public String getFrom() {
        return from;
    }



    public String getTo() {
        return to;
    }


    public String getFlightNumber() {
        return flightNumber;
    }



    public String getDeparture() {
        return departure;
    }



    public String getArrival() {
        return arrival;
    }



    public String getDuration() {
        return duration;
    }


    public String getInstructions() {
        return instructions;
    }



    public int getNumberOfStops() {
        return stops;
    }




    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Ticket)) {
            return false;
        }

        return flightNumber.equalsIgnoreCase(((Ticket) obj).getFlightNumber());
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.flightNumber != null ? this.flightNumber.hashCode() : 0);
        return hash;

    }
}
