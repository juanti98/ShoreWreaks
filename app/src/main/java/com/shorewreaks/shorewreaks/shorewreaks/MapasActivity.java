package com.shorewreaks.shorewreaks.shorewreaks;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MapasActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 3, mLocationListener);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                String lat, lon;
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void cargarPuntos() {
        firebaseDatos fb = new firebaseDatos();
        fb.getPuntos(new PlayasCallback() {
            @Override
            public void onPlayasLoaded(ArrayList<RankingPlayas> pts) {
                if (pts.size() > 1){
                    for (int i = 0; i < pts.size(); i++){
                        LatLng punto = new LatLng(Double.parseDouble(pts.get(i).getLat()), Double.parseDouble(pts.get(i).getLon()));
                        mMap.addMarker(new MarkerOptions().position(punto).title(pts.get(i).getNombre()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
                    }
                }
            }
        });

    }

    private void anadirPunto(final double latitud, final double longitud) {
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Añadir Punto");
        constructor.setMessage("Añade un Punto");

        LayoutInflater inflador = LayoutInflater.from(MapasActivity.this);
        View vista = inflador.inflate(R.layout.alert_mapa, null);

        constructor.setView(vista);
        final TextView tvLatitud, tvLongitud;
        final EditText etNombrePlaya, etDireccion, etLocalidad, etProvincia;

        tvLatitud = vista.findViewById(R.id.tvLatitud);
        tvLongitud = vista.findViewById(R.id.tvLongitud);
        etNombrePlaya = vista.findViewById(R.id.etNombrePlaya);
        etDireccion = vista.findViewById(R.id.etDireccion);
        etLocalidad = vista.findViewById(R.id.etLocalidad);
        etProvincia = vista.findViewById(R.id.etProvincia);

        tvLatitud.setText(Double.toString(latitud));
        tvLongitud.setText(Double.toString(longitud));

        constructor.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String lat, lon, nombre, direccion, localidad, provincia;
                RankingPlayas playas;
                nombre = etNombrePlaya.getText().toString();
                direccion = etDireccion.getText().toString();
                localidad = etLocalidad.getText().toString();
                provincia = etProvincia.getText().toString();
                lat = Double.toString(latitud);
                lon = Double.toString(longitud);
                playas = new RankingPlayas(nombre, direccion, localidad, provincia, lat, lon, 0);
                mDatabase.child("playas").push().setValue(playas);
                cargarPuntos();
                setResult(RESULT_OK);
                finish();
            }
        });
        constructor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("CancelarBTN", "Se ha cancelado");
            }
        });
        AlertDialog alert = constructor.create();
        alert.show();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        cargarPuntos();

        GoogleMap.OnMapClickListener oyente = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                anadirPunto(latLng.latitude, latLng.longitude);
            }
        };

        mMap.setOnMapClickListener(oyente);
    }
}
