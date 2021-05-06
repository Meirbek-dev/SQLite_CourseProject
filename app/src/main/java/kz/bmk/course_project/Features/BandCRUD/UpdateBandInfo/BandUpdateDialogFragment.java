package kz.bmk.course_project.Features.BandCRUD.UpdateBandInfo;

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
import kz.bmk.course_project.Features.BandCRUD.CreateBand.Band;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;


public class BandUpdateDialogFragment extends DialogFragment {

    private static long bandRegNo;
    private static int bandItemPosition;
    private static BandUpdateListener bandUpdateListener;

    private Band mBand;

    private EditText nameEditText;
    private EditText registrationEditText;
    private EditText genreEditText;

    private String nameString = "";
    private long registrationNumber = -1;
    private String genreString = "";

    private DatabaseQueryClass databaseQueryClass;

    public BandUpdateDialogFragment() {
    }

    public static BandUpdateDialogFragment newInstance(long registrationNumber, int position, BandUpdateListener listener) {
        bandRegNo = registrationNumber;
        bandItemPosition = position;
        bandUpdateListener = listener;
        BandUpdateDialogFragment bandUpdateDialogFragment = new BandUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("Имя", "Обновить информацию о группе");
        bandUpdateDialogFragment.setArguments(args);

        bandUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return bandUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_band_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        nameEditText = view.findViewById(R.id.bandNameEditText);
        registrationEditText = view.findViewById(R.id.registrationEditText);
        genreEditText = view.findViewById(R.id.genreEditText);
        Button updateButton = view.findViewById(R.id.updateBandInfoButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        String title = requireArguments().getString(Config.TITLE);
        Objects.requireNonNull(getDialog()).setTitle(title);

        mBand = databaseQueryClass.getBandByRegNum(bandRegNo);

        if (mBand != null) {
            nameEditText.setText(mBand.getName());
            registrationEditText.setText(String.valueOf(mBand.getRegistrationNumber()));
            genreEditText.setText(mBand.getGenre());

            updateButton.setOnClickListener(view12 -> {
                nameString = nameEditText.getText().toString();
                registrationNumber = Integer.parseInt(registrationEditText.getText().toString());
                genreString = genreEditText.getText().toString();

                mBand.setName(nameString);
                mBand.setRegistrationNumber(registrationNumber);
                mBand.setGenre(genreString);

                long id = databaseQueryClass.updateBandInfo(mBand);

                if (id > 0) {
                    bandUpdateListener.onBandInfoUpdated(mBand, bandItemPosition);
                    getDialog().dismiss();
                }
            });

            cancelButton.setOnClickListener(view1 -> getDialog().dismiss());

        }

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
