package kz.bmk.course_project.Util;

public class Config {

    public static final String DATABASE_NAME = "band-db";

    //Название колонок для таблицы bands
    public static final String TABLE_BANDS = "bands";
    public static final String COLUMN_BAND_ID = "_id";
    public static final String COLUMN_BAND_NAME = "name";
    public static final String COLUMN_BAND_REG_NUM = "registration_no";
    public static final String COLUMN_BAND_GENRES = "genres";

    //Название колонок для таблицы albums
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_REG_NUM = "fk_registration_no";
    public static final String COLUMN_ALBUM_TITLE = "title";
    public static final String COLUMN_ALBUM_PRICE = "price";
    public static final String COLUMN_ALBUM_RELEASE_DATE = "release_date";
    public static final String BAND_SUB_CONSTRAINT = "band_sub_unique";


    public static final String TITLE = "title";
    public static final String CREATE_BAND = "create_band";
    public static final String UPDATE_BAND = "update_band";
    public static final String CREATE_ALBUM = "create_album";
    public static final String UPDATE_ALBUM = "update_album";
    public static final String BAND_REGISTRATION = "band_registration";
}
