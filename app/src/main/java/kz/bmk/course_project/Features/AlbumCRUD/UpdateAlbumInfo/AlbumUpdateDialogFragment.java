package kz.bmk.course_project.Features.AlbumCRUD.UpdateAlbumInfo;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import kz.bmk.course_project.Database.DatabaseQueryClass;
import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.Album;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;

public class AlbumUpdateDialogFragment extends DialogFragment {

    private static AlbumUpdateListener albumUpdateListener;
    private static long albumId;
    private static int position;
    private EditText albumNameEditText;
    private EditText albumPriceEditText;
    private EditText albumRelDateEditText;
    private DatabaseQueryClass databaseQueryClass;

    public AlbumUpdateDialogFragment() {
    }

    public static AlbumUpdateDialogFragment newInstance(long subId, int pos, AlbumUpdateListener listener) {
        albumId = subId;
        position = pos;
        albumUpdateListener = listener;

        AlbumUpdateDialogFragment albumUpdateDialogFragment = new AlbumUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("Название", "Обновить информацию об альбоме");
        albumUpdateDialogFragment.setArguments(args);

        albumUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return albumUpdateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_update_dialog, container, false);

        albumNameEditText = view.findViewById(R.id.albumNameEditText);
        albumPriceEditText = view.findViewById(R.id.albumPriceEditText);
        albumRelDateEditText = view.findViewById(R.id.albumRelDateEditText);
        Button updateButton = view.findViewById(R.id.updateButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = requireArguments().getString(Config.TITLE);
        Objects.requireNonNull(getDialog()).setTitle(title);

        Album album = databaseQueryClass.getAlbumById(albumId);

        albumNameEditText.setText(album.getName());
        albumPriceEditText.setText(String.valueOf(album.getPrice()));
        albumRelDateEditText.setText(String.valueOf(album.getRelDate()));

        updateButton.setOnClickListener(view12 -> {
            String albumName = albumNameEditText.getText().toString();
            double albumPrice = Double.parseDouble(albumPriceEditText.getText().toString());
            String albumRelDate = albumRelDateEditText.getText().toString();

            Album album1 = new Album(albumId, albumName, albumPrice, albumRelDate);

            long rowCount = databaseQueryClass.updateAlbumInfo(album1);

            if (rowCount > 0) {
                albumUpdateListener.onAlbumInfoUpdate(album1, position);
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(view1 -> getDialog().dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
