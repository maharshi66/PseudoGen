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

/**
 * the simple implementation of an EditText where each line is numbered on the left
 */
public class LineNumberedEditText extends androidx.appcompat.widget.AppCompatEditText {

    /** whether to set the lines visible or not	*/
    private boolean lineNumberVisible = true;
    private Rect _rect;
    private Paint lpaint;

    /** the gap between the line number and the left margin of the text */
    private int lineNumberMarginGap = 2;

    /**
     *the difference between line text size and the normal text size.
     * line text size is preferabl smaller than the normal text size
     */
    protected int LINE_NUMBER_TEXTSIZE_GAP = 2;

    public LineNumberedEditText (Context context, AttributeSet attrs) {
        super(context, attrs);
        _rect = new Rect();
        lpaint = new Paint();
        lpaint.setAntiAlias(true);
        lpaint.setStyle(Paint.Style.FILL);
        lpaint.setColor(Color.BLACK);
        lpaint.setTextSize(getTextSize() - LINE_NUMBER_TEXTSIZE_GAP);
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

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: Implement this method
        if (lineNumberVisible) {

            //set the size in case it changed after the last update
            lpaint.setTextSize(getTextSize() - 1);
            setLineNumberMarginGap(5);
            setLineNumberTextColor(R.color.black);

            int baseLine = getBaseline();
            String t = "";
            for (int i = 0; i < getLineCount(); i++) {
                t = " " + (i + 1) + ". ";
                canvas.drawText(t, _rect.left, baseLine, lpaint);
                baseLine += getLineHeight();
            }

            // set padding again, adjusting only the left padding
            setPadding((int)lpaint.measureText(t) + lineNumberMarginGap, getPaddingTop(),
                    getPaddingRight(), getPaddingBottom());

        }
        super.onDraw(canvas);
    }
}
