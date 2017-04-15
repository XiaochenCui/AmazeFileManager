/*
 * Copyright (C) 2014 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiaochen.videoplayer.services.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.widget.Toast;

import com.xiaochen.videoplayer.activities.BaseActivity;
import com.xiaochen.videoplayer.exceptions.RootNotPermittedException;
import com.xiaochen.videoplayer.filesystem.BaseFile;
import com.xiaochen.videoplayer.filesystem.HFile;
import com.xiaochen.videoplayer.filesystem.RootHelper;
import com.xiaochen.videoplayer.fragments.Main;
import com.xiaochen.videoplayer.ui.Layoutelements;
import com.xiaochen.videoplayer.ui.icons.Icons;
import com.xiaochen.videoplayer.utils.DataUtils;
import com.xiaochen.videoplayer.utils.FileListSorter;
import com.xiaochen.videoplayer.utils.OpenMode;
import com.xiaochen.videoplayer.utils.provider.UtilitiesProviderInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class LoadList extends AsyncTask<String, String, ArrayList<Layoutelements>> {
    private UtilitiesProviderInterface utilsProvider;

    private String path;
    boolean back;
    Main ma;
    Context c;
    OpenMode openmode;
    public LoadList(Context c, UtilitiesProviderInterface utilsProvider, boolean back, Main ma, OpenMode openmode) {
        this.utilsProvider = utilsProvider;
        this.back = back;
        this.ma = ma;
        this.openmode = openmode;
        this.c=c;
    }

    @Override
    protected void onPreExecute() {
        if (ma!=null && ma.mSwipeRefreshLayout!=null)
            ma.mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onProgressUpdate(String... message) {
        if(c!=null)
            Toast.makeText(c, message[0], Toast.LENGTH_SHORT).show();
    }

    boolean grid;

    @Override
    // Actual download method, run in the task thread
    protected ArrayList<Layoutelements> doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        ArrayList<Layoutelements> list = null;
        path = params[0];
        grid = ma.checkforpath(path);
        ma.folder_count = 0;
        ma.file_count = 0;
        if (openmode == OpenMode.UNKNOWN) {
            HFile hFile = new HFile(OpenMode.UNKNOWN, path);
            hFile.generateMode(ma.getActivity());
            if (hFile.isLocal()) {
                openmode = OpenMode.FILE;
            } else if (hFile.isSmb()) {
                openmode = OpenMode.SMB;
                ma.smbPath = path;
            } else if (hFile.isOtgFile()) {
                openmode = OpenMode.OTG;
            } else if (hFile.isCustomPath())
                openmode = OpenMode.CUSTOM;
            else if (android.util.Patterns.EMAIL_ADDRESS.matcher(path).matches()) {
                openmode = OpenMode.ROOT;
            }
        }

        switch (openmode) {
            case CUSTOM:
                ArrayList<BaseFile> arrayList = null;
                arrayList = (listVideos());
                try {
                    if (arrayList != null)
                        list = addTo(arrayList);
                    else return new ArrayList<>();
                } catch (Exception e) {
                }
                break;
            case OTG:
                list = addTo(listOtg(path));
                openmode = OpenMode.OTG;
                break;
            default:
                // we're neither in OTG not in SMB, load the list based on root/general filesystem
                try {
                    ArrayList<BaseFile> arrayList1;
                    arrayList1 = RootHelper.getFilesList(path, BaseActivity.rootMode, ma.SHOW_HIDDEN,
                            new RootHelper.GetModeCallBack() {
                        @Override
                        public void getMode(OpenMode mode) {
                            openmode = mode;
                        }
                    });
                    list = addTo(arrayList1);

                } catch (RootNotPermittedException e) {
                    //AppConfig.toast(c, c.getString(R.string.rootfailure));
                    return null;
                }
                break;
        }

        if (list != null && !(openmode == OpenMode.CUSTOM && ((path).equals("5") || (path).equals("6"))))
            Collections.sort(list, new FileListSorter(ma.dsort, ma.sortby, ma.asc, BaseActivity.rootMode));
        return list;

    }

    private ArrayList<Layoutelements> addTo(ArrayList<BaseFile> mFile) {
        ArrayList<Layoutelements> a = new ArrayList<Layoutelements>();
        for (int i = 0; i < mFile.size(); i++) {
            BaseFile ele = mFile.get(i);
            File f = new File(ele.getPath());
            String size = "";
            if (!DataUtils.hiddenfiles.contains(ele.getPath())) {
                if (ele.isDirectory()) {
                    size = "";
                    Layoutelements layoutelements = utilsProvider.getFutils().newElement(ma.folder,
                            f.getPath(), ele.getPermisson(), ele.getLink(), size, 0, true, false,
                            ele.getDate() + "");
                    layoutelements.setMode(ele.getMode());
                    a.add(layoutelements);
                    ma.folder_count++;
                } else {
                    long longSize = 0;
                    try {
                        if (ele.getSize() != -1) {
                            longSize = Long.valueOf(ele.getSize());
                            size = Formatter.formatFileSize(c, longSize);
                        } else {
                            size = "";
                            longSize = 0;
                        }
                    } catch (NumberFormatException e) {
                        //e.printStackTrace();
                    }
                    try {
                        Layoutelements layoutelements = utilsProvider.getFutils().newElement(Icons.loadMimeIcon(
                                f.getPath(), !ma.IS_LIST, ma.res), f.getPath(), ele.getPermisson(),
                                ele.getLink(), size, longSize, false, false, ele.getDate() + "");
                        layoutelements.setMode(ele.getMode());
                        a.add(layoutelements);
                        ma.file_count++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return a;
    }

    @Override
    protected void onPostExecute(ArrayList<Layoutelements> list) {
        if (isCancelled()) {
            list = null;
        }

        ma.createViews(list, back, path, openmode, false, grid);
        ma.mSwipeRefreshLayout.setRefreshing(false);
    }


    ArrayList<BaseFile> listVideos() {
        ArrayList<BaseFile> songs = new ArrayList<>();
        final String[] projection = {MediaStore.Images.Media.DATA};
        final Cursor cursor = c.getContentResolver().query(MediaStore.Video.Media
                        .EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex
                        (MediaStore.Files.FileColumns.DATA));
                BaseFile strings = RootHelper.generateBaseFile(new File(path), ma.SHOW_HIDDEN);
                if (strings != null) songs.add(strings);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    /**
     * Lists files from an OTG device
     * @param path the path to the directory tree, starts with prefix 'otg:/'
     *             Independent of URI (or mount point) for the OTG
     * @return a list of files loaded
     */
    ArrayList<BaseFile> listOtg(String path) {

        return RootHelper.getDocumentFilesList(path, c);
    }

    boolean contains(String[] types, String path) {
        for (String string : types) {
            if (path.endsWith(string)) return true;
        }
        return false;
    }
}
