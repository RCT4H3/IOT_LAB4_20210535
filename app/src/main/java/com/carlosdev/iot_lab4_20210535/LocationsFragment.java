package com.carlosdev.iot_lab4_20210535;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LocationsAdapter locationAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationsFragment newInstance(String param1, String param2) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        EditText editBusqueda = view.findViewById(R.id.editBusqueda);
        Button btnBuscar = view.findViewById(R.id.btnBuscar);

        RecyclerView recycler = view.findViewById(R.id.recyclerResultados);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        locationAdapter = new LocationsAdapter(getContext(), new ArrayList<>(), location -> {
            Bundle args = new Bundle();
            args.putString("location_id", String.valueOf(location.getId()));
            args.putInt("dias", 14);

            PronosticoFragment fragment = new PronosticoFragment();
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentUwU, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recycler.setAdapter(locationAdapter);

        btnBuscar.setOnClickListener(v -> {
            String textoBusqueda = editBusqueda.getText().toString();
            if (!textoBusqueda.isEmpty()) {
                obtenerUbicaciones(textoBusqueda);
            } else {
                Log.d("msg-test", "El campo de búsqueda está vacío.");
            }
        });

        return view;
    }

    private void obtenerUbicaciones(String textoBusqueda) {
        LocationRepo locationRepo = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocationRepo.class);

        locationRepo.buscarUbicaciones("ec24b1c6dd8a4d528c1205500250305", textoBusqueda)
                .enqueue(new Callback<List<Location>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Location>> call,
                                           @NonNull Response<List<Location>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Location> resultados = response.body();
                            Log.d("msg-test", "Ubicaciones encontradas: " + resultados.size());
                            locationAdapter.actualizarLista(resultados);
                        } else {
                            Log.d("msg-test", "Error en respuesta de la API");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Location>> call, @NonNull Throwable t) {
                        Log.e("msg-test", "Fallo en la llamada a la API", t);
                    }
                });
    }


}