package com.example.chatapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.SearchUserRecyclerAdapter;
import com.example.chatapp.Model.UserModel;
import com.example.chatapp.Utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        searchInput = findViewById(R.id.seach_username_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);
        // Başlangıçta arama alanına odaklan
        searchInput.requestFocus();

        // Geri butonuna basıldığında önceki aktiviteye dön
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
// Arama butonu işlevselliği
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            // Geçersiz kullanıcı adı
            if(searchTerm.isEmpty() || searchTerm.length()<3){
                searchInput.setError("Geçersiz kullanıcı adı");
                return;
            }
            // Arama terimine göre kullanıcıları sorgulayarak RecyclerView'i kurar
            setupSearchRecyclerView(searchTerm);
        });
    }

    // Arama terimine göre kullanıcıları sorgulayarak RecyclerView'i kurar
    void setupSearchRecyclerView(String searchTerm){

        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username",searchTerm)
                .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff');
// Firestore sorgusu sonuçlarıyla RecyclerView'i ayarla
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query,UserModel.class).build();
// SearchUserRecyclerAdapter için bir adaptör oluşturuluyor
        adapter = new SearchUserRecyclerAdapter(options,getApplicationContext());
        // recyclerView için bir düzen yöneticisi ayarlanıyor
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView için adaptör ayarlanıyor
        recyclerView.setAdapter(adapter);
        // RecyclerView'i başlat
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        // Aktivite başladığında adapter dinlemeye başlar
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        // Aktivite durduğunda adapter dinlemeyi durdurur

        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        // Aktivite tekrar başladığında adapter dinlemeye başlar

        super.onResume();
        if(adapter!=null)
            adapter.startListening();
    }
}
