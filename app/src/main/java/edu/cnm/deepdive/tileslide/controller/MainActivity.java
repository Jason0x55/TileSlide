package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private static int PUZZLE_SIZE = 4;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private Button newPuzzle;
  private Button resetPuzzle;
  private TextView moveCount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    tileGrid.setOnItemClickListener(this);
    createPuzzle();

    moveCount = findViewById(R.id.move_count);
    resetPuzzle = findViewById(R.id.reset_button);
    resetPuzzle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        adapter.notifyDataSetChanged();
        moveCount.setText(String.valueOf(frame.getMoves()));
      }
    });
    newPuzzle = findViewById(R.id.new_puzzle_button);
    newPuzzle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(MainActivity.this, NewPuzzle.class);
        Bitmap bitmap = adapter.getBitmap();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        byte[] byteArray = byteStream.toByteArray();
        intent.putExtra("image", byteArray);
        startActivityForResult(intent, 7777);
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (frame.move(position / PUZZLE_SIZE, position % PUZZLE_SIZE)) {
      adapter.notifyDataSetChanged();
      moveCount.setText(String.valueOf(frame.getMoves()));
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK && requestCode == 7777) {
      ImageView imageView = (ImageView) findViewById(R.id.puzzle_image);
      Bitmap bmp;

      byte[] byteArray = data.getByteArrayExtra("image");
      bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
      PUZZLE_SIZE = data.getIntExtra("size", 3);

      tileGrid.setNumColumns(PUZZLE_SIZE);
      tileGrid.setOnItemClickListener(this);
      frame = new Frame(PUZZLE_SIZE, new Random());
      adapter = new FrameAdapter(this, frame, bmp);
      tileGrid.setAdapter(adapter);
    }

  }

}
