package com.example.javaqa.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.javaqa.R;
import com.example.javaqa.ui.activities.OpenConversationItem;
import com.example.javaqa.ui.adapters.ConversationAdapter;
import com.example.javaqa.models.PostData;
import com.example.javaqa.viewmodels.PostsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public class ConversationFragment extends Fragment {

  private View view;
  private RecyclerView recyclerView;
  private SearchView searchView;
  private AppCompatSpinner spinner;
  private SwipeRefreshLayout swipeRefreshLayout;

  private ConversationAdapter adapter;
  private RecyclerView.LayoutManager layoutManager;
  private List<PostData> posts = new ArrayList<>();;

  private DatabaseReference databaseReference;
  private DatabaseReference conversationsReference;
  private FirebaseAuth firebaseAuth;
  private FirebaseUser firebaseUser;
  private FirebaseStorage firebaseStorage;
  private String userId;

  private PostsViewModel mPostsViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_conversation,container,false);

    findAllViews();
    setUpAdapter();
    setUpViewModel();
    //initFirebase();
    setUpSpinner();
    //loadQuestions();
    setUpSwipeRefreshLayout();

    return view;
  }

  private void setUpViewModel() {
    mPostsViewModel = ViewModelProviders.of(this).get(PostsViewModel.class);
    mPostsViewModel.getAllPosts().observe(this, postData -> adapter.setPosts(postData.data));
  }

  private void initFirebase() {
    //Get to current user information in database;
    databaseReference = FirebaseDatabase.getInstance().getReference();
    conversationsReference = databaseReference.child("PostData");
    firebaseStorage = FirebaseStorage.getInstance();
  }

  private void findAllViews() {
    recyclerView = view.findViewById(R.id.public_conversation_place);
    searchView = view.findViewById(R.id.search_conversation);
    spinner = view.findViewById(R.id.drop_down_menu);
    swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
  }

  private void setUpSpinner(){
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        Objects.requireNonNull(getContext()),
        R.array.category,
        android.R.layout.simple_spinner_item);

    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
  }

  private void setUpAdapter() {
    layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
    ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
    adapter = new ConversationAdapter();

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);

    adapter.setOnItemClickListener(this::openConversationItem);
  }

  private void openConversationItem(int position) {
    Intent intent = new Intent(getActivity(), OpenConversationItem.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable("ItemData", posts.get(position));
    intent.putExtras(bundle);
    startActivity(intent);
  }

  private void setUpSwipeRefreshLayout(){
    swipeRefreshLayout.setOnRefreshListener(() -> {
      posts.clear();
      if(posts.isEmpty()) {
        //loadQuestions();
      }
      swipeRefreshLayout.setRefreshing(false);
    });
  }

  private void loadQuestions() {
    conversationsReference.addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        PostData postData = dataSnapshot.getValue(PostData.class);
        assert postData != null;
        postData.setKey(dataSnapshot.getKey());
        posts.add(postData);
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(posts.size() - 1);
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }



}