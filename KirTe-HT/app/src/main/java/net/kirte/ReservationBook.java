package net.kirte;

import java.util.ArrayList;

// Class to handle everything with reservations. Is a link from MainActivity to DBConnection. Is used also to store currently user data
public class ReservationBook {
    private ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    private User user;
    private final DBConnection db = DBConnection.getInstance();
    // private ArrayList<Reservation> freeReservations;
    private static ReservationBook resBook;

    public ReservationBook() {
    }

    public static ReservationBook getInstance() {
        if (resBook == null) {
            resBook = new ReservationBook();
        }
        return resBook;
    }

    public Boolean fetchReservationList() {
        reservationList = db.getUserReservations(user.getSession(), user.getId());
        if (reservationList == null) {
            return false;
        }
        return true;
    }

    public ArrayList<Reservation> getReservations() {
        return reservationList;
    }

    public String getUserName() {
        return user.getName();
    }

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
    }

    // remove also locally if successful and then return to the list
    public void removeReservation(Reservation reservation) {
        if (db.cancelReservation(user.getSession(), user.getId(), reservation.getRefNum())) {
            reservationList.remove(reservation);
            MainActivity.getInstance().returnList();
        } else {
            System.out.println("Removing failed...");
        }
    }

    public void payReservation(Reservation reservation) {
        MPConnection.getInstance().newPayment(reservation.getRefNum(), reservation.getPrice());
    }

    public Boolean login(String username, String password) {
        user = db.login(username, password);
        if (user == null) {
            return false;
        } else if (user.getSession() == null){
            MainActivity.getInstance().makeToast(MainActivity.getInstance().getString(R.string.wrong_credentials));
            return false;
        } else {
            return true;
        }
    }

    public void markAsPaid(String ref) {
        if (!db.markAsPaid(ref)) {
            System.out.println("Payment not registered in KirTe server!");
        }
    }

    // Using browser.
    /*
    public ArrayList<String> fetchFreeRes() {
        ArrayList<Reservation> reservations = db.getAvailability(user.getSession(), user.getId());
        // create list of free turns and show already reserved also
        return null;
    }

    // Reservation test list
    public void createTestList() {
        addReservation(new Reservation("date0", "Court 1", "10,00€", "0"));
        addReservation(new Reservation("date1", "Court 2", "10,00€", "1"));
        addReservation(new Reservation("date2", "Court 1", "10,00€", "2"));
        addReservation(new Reservation("date3", "Court 2", "10,00€", "3"));
    } */
}
