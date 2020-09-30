package com.example.majesticmasonry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyLogActivity extends AppCompatActivity {

    public TextView DayBox,Datebox,JobNameBox,BrickLayers,BrickTenders,Other,Total,SigText;
    public ImageView SignedArea;
    public String Day,fullDate,jobName;
    public Button DoneButton;
    public DatabaseReference baseRef, jobNameRef;
    public Integer numOfBrickLayers,numOfBrickTenders,numOfOthers,finalTotal;
    public Bitmap savedDiagram,wholePage;
    public byte[] signatureFinal, WholePage;
    public StorageReference ssTestRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_log);

        //Define Boxes and Buttons
        DayBox = (TextView) findViewById(R.id.dayBox);
        Datebox = (TextView) findViewById(R.id.dateBox);
        JobNameBox = (TextView) findViewById(R.id.jobNameBox);
        BrickLayers = (TextView) findViewById(R.id.brickLayer);
        BrickTenders = (TextView) findViewById(R.id.brickTender);
        Other = (TextView) findViewById(R.id.other);
        Total = (TextView) findViewById(R.id.total);
        SignedArea = (ImageView) findViewById(R.id.signedArea);
        DoneButton = (Button) findViewById(R.id.doneButton);
        SigText = (TextView) findViewById(R.id.signText);
        //End Definitions


        GenerateFields();



    }

    public void GenerateFields(){
        baseRef = FirebaseDatabase.getInstance().getReference().child("Projects").child("Majestic Masonry");
        jobNameRef = baseRef.child("ProjectName");
        //Generates Day
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        Day = sdf.format(d);
        DayBox.setText(Day);
        // End of Day

        //Generate full Date
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
        Date d2 = new Date();
        fullDate = sdf2.format(d2);
        Datebox.setText(fullDate);
        //End of Date


        //Get Job Name
        jobNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobName = dataSnapshot.getValue(String.class);
                JobNameBox.setText(jobName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmployees();
            }
        });

        SignedArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign();
            }
        });

        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ScreenShot();

            }
        });
    }

    public void AddEmployees(){

        numOfBrickLayers = Integer.valueOf(BrickLayers.getText().toString());
        numOfBrickTenders = Integer.valueOf(BrickTenders.getText().toString());
        numOfOthers = Integer.valueOf(Other.getText().toString());
        finalTotal = numOfBrickLayers + numOfBrickTenders + numOfOthers;
        Log.d("test",finalTotal.toString());
        Total.setText(String.valueOf(finalTotal));
    }

    public void Sign(){

        final MyDrawView myDrawView = new MyDrawView(DailyLogActivity.this);
        if (myDrawView.getParent() != null) {
            ((ViewGroup) myDrawView.getParent()).removeView(myDrawView);
        }
        final AlertDialog.Builder diagramDialog = new AlertDialog.Builder(DailyLogActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.signview, null);
        final ConstraintLayout signLayout = (ConstraintLayout) dialogView.findViewById(R.id.signLayout2);
        final AlertDialog SignDia = diagramDialog.create();
        Button DoneButton = (Button) dialogView.findViewById(R.id.doneSignBtn);
        Button ClearButton = (Button) dialogView.findViewById(R.id.clearButton2);
        SignDia.setView(dialogView);
        signLayout.addView(myDrawView);
        ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyDrawView) myDrawView).clear();
            }
        });
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Signature Photo
                signLayout.setDrawingCacheEnabled(true);
                savedDiagram = Bitmap.createBitmap(signLayout.getDrawingCache());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                savedDiagram.compress(Bitmap.CompressFormat.PNG, 100, stream);

                signatureFinal = stream.toByteArray();
                SigText.setText("");
                SignedArea.setImageBitmap(savedDiagram);
                SignDia.dismiss();

            }
        });

        SignDia.show();


    }

    public class MyDrawView extends View {

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private Paint mPaint, mPaintE;

        public MyDrawView(Context c) {
            super(c);

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFF000000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(3);
            /////////////////////////////
            mPaintE = new Paint();
            mPaintE.setAntiAlias(true);
            mPaintE.setDither(true);
            mPaintE.setColor(Color.TRANSPARENT);
            mPaintE.setStyle(Paint.Style.STROKE);
            mPaintE.setStrokeJoin(Paint.Join.ROUND);
            mPaintE.setStrokeCap(Paint.Cap.ROUND);
            mPaintE.setStrokeWidth(3);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void clear() {
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            System.gc();
        }
    }

    public void ScreenShot(){

        ssTestRef = FirebaseStorage.getInstance().getReference().child(jobName).child("DailyLog");
        View FormLayout =  findViewById(R.id.fullPage);
        FormLayout.setDrawingCacheEnabled(true);
        FormLayout.getDrawingCache();
        FormLayout.buildDrawingCache();
        //WholePage (capitalized) is the official image created
        wholePage = Bitmap.createBitmap(FormLayout.getDrawingCache());
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        wholePage.compress(Bitmap.CompressFormat.PNG,100,stream2);
        WholePage = stream2.toByteArray();
        ssTestRef.putBytes(WholePage);

        Intent returnIntent = new Intent(DailyLogActivity.this,ProjectDisplay.class);
        startActivity(returnIntent);
    }
}
