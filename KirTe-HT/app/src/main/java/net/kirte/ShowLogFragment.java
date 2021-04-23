package net.kirte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ShowLogFragment extends Fragment {
    private final ArrayList<String> logList;

    public ShowLogFragment(ArrayList<String> logList) {
        this.logList = logList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_log, container, false);
        Button returnButton = view.findViewById(R.id.returnButtonLog);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().returnList();
            }
        });
        ListView listView = view.findViewById(R.id.logList);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.res_list_row,
                R.id.textViewRow,
                logList
        );
        listView.setAdapter(listAdapter);
        return view;
    }
}
