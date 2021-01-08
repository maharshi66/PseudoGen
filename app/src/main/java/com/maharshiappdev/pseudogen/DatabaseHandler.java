package com.maharshiappdev.pseudogen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "posts_manager";
    private static final String TABLE_NAME = "exampleposts";
    private static final String TABLE_NAME_CHALLENGES = "examplechallenges";

    public String getNEW_POSTS_TABLE () {
        return NEW_POSTS_TABLE;
    }

    public void setNEW_POSTS_TABLE ( String NEW_POSTS_TABLE ) {
        this.NEW_POSTS_TABLE = NEW_POSTS_TABLE;
    }

    private String NEW_POSTS_TABLE;
    private String NEW_CHALLENGES_TABLE;
    // Coloumn Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DES = "description";
    private static final String KEY_PSEUDOCODE = "pseudocode";
    private static final String KEY_INPUT = "input";
    private static final String KEY_OUTPUT = "output";
    private static final String KEY_COMPLETE = "complete";
    //Column Combinations
    private static final String[] COLS_ID_TITLE_POSTS = new String[] {KEY_ID,KEY_TITLE,KEY_DES, KEY_PSEUDOCODE, KEY_INPUT, KEY_OUTPUT};
    private static final String[] COLS_ID_TITLE_CHALLENGES = new String[] {KEY_ID,KEY_TITLE, KEY_DES, KEY_INPUT, KEY_OUTPUT, KEY_COMPLETE};

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.insert(NEW_POSTS_TABLE, null, values);
        db.close();
    }

    public Posts getPost(String title){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(NEW_POSTS_TABLE, COLS_ID_TITLE_POSTS,KEY_TITLE +"=?",new String[]{title},null,null,null,null);
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
        Cursor cursor = db.query(NEW_POSTS_TABLE, COLS_ID_TITLE_POSTS, null, null, null, null, null);

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
        db.execSQL("DELETE FROM " + NEW_POSTS_TABLE + " WHERE " + KEY_TITLE + "=\"" + title + "\";");
        db.close();
    }

    public void deleteAllPosts()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NEW_POSTS_TABLE, null, null);
    }

    public boolean isDuplicateTitle(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NEW_POSTS_TABLE, COLS_ID_TITLE_POSTS, KEY_TITLE + "=?", new String[] { title }, null, null, null);

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

    public void overWritePostWithNewTitle(String oldTitle,String newTitle, String description, String pseudocode, String input, String output)
    {
        deletePost(oldTitle);
        Posts p = new Posts();
        p.setTitle(newTitle);
        p.setDescription(description);
        p.setPseudocode(pseudocode);
        p.setInput(input);
        p.setOutput(output);
        addPseudocodePost(p);
    }

    public void addChallenge(Posts post)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, post.getTitle());
        values.put(KEY_DES, post.getDescription());
        values.put(KEY_INPUT, post.getInput());
        values.put(KEY_OUTPUT, post.getOutput());
        values.put(KEY_COMPLETE, 0);
        db.insert(TABLE_NAME_CHALLENGES, null, values);
        db.close();
    }

    public void markChallengeComplete(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMPLETE, 1);
        db.update(TABLE_NAME_CHALLENGES, cv, KEY_TITLE + "= ?", new String[]{title});
        db.close ();
    }

    public void markChallengeIncomplete(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMPLETE, 0);
        db.update(TABLE_NAME_CHALLENGES, cv, KEY_TITLE + "= ?", new String[]{title});
        db.close ();
    }

    public int getCheckedState(String title)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        int state = 0;
        Cursor c = db.query(TABLE_NAME_CHALLENGES, COLS_ID_TITLE_CHALLENGES,KEY_TITLE +"=?",new String[]{title},null,null,null,null);
        if(c.moveToFirst ()){
            state = c.getInt ( 5 );
        }
        c.close ();
        return state;
    }

    public void createPostTable ( String tableName)
    {
        SQLiteDatabase db = getWritableDatabase ();
        NEW_POSTS_TABLE = formatTableNameStringForPosts ( tableName );
        String CREATE_POSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + NEW_POSTS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT NOT NULL,"
                + KEY_DES + " TEXT,"
                + KEY_PSEUDOCODE + " TEXT,"
                + KEY_INPUT + " TEXT,"
                + KEY_OUTPUT + " TEXT"
                + ")";

        db.execSQL (CREATE_POSTS_TABLE);
        db.close ();
    }

    public void createChallengeTable ( String tableName)
    {
        SQLiteDatabase db = getWritableDatabase ();
        String tName = formatTableNameStringForChallenges ( tableName );
        String CREATE_CHALLENGES_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + tName  + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT NOT NULL,"
                + KEY_DES + " TEXT,"
                + KEY_INPUT + " TEXT,"
                + KEY_OUTPUT + " TEXT,"
                + KEY_COMPLETE + " INTEGER"
                + ")";

        db.execSQL (CREATE_CHALLENGES_TABLE_USER);
        db.close ();
    }

    public void addChallenge(Posts post, String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        tableName = formatTableNameStringForChallenges ( tableName );
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, post.getTitle());
        values.put(KEY_DES, post.getDescription());
        values.put(KEY_INPUT, post.getInput());
        values.put(KEY_OUTPUT, post.getOutput());
        values.put(KEY_COMPLETE, 0);
        db.insert(tableName, null, values);
        db.close();
    }

    public void markChallengeComplete(String title, String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMPLETE, 1);
        tableName = formatTableNameStringForChallenges ( tableName );
        db.update(tableName, cv, KEY_TITLE + "= ?", new String[]{title});
        db.close ();
    }

    public void markChallengeIncomplete(String title, String tableName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        tableName = formatTableNameStringForChallenges ( tableName );
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMPLETE, 0);
        db.update(tableName, cv, KEY_TITLE + "= ?", new String[]{title});
        db.close ();
    }

    public String formatTableNameStringForChallenges ( String tableName)
    {
        if(tableName.equals ( "challenges" ))
        {
            return tableName;
        }
        tableName.toLowerCase ();
        tableName.replaceAll("[0-9]","");
        tableName = "[" +tableName + "]";
        return tableName;
    }

    public String formatTableNameStringForPosts(String tableName)
    {
        tableName.toLowerCase ();
        tableName.replaceAll("[0-9]","");
        tableName = "[" + tableName + "posts"+"]";
        return tableName;
    }

    public int getCheckedState(String title, String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        tableName = formatTableNameStringForChallenges ( tableName );
        int state = 0;
        Cursor c = db.query(tableName, COLS_ID_TITLE_CHALLENGES,KEY_TITLE +"=?",new String[]{title},null,null,null,null);
        if(c.moveToFirst ()){
            state = c.getInt ( 5 );
        }
        c.close ();
        return state;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table SQL query
        String CREATE_POSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT NOT NULL,"
                + KEY_DES + " TEXT,"
                + KEY_PSEUDOCODE + " TEXT,"
                + KEY_INPUT + " TEXT,"
                + KEY_OUTPUT + " TEXT"
                + ")";

        String CREATE_CHALLENGES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CHALLENGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT NOT NULL,"
                + KEY_DES + " TEXT,"
                + KEY_INPUT + " TEXT,"
                + KEY_OUTPUT + " TEXT,"
                + KEY_COMPLETE + " INTEGER"
                + ")";

        db.execSQL(CREATE_POSTS_TABLE);
        db.execSQL(CREATE_CHALLENGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        Log.d(TAG,DROP_TABLE);

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }
}
