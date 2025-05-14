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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PronosticoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PronosticoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    PronosticoAdapter adapter;
    EditText edtLocationId, edtDays;
    Button btnBuscar;


    public PronosticoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PronosticoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PronosticoFragment newInstance(String param1, String param2) {
        PronosticoFragment fragment = new PronosticoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String locationIdArg = null;
        int diasArg = -1;

        if (getArguments() != null) {
            locationIdArg = getArguments().getString("location_id");
            diasArg = getArguments().getInt("dias", -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pronostico, container, false);

        edtLocationId = view.findViewById(R.id.edtLocationId);
        edtDays = view.findViewById(R.id.edtDays);
        btnBuscar = view.findViewById(R.id.btnBuscarPronostico);
        recyclerView = view.findViewById(R.id.rvPronosticos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PronosticoAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        btnBuscar.setOnClickListener(v -> {
            String locationId = edtLocationId.getText().toString().trim();
            int days = Integer.parseInt(edtDays.getText().toString().trim());
            buscarPronostico(locationId, days);
        });

        if (getArguments() != null) {
            String locationIdArg = getArguments().getString("location_id");
            int diasArg = getArguments().getInt("dias", -1);

            if (locationIdArg != null && diasArg > 0) {
                edtLocationId.setText(locationIdArg);
                edtDays.setText(String.valueOf(diasArg));
                buscarPronostico(locationIdArg, diasArg);
            }
        }

        return view;
    }



    private void buscarPronostico(String locationId, int days) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService service = retrofit.create(WeatherApiService.class);
        Call<ForecastResponse> call = service.getForecast("ec24b1c6dd8a4d528c1205500250305", "id:" + locationId, days);

        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastDay> forecastList = response.body().forecast.forecastday;
                    adapter.updateData(forecastList);
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}