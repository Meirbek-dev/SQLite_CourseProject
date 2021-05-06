package kz.bmk.course_project.Features.AlbumCRUD.ShowAlbumList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kz.bmk.course_project.Database.DatabaseQueryClass;
import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.Album;
import kz.bmk.course_project.Features.AlbumCRUD.UpdateAlbumInfo.AlbumUpdateDialogFragment;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;

public class AlbumListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final Context context;
    private final List<Album> albumList;

    public AlbumListRecyclerViewAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int listPosition = position;
        final Album album = albumList.get(position);

        holder.albumNameTextView.setText(album.getName());
        holder.albumPriceTextView.setText(String.valueOf(album.getPrice()));
        holder.albumPriceTextView.append(" тг");
        holder.albumRelDateTextView.setText(String.valueOf(album.getRelDate()));
        holder.crossButtonImageView.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Вы уверены, что хотите удалить этот альбом?");
            alertDialogBuilder.setPositiveButton("Да",
                    (arg0, arg1) -> deleteAlbum(album));

            alertDialogBuilder.setNegativeButton("Нет", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        holder.editButtonImageView.setOnClickListener(view -> editAlbum(album.getId(), listPosition));
    }

    private void editAlbum(long albumId, int listPosition) {
        AlbumUpdateDialogFragment albumUpdateDialogFragment = AlbumUpdateDialogFragment.newInstance(albumId, listPosition, (album, position) -> {
            albumList.set(position, album);
            notifyDataSetChanged();
        });
        albumUpdateDialogFragment.show(((AlbumListActivity) context).getSupportFragmentManager(), Config.UPDATE_ALBUM);
    }

    private void deleteAlbum(Album album) {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
        boolean isDeleted = databaseQueryClass.deleteAlbumById(album.getId());

        if (isDeleted) {
            albumList.remove(album);
            notifyDataSetChanged();
            ((AlbumListActivity) context).viewVisibility();
        } else
            Toast.makeText(context, "Не удалено!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
