package com.dovbak.trails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dovbak.trails.models.TrailModel;
import com.dovbak.trails.models.TrailPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TrailViewFragment extends Fragment {

    private TrailModel trail;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            ArrayList<TrailPoint> points = trail.getPoints();

            LatLng center;
            // if we don't have any points, just show center of the map
            if (points.size() == 0) {
                center = new LatLng(0, 0);
            } else {
                // otherwise get first point in the list
                center = points.get(0).toLatLng();
            }

            // add marker for every point in the list
            for (TrailPoint point : points) {
                MarkerOptions marker = new MarkerOptions()
                        .position(point.toLatLng())
                        .title(point.getName());
                googleMap.addMarker(marker);
            }

            // add bounds to display

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 16));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trail_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // read the trail object from bundle arguments passed to this fragment
        this.trail = (TrailModel) getArguments().getSerializable("trail");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}