package kz.bmk.course_project.Features.AlbumCRUD.ShowAlbumList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kz.bmk.course_project.Database.DatabaseQueryClass;
import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.Album;
import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.AlbumCreateDialogFragment;
import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.AlbumCreateListener;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;

public class AlbumListActivity extends AppCompatActivity implements AlbumCreateListener {

    private final DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);
    private final List<Album> albumList = new ArrayList<>();
    private long bandRegNo;
    private TextView summaryTextView;
    private TextView albumListEmptyTextView;
    private AlbumListRecyclerViewAdapter albumListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        albumListEmptyTextView = findViewById(R.id.emptyListTextView);

        bandRegNo = getIntent().getLongExtra(Config.BAND_REGISTRATION, -1);

        albumList.addAll(databaseQueryClass.getAllAlbumsByRegNo(bandRegNo));

        albumListRecyclerViewAdapter = new AlbumListRecyclerViewAdapter(this, albumList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(albumListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openAlbumCreateDialog());
    }

    private void printSummary() {
        long bandNum = databaseQueryClass.getNumberOfBand();
        long albumNum = databaseQueryClass.getNumberOfAlbum();

        summaryTextView.setText(getResources().getString(R.string.database_summary, bandNum, albumNum));
    }

    private void openAlbumCreateDialog() {
        AlbumCreateDialogFragment albumCreateDialogFragment = AlbumCreateDialogFragment.newInstance(bandRegNo, this);
        albumCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_ALBUM);
    }

    @Override
    public void onAlbumCreated(Album album) {
        albumList.add(album);
        albumListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (albumList.isEmpty())
            albumListEmptyTextView.setVisibility(View.VISIBLE);
        else
            albumListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Вы уверены, что хотите удалить все альбомы?");
                alertDialogBuilder.setPositiveButton("Да",
                        (arg0, arg1) -> {
                            boolean isAllDeleted = databaseQueryClass.deleteAllAlbumsByRegNum(bandRegNo);
                            if (isAllDeleted) {
                                albumList.clear();
                                albumListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Нет", (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
