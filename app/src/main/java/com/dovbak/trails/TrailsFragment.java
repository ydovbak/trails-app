package com.dovbak.trails;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dovbak.trails.models.TrailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TrailsFragment extends Fragment {

    public static final String TAG = "TRAILS_DATA_FRAGMENT";
    private Button addTrail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRef = db.collection("trails");
    private ArrayList<TrailModel> trails = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyTrailRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TrailsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TrailsFragment newInstance(int columnCount) {
        TrailsFragment fragment = new TrailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trails_list, container, false);

        // init button
        //addTrail = (Button)view.findViewById(R.id.btn_add_trail);

//        addTrail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        // show toast
        Toast.makeText(getContext(), ((MainActivity) getActivity()).getUserName() , Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.trails_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new MyTrailRecyclerViewAdapter(trails);
        recyclerView.setAdapter(adapter);


        // get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fetchTrailsFromDb(user.getUid());
    }

    // fetches data from the firestore
    private void fetchTrailsFromDb(String userId) {
        // all records where user id is equal to the user that just authorised
        dbRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                TrailModel trailModel = document.toObject(TrailModel.class);
                                trails.add(trailModel);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}