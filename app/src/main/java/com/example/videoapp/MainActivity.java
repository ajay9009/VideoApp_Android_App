package com.example.videoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    RecyclerView MrecyclerView;
    FirebaseDatabase database;
    DatabaseReference reference;
    String name,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MrecyclerView = findViewById(R.id.recyclerview_video);
        MrecyclerView.setHasFixedSize(true);
        MrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("video");
    }

    private void firebaseSearch(String searchtext){
        String query = searchtext.toLowerCase();
        Query firebaseQuery = reference.orderByChild("search").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<member> options =
                new FirebaseRecyclerOptions.Builder<member>()
                        .setQuery(firebaseQuery, member.class)
                        .build();

        FirebaseRecyclerAdapter<member, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull member model) {
                        holder.setExoPlayer(getApplication(),model.getTitle(),model.getUrl());

                        holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getTitle();
                                url = getItem(position).getUrl();
                                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                                intent.putExtra("nam",name);
                                intent.putExtra("ur",url);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row, parent,false);

                        return new ViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        MrecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<member> options =
                new FirebaseRecyclerOptions.Builder<member>()
                        .setQuery(reference, member.class)
                        .build();

        FirebaseRecyclerAdapter<member, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<member, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull member model) {
                        holder.setExoPlayer(getApplication(),model.getTitle(),model.getUrl());

                        holder.setOnClicklistener(new ViewHolder.Clicklistener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                name = getItem(position).getTitle();
                                url = getItem(position).getUrl();
                                Intent intent = new Intent(MainActivity.this,Fullscreen.class);
                                intent.putExtra("nam",name);
                                intent.putExtra("ur",url);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.row, parent,false);

                        return new ViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        MrecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}