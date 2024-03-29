package com.example.javaqa.ui.holders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaqa.R;
import com.example.javaqa.ui.adapters.FriendMainListAdapter;

public class FriendItemHolder extends RecyclerView.ViewHolder {

  private TextView userName;
  private ImageView imageView;
  private ImageView switcher;

  public FriendItemHolder(@NonNull View itemView, final FriendMainListAdapter.OnItemClickListener listener) {
    super(itemView);

    this.userName = itemView.findViewById(R.id.friend_username);
    this.imageView = itemView.findViewById(R.id.friend_item_image);
    this.switcher = itemView.findViewById(R.id.forward_to_play_arrow);

    itemView.setOnClickListener(view -> {
      if(listener != null) {
        int position = getAdapterPosition();
        if(position != RecyclerView.NO_POSITION) {
          listener.onItemClick(position);
        }
      }
    });
  }

  public TextView getUserName(){
    return userName;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public ImageView getSwitcher() {
    return switcher;
  }

  public void setUserName(TextView userName) {
    this.userName = userName;
  }

  public void setImageView(Drawable picture) {
    this.imageView.setBackground(picture);
  }

  public void setSwitcher(ImageView switcher) {
    this.switcher = switcher;
  }

}
