package com.aadhil.cineworlddigital.fragment;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aadhil.cineworlddigital.HomeActivity;
import com.aadhil.cineworlddigital.PaymentActivity;
import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.adapter.TicketAdapter;
import com.aadhil.cineworlddigital.model.CheckoutInfo;
import com.aadhil.cineworlddigital.model.Invoice;
import com.aadhil.cineworlddigital.model.Ticket;
import com.aadhil.cineworlddigital.service.ActivityNavigator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CompletePayment extends Fragment {

    private String channelId = "info";
    private NotificationManager manager;

    public CompletePayment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmenet, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmenet, savedInstanceState);

        // Set Notification Manager and Notification Channel
        setNotificationChannel();

        // Get activity navigator
        ActivityNavigator navigator = ActivityNavigator.getNavigator(getContext(),
                getActivity().findViewById(R.id.parentLayoutPayment));

        // Load and show tickets
        loadTickets();

        // Request permission if not allowed previously
        requestPermission();

        Button button = fragmenet.findViewById(R.id.button24);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a filename to the pdf
                int endIndex = Math.min(10, PaymentActivity.checkoutInfo.getMovieName().length());
                String movieName = PaymentActivity.checkoutInfo.getMovieName()
                        .substring(0, endIndex)
                        .replace(" ", "-")
                        .toLowerCase();
                String timestamp = String.valueOf(System.currentTimeMillis());
                String filename = "e-ticket-" + movieName + "-" + timestamp + ".pdf";

                // Get the filepath
                File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File filePath = new File(downloadDir, filename);

                // Print pdf to Phone
                downloadTicketTo(filePath);

                // Go to Home
                navigator.setRedirection(new ActivityNavigator.NavigationManager() {
                    @Override
                    public void redirect() {
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);
                    }
                });
            }
        });
    }

    private void loadTickets() {
        ArrayList<Ticket> datalist = new ArrayList<>();

        CheckoutInfo checkoutInfo = PaymentActivity.checkoutInfo;
        Invoice invoice = PaymentActivity.invoice;

        for(int i=0; i<checkoutInfo.getSelectedSeats().size(); i++) {
            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfOut = new SimpleDateFormat("EEE dd, MMM yyyy");

            // Get showDate
            String showDate = "YYYY-MM-DD";
            try {
                Date date = sdfIn.parse(checkoutInfo.getDate());
                showDate = sdfOut.format(date).toUpperCase();
            } catch (ParseException | NullPointerException e) {
                e.printStackTrace();
            }

            // Create a Ticket instance and add it to arraylist
            Ticket ticket = new Ticket();
            ticket.setMovieName(checkoutInfo.getMovieName());
            ticket.setSeatNo(checkoutInfo.getSelectedSeats().get(i));
            ticket.setShowDate(showDate);
            ticket.setShowTime(checkoutInfo.getShowTime());
            ticket.setPrice(String.valueOf(checkoutInfo.getPrice().intValue()/checkoutInfo.getSelectedSeats().size()));
            ticket.setIssuedDateTime(invoice.getDatetime());
            ticket.setRefNo(invoice.getTransactionId());
            datalist.add(ticket);
        }

        // Set ticket adapter to the recycler view
        RecyclerView.Adapter adapter = new TicketAdapter((AppCompatActivity) getActivity(), datalist).getAdapter();
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS
        }, 1232);
    }

    private void downloadTicketTo(File filePath) {
        if(convertXmlToPdf(filePath)) {
            Toast.makeText(getContext(), "The ticket is saved to your Downloads!", Toast.LENGTH_SHORT).show();

            // Show notification
            showNotification();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error While Downloading")
                .setMessage("Your e-Ticket is not downloaded correctly. Please get screenshots of tickets showing on screen.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadTicketTo(filePath);
                    }
                })
                .create()
                .show();
        }
    }

    private boolean convertXmlToPdf(File filePath) {
        View view = getActivity().findViewById(R.id.recyclerView4);
        DisplayMetrics metrics = new DisplayMetrics();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getActivity().getDisplay().getRealMetrics(metrics);
        } else {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }

        view.measure(
            View.MeasureSpec.makeMeasureSpec(metrics.widthPixels, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();

        // Get the width and height of the view
        int viewHeight = view.getMeasuredHeight();

        // Create a PageInfo instance specifying the page attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(metrics.widthPixels, viewHeight, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas instance to draw on the page
        Canvas canvas = page.getCanvas();

        // Create a Paint instance for styling the view
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw the view on the canvas
        view.draw(canvas);

        // Finish the page
        document.finishPage(page);

        try {
            // Save the pdf
            FileOutputStream fos = new FileOutputStream(filePath);
            document.writeTo(fos);
            document.close();
            fos.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void setNotificationChannel() {
        NotificationManager manager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(channelId, "INFO", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
        this.manager = manager;
    }

    private void showNotification() {
        Notification notification = new NotificationCompat.Builder(getContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("CineWorld Digital")
                .setSubText("e-Ticket")
                .setContentText("Your e-Ticket downloaded successfully to Downloads.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Your e-Ticket downloaded successfully to Downloads."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getActivity().getColor(R.color.primary_theme))
                .build();
        manager.notify(1, notification);
    }
}