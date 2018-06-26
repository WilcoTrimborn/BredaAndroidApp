package com.example.wilco.breda;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.wilco.breda.applogic.BredaPhotoAdapter;
import com.example.wilco.breda.applogic.BredaPhotoOnClickListener;
import com.example.wilco.breda.applogic.NewPhotoListener;
import com.example.wilco.breda.dao.RetrievePhotoAsync;
import com.example.wilco.breda.domain.BredaPhoto;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewPhotoListener, AdapterView.OnItemSelectedListener{

    private Spinner cameraSpinner;
    private final String[]spinnerCameras = {"Foto's"};
    private ArrayList<BredaPhoto> bredaPhotos;
    private BredaPhotoAdapter bredaPhotoAdapter;
    private final String apiBaseUrl = "https://services7.arcgis.com/21GdwfcLrnTpiju8/arcgis/rest/services/Sierende_elementen/FeatureServer/0/query?where=1%3D1&outFields=*&outSR=4326&f=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bredaPhotos = new ArrayList<BredaPhoto>();

        this.cameraSpinner = findViewById(R.id.cameraSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , this.spinnerCameras);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        cameraSpinner.setAdapter(spinnerAdapter);
        cameraSpinner.setOnItemSelectedListener(this);

        ListView photoListView = findViewById(R.id.photoListView);
        this.bredaPhotoAdapter = new BredaPhotoAdapter(getLayoutInflater(), bredaPhotos);
        photoListView.setAdapter(this.bredaPhotoAdapter);
        this.bredaPhotoAdapter.notifyDataSetChanged();
        photoListView.setOnItemClickListener(new BredaPhotoOnClickListener(getApplicationContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void retrievePhotos(Context context, String apiBaseUrl) {
        RetrievePhotoAsync retriever = new RetrievePhotoAsync(context, this, apiBaseUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
this.bredaPhotos.clear();
this.bredaPhotoAdapter.notifyDataSetChanged();
this.retrievePhotos(this.getApplicationContext(), this.apiBaseUrl);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onNewPhotoListener(BredaPhoto bredaPhoto) {
        this.bredaPhotos.add(bredaPhoto);
        Log.i("Foto toegevoegd", "Foto");
        this.bredaPhotoAdapter.notifyDataSetChanged();
    }
}
