package kz.bmk.course_project.Features.BandCRUD.ShowBandList;

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

import kz.bmk.course_project.Database.DatabaseQueryClass;
import kz.bmk.course_project.Features.BandCRUD.CreateBand.Band;
import kz.bmk.course_project.Features.BandCRUD.CreateBand.BandCreateDialogFragment;
import kz.bmk.course_project.Features.BandCRUD.CreateBand.BandCreateListener;
import kz.bmk.course_project.R;
import kz.bmk.course_project.Util.Config;

public class BandListActivity extends AppCompatActivity implements BandCreateListener {

    private final DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private final List<Band> bandList = new ArrayList<>();

    private TextView summaryTextView;
    private TextView bandListEmptyTextView;
    private BandListRecyclerViewAdapter bandListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        bandListEmptyTextView = findViewById(R.id.emptyListTextView);

        bandList.addAll(databaseQueryClass.getAllBand());

        bandListRecyclerViewAdapter = new BandListRecyclerViewAdapter(this, bandList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bandListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openBandCreateDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        printSummary();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_delete) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Вы уверены, что хотите удалить все группы?");
            alertDialogBuilder.setPositiveButton("Да",
                    (arg0, arg1) -> {
                        boolean isAllDeleted = databaseQueryClass.deleteAllBands();
                        if (isAllDeleted) {
                            bandList.clear();
                            bandListRecyclerViewAdapter.notifyDataSetChanged();
                            viewVisibility();
                        }
                    });

            alertDialogBuilder.setNegativeButton("Нет", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if (bandList.isEmpty())
            bandListEmptyTextView.setVisibility(View.VISIBLE);
        else
            bandListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    private void openBandCreateDialog() {
        BandCreateDialogFragment bandCreateDialogFragment = BandCreateDialogFragment.newInstance("Добавить группу", this);
        bandCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_BAND);
    }

    private void printSummary() {
        long bandNum = databaseQueryClass.getNumberOfBand();
        long albumNum = databaseQueryClass.getNumberOfAlbum();

        summaryTextView.setText(getResources().getString(R.string.database_summary, bandNum, albumNum));
    }

    @Override
    public void onBandCreated(Band band) {
        bandList.add(band);
        bandListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

}
