package net.kirte;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;

// ReservationList fragment management
public class ResListFragment extends Fragment {
    private final String username;
    private final ArrayList<Reservation> resList;

    public ResListFragment(ArrayList<Reservation> reservations, String username) {
        resList = reservations;
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.res_list_fragment, container, false);
        TextView textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewUsername.setText(username);
        Button createResButton = view.findViewById(R.id.NewResButton);
        createResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.getInstance().createResList();
                MainActivity.getInstance().newRes();
            }
        });
        Button logOutButton = view.findViewById(R.id.returnButtonNewRes);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().logout();
            }
        });
        ListView resListView = view.findViewById(R.id.resList);
        resListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                MainActivity.getInstance().openReservation(pos);
            }
        });
        ArrayAdapter<Reservation> resListAdapter = new ArrayAdapter<Reservation>(
                getActivity(),
                R.layout.res_list_row,
                R.id.textViewRow,
                resList
        );
        resListView.setAdapter(resListAdapter);
        TextView listEmpty = view.findViewById(R.id.listEmptyText);
        resListView.setEmptyView(listEmpty);
        SwipeRefreshLayout sRL = view.findViewById(R.id.swipe_refresh);
        sRL.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        MainActivity.getInstance().refreshList();
                    }
                }
        );
        return view;
    }
}
