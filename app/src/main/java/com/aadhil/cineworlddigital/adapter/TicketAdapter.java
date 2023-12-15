package com.aadhil.cineworlddigital.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.Ticket;
import com.aadhil.cineworlddigital.util.QRUtil;

import java.util.ArrayList;

public class TicketAdapter {
    final private AppCompatActivity activity;
    final private ArrayList<Ticket> datalist;

    public TicketAdapter(AppCompatActivity activity, ArrayList<Ticket> datalist) {
        this.activity = activity;
        this.datalist = datalist;
    }

    public RecyclerView.Adapter getAdapter() {
        return createAdapter();
    }

    private RecyclerView.Adapter createAdapter() {
        return new RecyclerView.Adapter<TicketViewHolder>() {
            @NonNull
            @Override
            public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                View ticketView = inflater.inflate(R.layout.ticket, parent, false);
                return new TicketViewHolder(ticketView);
            }

            @Override
            public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
                // Set QR to ImageView
                String sourceForQr = datalist.get(position).getRefNo() + datalist.get(position).getSeatNo();
                Bitmap qrBitmap = QRUtil.getQRBitmap(sourceForQr, activity);
                holder.qrCode.setImageBitmap(qrBitmap);

                // Set other details
                holder.movieName.setText(datalist.get(position).getMovieName());
                holder.seatNumber.setText(datalist.get(position).getSeatNo());
                holder.showDate.setText(datalist.get(position).getShowDate());
                holder.showTime.setText(datalist.get(position).getShowTime());
                holder.price.setText(datalist.get(position).getPrice());
                holder.issuedDateTime.setText(datalist.get(position).getIssuedDateTime());
                holder.refNo.setText(datalist.get(position).getRefNo());
            }

            @Override
            public int getItemCount() {
                return datalist.size();
            }
        };
    }
}
