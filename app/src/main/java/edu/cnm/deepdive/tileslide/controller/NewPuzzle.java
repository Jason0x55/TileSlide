package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import edu.cnm.deepdive.tileslide.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NewPuzzle extends AppCompatActivity {

  private Intent intent;
  private ImageView imageView;
  private Button imageButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_puzzle);

    setupUI();

  }

  private void setupUI() {
    imageView = (ImageView) findViewById(R.id.puzzle_image);
    imageButton = findViewById(R.id.select_image_button);
    intent = getIntent();
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.android_robot_circle);
    imageView.setImageDrawable(drawable);
    imageButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Bitmap bitmap = null;
    if(resultCode == RESULT_CANCELED) {
      Drawable drawable = ContextCompat.getDrawable(this, R.drawable.reddit);
      bitmap = ((BitmapDrawable) drawable).getBitmap();
    }
    if(resultCode == RESULT_OK) {
      Uri imagePath = data.getData();
      try {
        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
        imageView.setImageBitmap(
            MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath));

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
      byte[] byteArray = byteStream.toByteArray();
      intent.putExtra("image", byteArray);
      setResult(RESULT_OK, intent);
  }

}
