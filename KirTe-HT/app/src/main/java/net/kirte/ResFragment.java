package net.kirte;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ResFragment extends Fragment {
    private final Reservation reservation;

    public ResFragment(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.res_fragment, container, false);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.cancelReservation();
            }
        });
        Button remindButton = view.findViewById(R.id.remindButton);
        remindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText minutes = view.findViewById(R.id.remindMinutes);
                if (!minutes.getText().toString().isEmpty()) {
                    if (reservation.remind(Integer.parseInt(minutes.getText().toString()))) {
                        MainActivity.getInstance().makeToast(MainActivity.getInstance().getString(R.string.remind_set));
                        minutes.setText("");
                    }
                } else {
                    MainActivity.getInstance().makeToast(MainActivity.getInstance().getString(R.string.remind_missing_minutes));
                }
            }
        });
        ImageButton payButton = view.findViewById(R.id.payButton);
        String status = DBConnection.getInstance().getPaymentStatus(reservation.getRefNum());
        if (status == null || status.equals("null") || status.isEmpty()) {
            payButton.setEnabled(true);
        } else {
            payButton.setEnabled(false);
        }
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationBook.getInstance().payReservation(reservation);
                // DBConnection.getInstance().markAsPaid(reservation.getRefNum());
            }
        });
        Button openDoorButton = view.findViewById(R.id.doorButton);
        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservation.openDoor();
            }
        });
        Button returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().returnList();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView resDatetime = view.findViewById(R.id.resDatetime);
        TextView resCourt = view.findViewById(R.id.resCourt);
        resDatetime.setText(reservation.getDatetime());
        resCourt.setText(reservation.getCourt());
    }

}