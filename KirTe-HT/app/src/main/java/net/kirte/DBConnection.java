package net.kirte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Class to create and handle JSON requestes and responses
public class DBConnection {
    private static final String baseUrl = "https://kirte.net/varauskalenteri/Web/Services/";
    private static final DBConnection DB = new DBConnection();
    private static final String[] apiCredentials = {"Username", "Password"}; // TODO hide credentials!

    public static DBConnection getInstance() {
        return DB;
    }

    public User login(String username, String password) {
        String url = baseUrl + "/Authentication/Authenticate";
        String json = null;
        User user = null;
        try {
            json = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response = null;
        try {
            response = new AsyncConnection().execute(url, json).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("isAuthenticated").equals("false")) {
                    MainActivity.getInstance().makeToast("Väärä käyttäjätunnus tai salasana. Yritä uudelleen!");
                    return null;
                }
                user = new User(
                        jsonObject.getString("sessionToken"),
                        jsonObject.getString("sessionExpires"),
                        jsonObject.getString("userId"),
                        jsonObject.getString("version"),
                        jsonObject.getString("links"),
                        jsonObject.getString("message")
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        System.out.println("Login successful, fetching user data...");
        if (user == null) throw new AssertionError();
        url = baseUrl + "/Accounts/:" + user.getId();
        try {
            response = new AsyncConnection().execute(url, null, user.getSession(), user.getId(), "GET").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                user.setName(jsonObject.getString("firstName"));
                user.setEmail(jsonObject.getString("emailAddress"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    // fetch users reservations from server and store them to ArrayList
    public ArrayList<Reservation> getUserReservations(String session, String Uid) {
        String url = baseUrl + "Reservations/?userId=" + Uid;
        String response = null;
        ArrayList<Reservation> reservations = null;
        Reservation reservation;
        JSONObject jsonObject;
        try {
            // response from server as JSON
            response = new AsyncConnection().execute(url, null, session, Uid, "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // parse JSON, initialize ArrayList and fill with data
        if (response != null) {
            try {
                JSONObject responseObject = new JSONObject(response);
                JSONArray jsonArray = responseObject.getJSONArray("reservations");
                reservations = new ArrayList<Reservation>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    // parse datetime to user-friendly string with right timezone
                    String date = parseDate(jsonObject.getString("startDate"), jsonObject.getString("endDate"));
                    reservation = new Reservation(
                            date,
                            jsonObject.getString("resourceName"),
                            jsonObject.getString("creditsConsumed"),
                            jsonObject.getString("referenceNumber")
                    );
                    reservations.add(reservation);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reservations;
    }

    // Get availability
    /*
    public ArrayList<Reservation> getAvailability(String session, String uid) {
        String url = baseUrl + "Resources/Availability";
        String response = null;
        JSONObject jsonObject;
        try {
            response = new AsyncConnection().execute(url, null, session, uid, "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println("Response: " + response);
        }
        return null;
    }

    public void createReservation(Reservation reservation) {
        // create new reservation
    }

     */

    public Boolean cancelReservation(String session, String uid, String refNum) {
        String url = baseUrl + "Reservations/" + refNum;
        String response = null;
        JSONObject jsonObject;
        try {
            response = new AsyncConnection().execute(url, null, session, uid, "DELETE").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println(refNum + " Response: " + response);
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getString("message").equals("The item was deleted")) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // parse "YYYY-MM-DDTHH:mm:ss+HHmm" to "DD.MM.YYYY HH:mm - HH:mm"
    public String parseDate(String startDate, String endDate) {
        String date;
        // Booked scheduler formatter
        DateTimeFormatter jF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        // Parse string to datetime object, timezone from GMT to Europe/Helsinki
        ZonedDateTime sDate = ZonedDateTime.parse(startDate, jF).withZoneSameInstant(ZoneId.of("Europe/Helsinki"));
        ZonedDateTime eDate = ZonedDateTime.parse(endDate, jF).withZoneSameInstant(ZoneId.of("Europe/Helsinki"));
        // Format start and end times to one string. No possibility to have schedule over night.
        DateTimeFormatter sF = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        DateTimeFormatter eF = DateTimeFormatter.ofPattern("HH:mm");
        date = sF.format(sDate) + " - " + eF.format(eDate);
        return date;
    }

    public Boolean markAsPaid(String ref) {
        User api = login(apiCredentials[0], apiCredentials[1]);
        String url = baseUrl + "Reservations/" + ref;
        String response = null;
        JSONObject jsonObject = null;
        JSONArray customAttributes;
        JSONObject customAttribute;
        JSONObject requestObject = new JSONObject();
        try {
            response = new AsyncConnection().execute(url, null, api.getSession(), api.getId(), "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println(response);
            try {
                jsonObject = new JSONObject(response);
                customAttributes = jsonObject.getJSONArray("customAttributes");
                for (int i = 0; i < customAttributes.length(); i++) {
                    customAttribute = customAttributes.getJSONObject(i);
                    if (customAttribute.getString("label").equals("MobilePay")) {
                        customAttribute.put("value", ref);
                        customAttributes.put(i, customAttribute);
                        requestObject.put("customAttributes", customAttributes);
                        /*requestObject.put("accessories", jsonObject.getJSONArray("accessories"));
                        requestObject.put("description", jsonObject.getString("description"));
                        requestObject.put("endDateTime", jsonObject.getString("endDate"));
                        requestObject.put("invitees", jsonObject.getJSONArray("invitees"));
                        requestObject.put("participants", jsonObject.getJSONArray("participants"));
                        requestObject.put("participatingGuests", null);
                        requestObject.put("invitedGuests", null);
                        requestObject.put("recurrenceRule", jsonObject.getJSONObject("recurrenceRule"));
                        requestObject.put("startDateTime", jsonObject.getString("startDate"));
                        requestObject.put("title", jsonObject.getString("title"));
                        requestObject.put("userId", jsonObject.getJSONObject("owner").getString("userId"));
                        requestObject.put("startReminder", null);
                        requestObject.put("endReminder", null);
                        requestObject.put("allowParticipation", jsonObject.getString("allowParticipation"));
                        requestObject.put("retryParameters", null);
                        requestObject.put("termsAccepted", true); */
                        // url = baseUrl + "Reservations/:" + ref;
                        response = new AsyncConnection().execute(url, requestObject.toString(), api.getSession(), api.getId(), "POST").get();
                        if (response != null) {
                            jsonObject = new JSONObject(response);
                            if (jsonObject.getString("message").equals("null")) {
                                return true;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getPaymentStatus(String ref) {
        User api = login(apiCredentials[0], apiCredentials[1]);
        String url = baseUrl + "Reservations/" + ref;
        String response = null;
        JSONObject jsonObject;
        JSONArray customAttributes;
        try {
            response = new AsyncConnection().execute(url, null, api.getSession(), api.getId(), "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println(response);
            try {
                jsonObject = new JSONObject(response);
                customAttributes = jsonObject.getJSONArray("customAttributes");
                for (int i = 0; i < customAttributes.length(); i++) {
                    jsonObject = customAttributes.getJSONObject(i);
                    if (jsonObject.getString("label").equals("MobilePay")) {
                        return jsonObject.getString("value").trim();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
