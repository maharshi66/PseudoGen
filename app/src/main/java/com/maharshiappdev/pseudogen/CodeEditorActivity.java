package com.maharshiappdev.pseudogen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.widget.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class CodeEditorActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Discard draft")
                .setCancelable(true)
                .setMessage("Do you want to discard this draft?")
                .setPositiveButton("Yes, discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CodeEditorActivity.this, CodeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No, continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_editor);
        Intent intent = getIntent();
        String title = intent.getStringExtra("pseudocodeTitle");
        setTitle(title);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_code)
                {
                    Intent intent = new Intent(CodeEditorActivity.this, CodeActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    class LineNumberedEditText extends androidx.appcompat.widget.AppCompatEditText {

         boolean lineNumberVisible = true;
         Rect rect;
         Paint lpaint;
         int lineNumberMarginGap = 2;
         int LINE_NUMBER_TEXTSIZE_GAP = 2;

        public LineNumberedEditText(Context context, AttributeSet attributeSet)
        {
            super(context,attributeSet);
            rect = new Rect();
            lpaint = new Paint();
            lpaint.setAntiAlias(true);
            lpaint.setStyle(Paint.Style.FILL);
            lpaint.setColor(Color.BLACK);
            lpaint.setTextSize(getTextSize() - LINE_NUMBER_TEXTSIZE_GAP);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (lineNumberVisible) {

                //set the size in case it changed after the last update
                lpaint.setTextSize(getTextSize() - LINE_NUMBER_TEXTSIZE_GAP);

                int baseLine = getBaseline();
                String t = "";
                for (int i = 0; i < getLineCount(); i++) {
                    t = "" + (i + 1);
                    canvas.drawText(t, rect.left, baseLine, lpaint);
                    baseLine += getLineHeight();
                }

                // set padding again, adjusting only the left padding
                setPadding((int)lpaint.measureText(t) + lineNumberMarginGap, getPaddingTop(),
                        getPaddingRight(), getPaddingBottom());
            }
            super.onDraw(canvas);
        }

        public void setLineNumberMarginGap(int lineNumberMarginGap) {
            this.lineNumberMarginGap = lineNumberMarginGap;
        }

        public int getLineNumberMarginGap() {
            return lineNumberMarginGap;
        }

        public void setLineNumberVisible(boolean lineNumberVisible) {
            this.lineNumberVisible = lineNumberVisible;
        }

        public boolean isLineNumberVisible() {
            return lineNumberVisible;
        }

        public void setLineNumberTextColor(int textColor) {
            lpaint.setColor(textColor);
        }

        public int getLineNumberTextColor() {
            return	lpaint.getColor();
        }

    }
}