package kz.bmk.course_project.Features.BandCRUD.ShowBandList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import kz.bmk.course_project.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    final TextView nameTextView;
    final TextView registrationNumTextView;
    final TextView genreTextView;
    final ImageView crossButtonImageView;
    final ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.nameTextView);
        registrationNumTextView = itemView.findViewById(R.id.registrationNumTextView);
        genreTextView = itemView.findViewById(R.id.genreTextView);
        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
