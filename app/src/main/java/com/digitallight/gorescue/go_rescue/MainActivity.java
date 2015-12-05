package com.digitallight.gorescue.go_rescue;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends ListActivity {

    //Array untuk menampung data yang di ambil dari MySQL
    private String[] kdSelect;
    private String[] Nama;
    private String[] subNama;
    private String[] Gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Mengeksekusi kelas GetData untuk mengirim permintaan ke MySQL
        new GetData().execute("http://192.168.56.1/IcaksamaCrud/android/getdata.php");
    }

    //Method untuk mengeluarkan event saat list di click
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getBaseContext(), "Terpilih " + Nama[position],
                Toast.LENGTH_LONG).show();
    }

    //Class GetData yang menuruni kelas AsyncTask untuk melakukan requset data dari internet
    private class GetData extends AsyncTask<String, Void, String> {

        // Instansiasi class dialog
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        String Content;
        String Error = null;
        // membuat object class JSONObject yang digunakan untuk menangkap data
        // dengan format json
        JSONObject jObject;
        // instansiasi class ArrayList
        ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

        @Override
        protected String doInBackground(String... params) {
            try {
                Content = CustomHttpClient.executeHttpPost(
                        "http://127.0.0.1/IcaksamaCrud/android/getdata.php",
                        data);
            } catch (ClientProtocolException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (IOException e) {
                Error = e.getMessage();
                cancel(true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return Content;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // menampilkan dialog pada saat proses pengambilan data dari
            // internet
            this.dialog.setMessage("Loading Data..");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // menutup dialog saat pengambilan data selesai
            this.dialog.dismiss();
            if (Error != null) {
                Toast.makeText(getBaseContext(), "Error Connection Internet",
                        Toast.LENGTH_LONG).show();
            } else {
                try {
                    // instansiasi kelas JSONObject
                    jObject = new JSONObject(Content);
                    // mengubah json dalam bentuk array
                    JSONArray menuitemArray = jObject.getJSONArray("select");

                    // mendeskripsikan jumlah array yang bisa di tampung
                    kdSelect = new String[menuitemArray.length()];
                    Nama = new String[menuitemArray.length()];
                    subNama = new String[menuitemArray.length()];
                    Gambar = new String[menuitemArray.length()];

                    // mengisi variable array dengan data yang di ambil dari
                    // internet yang telah dibuah menjadi Array
                    for (int i = 0; i < menuitemArray.length(); i++) {
                        kdSelect[i] = menuitemArray.getJSONObject(i)
                                .getString("kdselect").toString();
                        Nama[i] = menuitemArray.getJSONObject(i)
                                .getString("nama").toString();
                        subNama[i] = menuitemArray.getJSONObject(i)
                                .getString("subnama").toString();
                        Gambar[i] = "http://192.168.56.1/IcaksamaCrud/gambar/"
                                + menuitemArray.getJSONObject(i)
                                .getString("gambar").toString();
                    }
                    // instansiasi class ListAdapter (Buka class ListAdapter)
                    ListAdapter adapter = new ListAdapter(getBaseContext(),
                            Nama, subNama, Gambar);
                    setListAdapter(adapter);
                } catch (JSONException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
    }
}
