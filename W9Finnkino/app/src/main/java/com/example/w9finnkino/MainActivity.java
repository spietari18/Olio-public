package com.example.w9finnkino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private Theaters theaters = null;
    private Spinner selectTheater;
    private ArrayAdapter<Theater> selectTheaterAdapter;
    private ListView listMovies;
    private ArrayAdapter<String> movieAdapter;
    private ArrayList<String> movieList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        theaters = Theaters.getInstance();
        selectTheater = findViewById(R.id.selectTheater);
        selectTheaterAdapter = new ArrayAdapter<Theater>(this, android.R.layout.simple_spinner_item, theaters.getTheaterList());
        selectTheaterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectTheater.setAdapter(selectTheaterAdapter);
        fetchTheaters();
        listMovies = findViewById(R.id.listMovies);
        // listMovies.addView(R.layout.activity_listview);
        // movieAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, movieList);
        movieAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, movieList);
        listMovies.setAdapter(movieAdapter);
    }

    public NodeList readXML(String url, String tag) {
        NodeList nList = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();
            nList = doc.getDocumentElement().getElementsByTagName(tag);
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("NodeList created");
        }
        return nList;
    }

    public void fillMovieList(String url) {
        String tag = "Show";
        // movieList.clear();
        movieAdapter.clear();
        NodeList nList = readXML(url, tag);
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                // movieList.add(title);
                movieAdapter.add(title);
                System.out.println("Added movie: " + title);
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    public void fillMovieListByTime(String url, String startTime, String endTime) {
        String tag = "Show";
        movieAdapter.clear();
        NodeList nList = readXML(url, tag);
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String title = element.getElementsByTagName("Title").item(0).getTextContent();
                String time = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                String[] splitDate = time.split("T");
                time = splitDate[1];
                splitDate = time.split(":");
                time = splitDate[0] + ":" + splitDate[1];
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date startT = sdf.parse(startTime);
                    Date endT = sdf.parse(endTime);
                    Date timeT = sdf.parse(time);
                    if (timeT.after(startT) && timeT.before(endT)) {
                        movieAdapter.add(title);
                        System.out.println("Added movie: " + title + " at: " + time);
                    }
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Check time format (HH:mm)", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String readEditDate() {
        String date = ((EditText) findViewById(R.id.editDate)).getText().toString();
        return date;
    }

    public String readStartTime() {
        String time = ((EditText) findViewById(R.id.editStartTime)).getText().toString();
        return time;
    }

    public String readEndTime() {
        String time = ((EditText) findViewById(R.id.editEndTime)).getText().toString();
        return time;
    }

    public void searchButtonPressed(View v) {
        System.out.println("Seach button pressed...");
        String date = readEditDate();
        String timeStart = readStartTime();
        String timeEnd = readEndTime();
        int areaNo = selectTheater.getSelectedItemPosition();
        String id = theaters.getTheaterList().get(areaNo).getId();
        String url;
        if (date.equals("") && timeStart.equals("") && timeEnd.equals("")) {
            System.out.println("No date given");
            url = "https://www.finnkino.fi/xml/Schedule/?area=" + id;
            fillMovieList(url);
        }
        else {
            System.out.println("Date given: " + date);
            url = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + date;
            fillMovieListByTime(url, timeStart, timeEnd);
        }
    }

    public void fetchTheaters() {
        String url = "https://www.finnkino.fi/xml/TheatreAreas/";
        String tag = "TheatreArea";
        theaters.clearTheaterList();
        NodeList nList = readXML(url, tag);
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String location = element.getElementsByTagName("Name").item(0).getTextContent();
                String id = element.getElementsByTagName("ID").item(0).getTextContent();
                theaters.addTheater(location, id);
            }
        }
        selectTheaterAdapter.notifyDataSetChanged();
    }
}