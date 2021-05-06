package kz.bmk.course_project.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kz.bmk.course_project.Util.Config;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Название БД
    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    private static DatabaseHelper databaseHelper;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Создание таблицы SQL
        String CREATE_BAND_TABLE = "CREATE TABLE " + Config.TABLE_BANDS + "("
                + Config.COLUMN_BAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_BAND_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_BAND_REG_NUM + " INTEGER NOT NULL UNIQUE, "
                + Config.COLUMN_BAND_GENRES + " TEXT " //nullable
                + ")";

        String CREATE_ALBUM_TABLE = "CREATE TABLE " + Config.TABLE_ALBUMS + "("
                + Config.COLUMN_ALBUM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_ALBUM_REG_NUM + " INTEGER NOT NULL, "
                + Config.COLUMN_ALBUM_TITLE + " TEXT NOT NULL, "
                + Config.COLUMN_ALBUM_PRICE + " DOUBLE NOT NULL, "
                + Config.COLUMN_ALBUM_RELEASE_DATE + " TEXT, "
                + "FOREIGN KEY (" + Config.COLUMN_ALBUM_REG_NUM + ") REFERENCES " + Config.TABLE_BANDS + "(" + Config.COLUMN_BAND_REG_NUM + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + Config.BAND_SUB_CONSTRAINT + " UNIQUE (" + Config.COLUMN_ALBUM_REG_NUM + "," + Config.COLUMN_ALBUM_PRICE + ")"
                + ")";

        db.execSQL(CREATE_BAND_TABLE);
        db.execSQL(CREATE_ALBUM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаление старой таблицы, если она существует
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_BANDS);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_ALBUMS);

        // Создание таблицы заново
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        db.execSQL("PRAGMA foreign_keys=ON;");
    }

}
