package kz.bmk.course_project.Features.BandCRUD.CreateBand;

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
import kz.bmk.course_project.Util.Config;


public class BandCreateDialogFragment extends DialogFragment {

    private static BandCreateListener bandCreateListener;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText genreEditText;

    private String nameString = "";
    private long registrationNumber = -1;
    private String genreString = "";

    public BandCreateDialogFragment() {
        // Required empty public constructor
    }

    public static BandCreateDialogFragment newInstance(String title, BandCreateListener listener) {
        bandCreateListener = listener;
        BandCreateDialogFragment bandCreateDialogFragment = new BandCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        bandCreateDialogFragment.setArguments(args);

        bandCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return bandCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_band_create_dialog, container, false);

        nameEditText = view.findViewById(R.id.bandNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        genreEditText = view.findViewById(R.id.genreEditText);
        Button createButton = view.findViewById(R.id.createButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        String title = requireArguments().getString(Config.TITLE);
        Objects.requireNonNull(getDialog()).setTitle(title);

        createButton.setOnClickListener(view12 -> {
            nameString = nameEditText.getText().toString();
            registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
            genreString = genreEditText.getText().toString();

            Band band = new Band(-1, nameString, registrationNumber, genreString);

            DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

            long id = databaseQueryClass.insertBand(band);

            if (id > 0) {
                band.setId(id);
                bandCreateListener.onBandCreated(band);
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
