
package org.orbitbreak.tonematrix;

// taken from http://android-er.blogspot.com/2010/01/implement-simple-file-explorer-in.html
// adapted to show only desired formats

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.orbitbreak.tonematrix.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidExplorer extends ListActivity {

    private List<String> item = null;

    private List<String> path = null;

    private String root = "/sdcard";

    private TextView myPath;

    private String regexp = "(?i:.*.(ogg|mp3))";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileexplorer_main);
        myPath = (TextView) findViewById(R.id.path);
        getDir(root);
    }

    private void getDir(String dirPath) {
        myPath.setText("Location: " + dirPath);

        item = new ArrayList<String>();
        path = new ArrayList<String>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(root)) {

            item.add(root);
            path.add(root);

            item.add("../");
            path.add(f.getParent());

        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                path.add(file.getPath());
                item.add(file.getName() + "/");
            } else {
                if (file.getName().matches(regexp)) {
                    path.add(file.getPath());
                    item.add(file.getName());
                }
            }
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.fileexplorer_row,
                item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = new File(path.get(position));

        if (file.isDirectory()) {
            if (file.canRead())
                getDir(path.get(position));
            else {
                new AlertDialog.Builder(this).setIcon(R.drawable.icon)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), BoardActivity.class);
            resultIntent.putExtra("path", file.getPath());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
