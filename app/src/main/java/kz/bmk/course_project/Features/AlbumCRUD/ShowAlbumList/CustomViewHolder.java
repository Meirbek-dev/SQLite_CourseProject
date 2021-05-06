package kz.bmk.course_project.Features.AlbumCRUD.ShowAlbumList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import kz.bmk.course_project.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    final TextView albumNameTextView;
    final TextView albumPriceTextView;
    final TextView albumRelDateTextView;
    final ImageView crossButtonImageView;
    final ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);

        albumNameTextView = itemView.findViewById(R.id.albumNameTextView);
        albumPriceTextView = itemView.findViewById(R.id.albumPriceTextView);
        albumRelDateTextView = itemView.findViewById(R.id.albumRelDateTextView);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
