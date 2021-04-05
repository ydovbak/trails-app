package com.dovbak.trails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.dovbak.trails.models.TrailModel;
import com.dovbak.trails.models.TrailPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CreateTrailFragment extends Fragment {

    public static final String TAG = "NEW_TRAILS_FRAGMENT";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference trailsColRef = db.collection("trails");
    private ArrayList<Marker> markers = new ArrayList<>();
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

            // we want to add markers on long click
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    // we want to prompt user for the name of the location
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("New Marker Name");
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pointName = input.getText().toString();
                            // add new marker at given lat long with a name
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .draggable(true)
                                    .title(pointName);
                            // add marker to the map
                            Marker marker = googleMap.addMarker(markerOptions);
                            markers.add(marker);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_trail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        EditText trailNameTxtView = view.findViewById(R.id.new_trail_name);
        EditText trailDescTxtView = view.findViewById(R.id.new_trail_desc);

        // when user presses save, we want to
        FloatingActionButton saveBtn = (FloatingActionButton) view.findViewById(R.id.btn_save_trail);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trailName = trailNameTxtView.getText().toString();
                String trailDesc = trailDescTxtView.getText().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // create trail
                TrailModel newTrail = new TrailModel();
                newTrail.setName(trailName);
                newTrail.setDescription(trailDesc);
                newTrail.setUserId(userId);

                // add all markers to our points
                ArrayList<TrailPoint> points = new ArrayList<>();
                for(Marker marker: markers) {
                    LatLng latlng = marker.getPosition();

                    TrailPoint point = new TrailPoint();

                    point.setName(marker.getTitle());
                    point.setLat(String.valueOf(latlng.latitude));
                    point.setLng(String.valueOf(latlng.longitude));

                    points.add(point);
                }
                newTrail.setPoints(points);

                Toast.makeText(getContext(), "Saving...", Toast.LENGTH_LONG).show();

                // add it to firestore
                trailsColRef.add(newTrail).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()) {
                            // navigate back to list of trails
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                    .navigate(R.id.action_createTrailFragment_to_trailsFragment);
                        } else {
                            Toast.makeText(getContext(), "Failed to load create trail", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error creating trail: ", task.getException());
                        }
                    }
                });
            }
        });
    }
}