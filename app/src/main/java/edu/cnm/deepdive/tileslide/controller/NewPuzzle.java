package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import edu.cnm.deepdive.tileslide.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NewPuzzle extends AppCompatActivity {

  private Intent intent;
  private ImageView newImage;
  private ImageView currentImage;
  private Button imageButton;
  private Button createPuzzleButton;
  private SeekBar puzzleSlider;

  private boolean newImageSet = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_puzzle);

    setupUI();

  }

  private void setupUI() {
    newImage = (ImageView) findViewById(R.id.puzzle_image);
    imageButton = findViewById(R.id.select_image_button);
    currentImage = findViewById(R.id.current_image);
    puzzleSlider = findViewById(R.id.puzzle_size_slider);
    createPuzzleButton = findViewById(R.id.create_puzzle_button);
    intent = getIntent();

    setupImages();
    imageButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), 1);
      }
    });

    createPuzzleButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        intent.putExtra("size", puzzleSlider.getProgress() + 2);
        if (!newImageSet) {
          Drawable drawable = ContextCompat.getDrawable(NewPuzzle.this, R.drawable.android_robot_circle);
          Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
          setImage(bitmap);
        }
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  private void setupImages() {
    Bitmap bitmap;
    byte[] byteArray = intent.getByteArrayExtra("image");
    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.android_robot_circle);
    newImage.setImageDrawable(drawable);
    currentImage.setImageBitmap(bitmap);
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
        newImage.setImageBitmap(
            MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath));
        newImageSet = true;

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    setImage(bitmap);
  }

  private void setImage(Bitmap bitmap) {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
    byte[] byteArray = byteStream.toByteArray();
    intent.putExtra("image", byteArray);
  }

}
