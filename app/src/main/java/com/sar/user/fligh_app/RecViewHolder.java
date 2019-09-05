package com.sar.user.fligh_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private List<Ticket> contactList;
    private TicketsAdapterListener listener;
    @BindView(R.id.airline_name)
    TextView airlineName;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.number_of_stops)
    TextView stops;

    @BindView(R.id.number_of_seats)
    TextView seats;

    @BindView(R.id.departure)
    TextView departure;

    @BindView(R.id.arrival)
    TextView arrival;

    @BindView(R.id.duration)
    TextView duration;

    @BindView(R.id.price)
    TextView price;

    @BindView(R.id.loader)
    SpinKitView loader;

    public RecViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTicketSelected(contactList.get(getAdapterPosition()));
            }
        });

    }
    public interface TicketsAdapterListener {
        void onTicketSelected(Ticket contact);
    }

}
