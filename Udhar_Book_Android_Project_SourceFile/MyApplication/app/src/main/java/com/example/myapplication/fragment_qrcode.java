package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;

public class fragment_qrcode extends Fragment {

    String user_id,user_name,user_phone_number,user_business_name,user_location;
    ImageView qr_image;
    LinearLayout share_layout;
    Bitmap bitmap = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_qrcode, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "");

        DatabaseHelper myDB = new DatabaseHelper(getContext());
        Cursor cursor = myDB.get_user_details(user_id);
        while (cursor.moveToNext()){
            user_phone_number = cursor.getString(0);
            user_name = cursor.getString(1);
            user_business_name = cursor.getString(2);
            user_location = cursor.getString(3);
        }

        String inputValue = " Name :- "+user_name+"\n\n\n Contact Number :- "+user_phone_number+"\n\n\n Business Name :- "+user_business_name+"\n\n\n Business Location :- "+user_location;

        qr_image = root.findViewById(R.id.qr_image);
        share_layout = root.findViewById(R.id.share_layout);

        WindowManager manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qr_image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("Error_Tag", e.toString());
        }

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = qr_image.getDrawable();
                Bitmap bitmap1 = ((BitmapDrawable)drawable).getBitmap();

                try {
                    File file = new File(getContext().getExternalCacheDir(), File.separator + user_name +"_"+user_business_name+".jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID +".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");
                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
}
}
