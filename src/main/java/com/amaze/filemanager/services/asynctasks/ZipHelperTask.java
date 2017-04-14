package com.amaze.filemanager.services.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.amaze.filemanager.ui.ZipObj;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Vishal on 11/23/2014.
 */
public class ZipHelperTask extends AsyncTask<String, Void, ArrayList<ZipObj>> {

    String dir;

    /**
     * AsyncTask to load ZIP file items.
     * @param zipViewer the zipViewer fragment instance
     * @param dir
     */

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<ZipObj> doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ZipObj> zipEntries) {
    }

    class FileListSorter implements Comparator<ZipObj> {


        public FileListSorter() {

        }

        @Override
        public int compare(ZipObj file1, ZipObj file2) {
            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;


            } else if (file2.isDirectory() && !(file1).isDirectory()) {
                return 1;
            }
            return file1.getEntry().getName().compareToIgnoreCase(file2.getEntry().getName());
        }
    }
}
