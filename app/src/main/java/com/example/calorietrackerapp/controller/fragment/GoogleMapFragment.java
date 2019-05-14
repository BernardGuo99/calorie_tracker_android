package com.example.calorietrackerapp.controller.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.calorietrackerapp.R;
import com.example.calorietrackerapp.controller.activity.MainActivity;
import com.example.calorietrackerapp.model.google_custom_search.NearbySearch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {


    private View vGoogleMap;
    private GoogleMap mGoogleMap;
    private MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vGoogleMap = inflater.inflate(R.layout.fragment_google_map, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Location");


        return vGoogleMap;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) vGoogleMap.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        String user_address = sharedPref.getString("user_address", "");
        LatLng coordinates = getCoordinates(user_address);


        mGoogleMap.addMarker(new MarkerOptions().position(coordinates).title("My Home").snippet(user_address)).showInfoWindow();
        CameraPosition liberty = CameraPosition.builder().target(coordinates).zoom(13).bearing(0).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));

        Circle circle = mGoogleMap.addCircle(new CircleOptions()
                .center(coordinates)
                .radius(5000)
                .strokeColor(Color.rgb(255,127,80))
                .fillColor(Color.parseColor("#2271cce7")));

        GetAllNearbyAsyncTask getAllNearbyAsyncTask = new GetAllNearbyAsyncTask();
        getAllNearbyAsyncTask.execute(String.valueOf(coordinates.latitude), String.valueOf(coordinates.longitude));


    }


    public LatLng getCoordinates(String address) {
        List<Address> addressList = null;
        Address realAddress = null;
        double latitude = 0;
        double longitude = 0;
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            addressList = geocoder.getFromLocationName(address, 1);
            realAddress = addressList.get(0);
            latitude = realAddress.getLatitude();
            longitude = realAddress.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LatLng(latitude, longitude);
    }

    private class GetAllNearbyAsyncTask extends AsyncTask<String, Void, List<String[]>> {


        @Override
        protected List<String[]> doInBackground(String... params) {

            return NearbySearch.getAllLocations(NearbySearch.search(params[0], params[1]));

        }

        @Override
        protected void onPostExecute(List<String[]> list) {
            for (String[] park : list) {
                double latitude = Double.parseDouble(park[0]);
                double langitude = Double.parseDouble(park[1]);

                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.placeholder);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, langitude)).title(park[2]).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
            }
        }
    }


}
