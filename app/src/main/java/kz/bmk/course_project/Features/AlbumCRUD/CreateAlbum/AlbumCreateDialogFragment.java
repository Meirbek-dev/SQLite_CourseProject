package kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum;


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
import kz.bmk.course_project.R;


public class AlbumCreateDialogFragment extends DialogFragment {

    private static long bandRegistrationNumber;
    private static AlbumCreateListener albumCreateListener;

    private EditText albumNameEditText;
    private EditText albumPriceEditText;
    private EditText albumRelDateEditText;

    public AlbumCreateDialogFragment() {
    }

    public static AlbumCreateDialogFragment newInstance(long bandRegNo, AlbumCreateListener listener) {
        bandRegistrationNumber = bandRegNo;
        albumCreateListener = listener;

        AlbumCreateDialogFragment albumCreateDialogFragment = new AlbumCreateDialogFragment();

        albumCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return albumCreateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_create_dialog, container, false);
        Objects.requireNonNull(getDialog()).setTitle(getResources().getString(R.string.add_new_album));

        albumNameEditText = view.findViewById(R.id.albumNameEditText);
        albumPriceEditText = view.findViewById(R.id.albumPriceEditText);
        albumRelDateEditText = view.findViewById(R.id.albumRelDateEditText);
        Button createButton = view.findViewById(R.id.createButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        createButton.setOnClickListener(view12 -> {
            String albumName = albumNameEditText.getText().toString();
            double albumPrice = Double.parseDouble(albumPriceEditText.getText().toString());
            String albumRelDate = albumRelDateEditText.getText().toString();

            Album album = new Album(-1, albumName, albumPrice, albumRelDate);

            DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

            long id = databaseQueryClass.insertAlbum(album, bandRegistrationNumber);

            if (id > 0) {
                album.setId(id);
                albumCreateListener.onAlbumCreated(album);
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
