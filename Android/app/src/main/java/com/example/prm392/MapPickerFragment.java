package com.example.prm392;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapPickerFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker currentMarker;
    private FloatingActionButton btnPick;
    private LatLng pickedLatLng;
    private String pickedAddress;
    private OnLocationPickedListener callback;

    public interface OnLocationPickedListener {
        void onLocationPicked(LatLng latLng, String address);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationPickedListener) {
            callback = (OnLocationPickedListener) context;
        } else if (getParentFragment() instanceof OnLocationPickedListener) {
            callback = (OnLocationPickedListener) getParentFragment();
        } else {
            throw new RuntimeException("Parent must implement OnLocationPickedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_picker, container, false);
        btnPick = view.findViewById(R.id.btn_pick_location);
        btnPick.setOnClickListener(v -> {
            if (pickedLatLng != null && pickedAddress != null) {
                callback.onLocationPicked(pickedLatLng, pickedAddress);
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn vị trí trên bản đồ", Toast.LENGTH_SHORT).show();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLatLng = new LatLng(21.0285, 105.8542); // Hà Nội
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 15));
        mMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) currentMarker.remove();
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Vị trí đã chọn"));
            pickedLatLng = latLng;
            pickedAddress = getAddressFromLatLng(latLng);
            if (pickedAddress != null) {
                Toast.makeText(getContext(), pickedAddress, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
} 