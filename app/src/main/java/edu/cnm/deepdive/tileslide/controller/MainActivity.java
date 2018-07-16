package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private static int PUZZLE_SIZE = 4;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    tileGrid.setOnItemClickListener(this);
    createPuzzle();

    Button button = findViewById(R.id.button);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, NewPuzzle.class);
        startActivityForResult(intent, 7777);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK && requestCode == 7777) {
      ImageView imageView = (ImageView) findViewById(R.id.puzzle_image);
      Bitmap bmp;

      byte[] byteArray = data.getByteArrayExtra("image");
      bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

      frame = new Frame(PUZZLE_SIZE, new Random());
      adapter = new FrameAdapter(this, frame, bmp);
      tileGrid.setAdapter(adapter);
    }

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (frame.move(position / PUZZLE_SIZE, position % PUZZLE_SIZE)) {
      adapter.notifyDataSetChanged();
      if (frame.isSolved()) {
        Toast.makeText(this, "Solved!", Toast.LENGTH_LONG).show();
      }
    } else {
      Toast toast = Toast.makeText(this, "Not a valid move!", Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.CENTER, 0, 0);
      toast.show();
    }
  }

  private void createPuzzle() {
    frame = new Frame(PUZZLE_SIZE, new Random());
    adapter = new FrameAdapter(this, frame);
    tileGrid.setAdapter(adapter);
  }

}
