package com.amaze.filemanager.services.asynctasks;

/**
 * Created by Arpit on 25-01-2015.
 */

import android.os.AsyncTask;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Vishal on 11/23/2014.
 */
public class RarHelperTask extends AsyncTask<File, Void, ArrayList<FileHeader>> {

    String dir;

    /**
     * AsyncTask to load RAR file items.
     * @param zipViewer the zipViewer fragment instance
     * @param dir
     */

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<FileHeader> doInBackground(File... params) {
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<FileHeader> zipEntries) {
    }

    class FileListSorter implements Comparator<FileHeader> {


        public FileListSorter() {

        }

        @Override
        public int compare(FileHeader file1, FileHeader file2) {

            if (file1.isDirectory() && !file2.isDirectory()) {
                return -1;


            } else if (file2.isDirectory() && !(file1).isDirectory()) {
                return 1;
            }
            return file1.getFileNameString().compareToIgnoreCase(file2.getFileNameString());
        }
    }
}

