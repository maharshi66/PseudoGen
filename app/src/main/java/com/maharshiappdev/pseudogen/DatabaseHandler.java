package com.maharshiappdev.pseudogen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "posts_manager";
    private static final String TABLE_NAME = "posts";

    // Coloumn Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DES = "description";
    private static final String KEY_PSEUDOCODE = "pseudocode";
    private static final String KEY_INPUT = "input";
    private static final String KEY_OUTPUT = "output";
    private static final String KEY_TIME = "time";
    private static final String KEY_SPACE = "space";

    //Column Combinations
    private static final String[] COLS_ID_TITLE_POSTS = new String[] {KEY_ID,KEY_TITLE,KEY_DES, KEY_PSEUDOCODE, KEY_INPUT, KEY_OUTPUT, KEY_TIME, KEY_SPACE};

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL query
        String CREATE_POSTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_TITLE + " TEXT NOT NULL,"
                        + KEY_DES + " TEXT,"
                        + KEY_PSEUDOCODE + " TEXT,"
                        + KEY_INPUT + " TEXT,"
                        + KEY_OUTPUT + " TEXT,"
                        + KEY_TIME + " TEXT,"
                        + KEY_SPACE + " TEXT"
                        + ")";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        Log.d(TAG,DROP_TABLE);

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    //CRUD Operations
    public void addPseudocodePost(Posts post)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, post.getTitle());
        values.put(KEY_DES, post.getDescription());
        values.put(KEY_PSEUDOCODE, post.getPseudocode());
        values.put(KEY_INPUT, post.getInput());
        values.put(KEY_OUTPUT, post.getOutput());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Posts getPost(String title){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME, COLS_ID_TITLE_POSTS,KEY_TITLE +"=?",new String[]{title},null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        db.close();

//        Log.d(TAG,"Get Post Result "+ c.getString(0)+","+c.getString(1)+","+c.getString(2));
        Posts post = new Posts(c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4),
                c.getString(5));
        return post;
    }

    public List<Posts> getAllPosts()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Posts> postsList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, COLS_ID_TITLE_POSTS, null, null, null, null, null);

        if(cursor != null && cursor.moveToFirst())
        {
            do{
                Posts post = new Posts();
                post.setTitle(cursor.getString(1));
                post.setDescription(cursor.getString(2));
                post.setPseudocode(cursor.getString(3));
                post.setInput(cursor.getString(4));
                post.setOutput(cursor.getString(5));
                postsList.add(post);
            }while(cursor.moveToNext());
        }

        db.close();
        return postsList;
    }

    public void deletePost(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + KEY_TITLE + "=\"" + title + "\";");
        db.close();
    }

    public boolean isDuplicateTitle(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLS_ID_TITLE_POSTS, KEY_TITLE + "=?", new String[] { title }, null, null, null);

        if(cursor.moveToFirst())
        {
            return true;
        }else
        {
            return false;
        }
    }

    public void overWritePost(String title, String description, String pseudocode, String input, String output)
    {
        //Delete post
        //Make a fresh entry with new title, description, input, output
        deletePost(title);
        Posts p = new Posts();
        p.setTitle(title);
        p.setDescription(description);
        p.setPseudocode(pseudocode);
        p.setInput(input);
        p.setOutput(output);
        addPseudocodePost(p);
    }
}
