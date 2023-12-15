package com.aadhil.cineworlddigital.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aadhil.cineworlddigital.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRUtil {

    public static Bitmap getQRBitmap(@NonNull String source, @NonNull AppCompatActivity activity) {
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimension = Math.min(width, height);
        dimension = dimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(source, null, QRGContents.Type.TEXT, dimension);
        qrgEncoder.setColorBlack(activity.getColor(R.color.primary_foreground));
        qrgEncoder.setColorWhite(activity.getColor(R.color.primary_background));
        try {
            return qrgEncoder.getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
