package kz.bmk.course_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.Album;
import kz.bmk.course_project.Features.BandCRUD.CreateBand.Band;
import kz.bmk.course_project.Util.Config;


public class DatabaseQueryClass {

    private final Context context;

    public DatabaseQueryClass(Context context) {
        this.context = context;
    }

    public long insertBand(Band band) {

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_BAND_NAME, band.getName());
        contentValues.put(Config.COLUMN_BAND_REG_NUM, band.getRegistrationNumber());
        contentValues.put(Config.COLUMN_BAND_GENRES, band.getGenre());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_BANDS, null, contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Операция не удалась: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Band> getAllBand() {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase(); Cursor cursor = sqLiteDatabase.query(Config.TABLE_BANDS, null, null, null, null, null, null, null)) {

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    List<Band> bandList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BAND_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BAND_NAME));
                        long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_BAND_REG_NUM));
                        String genre = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BAND_GENRES));

                        bandList.add(new Band(id, name, registrationNumber, genre));
                    } while (cursor.moveToNext());

                    return bandList;
                }
        } catch (Exception e) {
            Toast.makeText(context, "Операция не удалась", Toast.LENGTH_SHORT).show();
        }

        return Collections.emptyList();
    }

    public Band getBandByRegNum(long registrationNum) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Band band = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_BANDS, null,
                    Config.COLUMN_BAND_REG_NUM + " = ? ", new String[]{String.valueOf(registrationNum)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_BAND_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BAND_NAME));
                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_BAND_REG_NUM));
                String genre = cursor.getString(cursor.getColumnIndex(Config.COLUMN_BAND_GENRES));

                band = new Band(id, name, registrationNumber, genre);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Операция не удалась", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return band;
    }

    public long updateBandInfo(Band band) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_BAND_NAME, band.getName());
        contentValues.put(Config.COLUMN_BAND_REG_NUM, band.getRegistrationNumber());
        contentValues.put(Config.COLUMN_BAND_GENRES, band.getGenre());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_BANDS, contentValues,
                    Config.COLUMN_BAND_ID + " = ? ",
                    new String[]{String.valueOf(band.getId())});
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteBandByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_BANDS,
                    Config.COLUMN_BAND_REG_NUM + " = ? ",
                    new String[]{String.valueOf(registrationNum)});
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return deletedRowCount;
    }

    public boolean deleteAllBands() {
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            sqLiteDatabase.delete(Config.TABLE_BANDS, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_BANDS);

            if (count == 0)
                deleteStatus = true;

        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return deleteStatus;
    }

    public long getNumberOfBand() {
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_BANDS);
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return count;
    }

    // albums
    public long insertAlbum(Album album, long registrationNo) {
        long rowId = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ALBUM_TITLE, album.getName());
        contentValues.put(Config.COLUMN_ALBUM_PRICE, album.getPrice());
        contentValues.put(Config.COLUMN_ALBUM_RELEASE_DATE, album.getRelDate());
        contentValues.put(Config.COLUMN_ALBUM_REG_NUM, registrationNo);

        try {
            rowId = sqLiteDatabase.insertOrThrow(Config.TABLE_ALBUMS, null, contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowId;
    }

    public Album getAlbumById(long albumId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Album album = null;

        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(Config.TABLE_ALBUMS, null,
                    Config.COLUMN_ALBUM_ID + " = ? ", new String[]{String.valueOf(albumId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String albumName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALBUM_TITLE));
                double albumPrice = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_ALBUM_PRICE));
                String albumRelDate = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALBUM_RELEASE_DATE));

                album = new Album(albumId, albumName, albumPrice, albumRelDate);
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return album;
    }

    public long updateAlbumInfo(Album album) {

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_ALBUM_TITLE, album.getName());
        contentValues.put(Config.COLUMN_ALBUM_PRICE, album.getPrice());
        contentValues.put(Config.COLUMN_ALBUM_RELEASE_DATE, album.getRelDate());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_ALBUMS, contentValues,
                    Config.COLUMN_ALBUM_ID + " = ? ",
                    new String[]{String.valueOf(album.getId())});
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<Album> getAllAlbumsByRegNo(long registrationNo) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Album> albumList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.query(Config.TABLE_ALBUMS,
                    new String[]{Config.COLUMN_ALBUM_ID, Config.COLUMN_ALBUM_TITLE, Config.COLUMN_ALBUM_PRICE, Config.COLUMN_ALBUM_RELEASE_DATE},
                    Config.COLUMN_ALBUM_REG_NUM + " = ? ",
                    new String[]{String.valueOf(registrationNo)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                albumList = new ArrayList<>();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_ALBUM_ID));
                    String albumName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALBUM_TITLE));
                    double albumPrice = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_ALBUM_PRICE));
                    String albumRelDate = cursor.getString(cursor.getColumnIndex(Config.COLUMN_ALBUM_RELEASE_DATE));

                    albumList.add(new Album(id, albumName, albumPrice, albumRelDate));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return albumList;
    }

    public boolean deleteAlbumById(long albumId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_ALBUMS,
                Config.COLUMN_ALBUM_ID + " = ? ", new String[]{String.valueOf(albumId)});

        return row > 0;
    }

    public boolean deleteAllAlbumsByRegNum(long registrationNum) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_ALBUMS,
                Config.COLUMN_ALBUM_REG_NUM + " = ? ", new String[]{String.valueOf(registrationNum)});

        return row > 0;
    }

    public long getNumberOfAlbum() {
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_ALBUMS);
        } catch (SQLiteException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return count;
    }
}

