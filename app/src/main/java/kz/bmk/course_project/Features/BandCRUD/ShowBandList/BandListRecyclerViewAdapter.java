package kz.bmk.course_project.Features.BandCRUD.ShowBandList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.bmk.course_project.Database.DatabaseQueryClass;
import kz.bmk.course_project.Features.AlbumCRUD.ShowAlbumList.AlbumListActivity;
import kz.bmk.course_project.Features.BandCRUD.CreateBand.Band;
import kz.bmk.course_project.Features.BandCRUD.UpdateBandInfo.BandUpdateDialogFragment;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;

public class BandListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final Context context;
    private final List<Band> bandList;
    private final DatabaseQueryClass databaseQueryClass;

    public BandListRecyclerViewAdapter(Context context, List<Band> bandList) {
        this.context = context;
        this.bandList = bandList;
        databaseQueryClass = new DatabaseQueryClass(context);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_band, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Band band = bandList.get(position);

        holder.nameTextView.setText(band.getName());
        holder.registrationNumTextView.setText(String.valueOf(band.getRegistrationNumber()));
        holder.genreTextView.setText(band.getGenre());

        holder.crossButtonImageView.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Вы уверены, что хотите удалить эту группу?");
            alertDialogBuilder.setPositiveButton("Да",
                    (arg0, arg1) -> deleteBand(itemPosition));

            alertDialogBuilder.setNegativeButton("Нет", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        holder.editButtonImageView.setOnClickListener(view -> {
            BandUpdateDialogFragment bandUpdateDialogFragment = BandUpdateDialogFragment.newInstance(band.getRegistrationNumber(), itemPosition, (band1, position1) -> {
                bandList.set(position1, band1);
                notifyDataSetChanged();
            });
            bandUpdateDialogFragment.show(((BandListActivity) context).getSupportFragmentManager(), Config.UPDATE_BAND);
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, AlbumListActivity.class);
            intent.putExtra(Config.BAND_REGISTRATION, band.getRegistrationNumber());
            context.startActivity(intent);
        });
    }

    private void deleteBand(int position) {
        Band band = bandList.get(position);
        long count = databaseQueryClass.deleteBandByRegNum(band.getRegistrationNumber());

        if (count > 0) {
            bandList.remove(position);
            notifyDataSetChanged();
            ((BandListActivity) context).viewVisibility();
            Toast.makeText(context, "Группа успешно удалена", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Ошибка! Группа не удалена", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return bandList.size();
    }
}
