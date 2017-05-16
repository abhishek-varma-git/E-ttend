package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class QRGenerator extends Fragment {
    Button generate;
    ImageView image;
    String ImagePath,value;
    Bitmap bitmap;
    Uri URI;
    static String url_data = "http://irretrievable-meter.000webhostapp.com/qru.php";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_qrgenerator, container, false);
        generate = (Button) view.findViewById(R.id.generate);
        image = (ImageView) view.findViewById(R.id.image);
        final String emp_id = SharedPrefManager.getInstance(getActivity()).getteacherempid();
        final String qr = SharedPrefManager.getInstance(getActivity()).getqr();
        if (qr != null) {
            MultiFormatWriter multiFormatWriter2 = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix2 = multiFormatWriter2.encode(qr, BarcodeFormat.QR_CODE, 200, 200);
                BarcodeEncoder barcodeEncoder2 = new BarcodeEncoder();
                final Bitmap bitmap2 = barcodeEncoder2.createBitmap(bitMatrix2);
                image.setImageBitmap(bitmap2);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {

                    final int a = 1 + (int) (Math.random() * 999999999);
                    value = Integer.toString(a);
                    BitMatrix bitMatrix = multiFormatWriter.encode(value, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Updating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url_data,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {

                                        SharedPrefManager.getInstance(getActivity())
                                                .qr(Integer.toString(a));
                                        image.setImageBitmap(bitmap);
                                       //save to gallery
                                        savePicture(bitmap,value);
                                        //Toast.makeText(getActivity(),value,Toast.LENGTH_SHORT).show();
                                        image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
                                                FullImage fragment = new FullImage();
                                                Bundle args = new Bundle();
                                                args.putInt("image", a);
                                                fragment.setArguments(args);
                                                getFragmentManager().beginTransaction().add(R.id.QR_Generator, fragment).commit();
                                            }
                                        });
                                        JSONArray JsonArray = new JSONArray(response);
                                        JSONObject jsonobject = JsonArray.getJSONObject(0);
                                        String code = jsonobject.getString("code");
                                        if (code == "success") {
                                            Toast.makeText(getActivity(), jsonobject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), jsonobject.getString("message"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error! Check for your Internet Connection" +
                                    "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("emp_id", emp_id);
                            params.put("qr_code", value);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
    private void savePicture(Bitmap bm, String imgName)
    {
        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        File f = new File(strDirectory, imgName);
        try {
            fOut = new FileOutputStream(f);
            Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            /**Update image to gallery**/
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    f.getAbsolutePath(), f.getName(), f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
