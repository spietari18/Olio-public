package net.kirte;

import android.app.AlarmManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;


// Class for reservation objects that are stored in a list
public class Reservation {
    private final String datetime;
    private final String court;
    private final String price;
    private final String refNum;

    public Reservation(String datetime, String court, String price, String refNum) {
        this.court = court;
        this.datetime = datetime;
        if (price == null || price.equals("null")) {
            this.price = "10.00";
        } else {
            this.price = price;
        }
        this.refNum = refNum;
    }

    public String getCourt() {
        return court;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getRefNum() { return refNum; }

    public void openDoor() {
        MainActivity.getInstance().callToDoor();
    }

    public void cancelReservation() {
        ReservationBook.getInstance().removeReservation(this);
    }

    public Boolean remind(int minutes) {
        if (MainActivity.getInstance().reminder(datetime, minutes)) return true;
        return false;
    }

    public String toString() {
        return datetime + " " + court;
    }

    public String getPrice() {
        return price;
    }
}
