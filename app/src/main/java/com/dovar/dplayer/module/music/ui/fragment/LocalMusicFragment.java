package com.dovar.dplayer.module.music.ui.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dovar.dplayer.R;
import com.dovar.dplayer.bean.LocalTrack;
import com.dovar.dplayer.common.base.BaseFragment;
import com.dovar.dplayer.module.music.adapter.LocalMusicAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-11.
 */

public class LocalMusicFragment extends BaseFragment {

    private LocalMusicAdapter mAdapter;
    private RecyclerView localMusicList;

    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        Bundle mBundle = new Bundle();
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_local_music, container, false);
        return mainView;
    }

    @Override
    protected void initUI() {
        localMusicList = findView(R.id.localMusicList, mainView);
        localMusicList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initData() {
        mAdapter = new LocalMusicAdapter(getActivity());
        localMusicList.setAdapter(mAdapter);

        getLocalSongs();
        mAdapter.addDatas(finalLocalSearchResultList, true);
    }

    ArrayList<LocalTrack> finalLocalSearchResultList = new ArrayList<>();

    private void getLocalSongs() {
//        localTrackList.clear();
//        recentlyAddedTrackList.clear();
        finalLocalSearchResultList.clear();
//        finalRecentlyAddedTrackSearchResultList.clear();
//        albums.clear();
//        finalAlbums.clear();
//        artists.clear();
//        finalArtists.clear();

        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int pathColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                String path = musicCursor.getString(pathColumn);
                long duration = musicCursor.getLong(durationColumn);
                if (duration > 10000) {
                    LocalTrack lt = new LocalTrack(thisId, thisTitle, thisArtist, thisAlbum, path, duration);
//                    localTrackList.add(lt);
                    finalLocalSearchResultList.add(lt);
//                    if (recentlyAddedTrackList.size() <= 50) {
//                        recentlyAddedTrackList.add(lt);
//                        finalRecentlyAddedTrackSearchResultList.add(lt);
//                    }

//                    int pos;
//                    if (thisAlbum != null) {
//                        pos = checkAlbum(thisAlbum);
//                        if (pos != -1) {
//                            albums.get(pos).getAlbumSongs().add(lt);
//                        } else {
//                            List<LocalTrack> llt = new ArrayList<>();
//                            llt.add(lt);
//                            Album ab = new Album(thisAlbum, llt);
//                            albums.add(ab);
//                        }
//                        if (pos != -1) {
//                            finalAlbums.get(pos).getAlbumSongs().add(lt);
//                        } else {
//                            List<LocalTrack> llt = new ArrayList<>();
//                            llt.add(lt);
//                            Album ab = new Album(thisAlbum, llt);
//                            finalAlbums.add(ab);
//                        }
//                    }

//                    if (thisArtist != null) {
//                        pos = checkArtist(thisArtist);
//                        if (pos != -1) {
//                            artists.get(pos).getArtistSongs().add(lt);
//                        } else {
//                            List<LocalTrack> llt = new ArrayList<>();
//                            llt.add(lt);
//                            Artist ab = new Artist(thisArtist, llt);
//                            artists.add(ab);
//                        }
//                        if (pos != -1) {
//                            finalArtists.get(pos).getArtistSongs().add(lt);
//                        } else {
//                            List<LocalTrack> llt = new ArrayList<>();
//                            llt.add(lt);
//                            Artist ab = new Artist(thisArtist, llt);
//                            finalArtists.add(ab);
//                        }
//                    }

//                    File f = new File(path);
//                    String dirName = f.getParentFile().getName();
//                    if (getFolder(dirName) == null) {
//                        MusicFolder mf = new MusicFolder(dirName);
//                        mf.getLocalTracks().add(lt);
//                        allMusicFolders.getMusicFolders().add(mf);
//                    } else {
//                        getFolder(dirName).getLocalTracks().add(lt);
//                    }
                }

            } while (musicCursor.moveToNext());
        }

        if (musicCursor != null)
            musicCursor.close();

//        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//        try {
//            if (localTrackList.size() > 0) {
//                Collections.sort(localTrackList, new LocalMusicComparator());
//                Collections.sort(finalLocalSearchResultList, new LocalMusicComparator());
//            }
//            if (albums.size() > 0) {
//                Collections.sort(albums, new AlbumComparator());
//                Collections.sort(finalAlbums, new AlbumComparator());
//            }
//            if (artists.size() > 0) {
//                Collections.sort(artists, new ArtistComparator());
//                Collections.sort(finalArtists, new ArtistComparator());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        List<UnifiedTrack> tmp = new ArrayList<>();
//        boolean queueCurrentIndexCollision = false;
//        int indexCorrection = 0;
//        for (int i = 0; i < queue.getQueue().size(); i++) {
//            UnifiedTrack ut = queue.getQueue().get(i);
//            if (ut.getType()) {
//                if (!checkTrack(ut.getLocalTrack())) {
//                    if (i == queueCurrentIndex) {
//                        queueCurrentIndexCollision = true;
//                    } else if (i < queueCurrentIndex) {
//                        indexCorrection++;
//                    }
//                    tmp.add(ut);
//                }
//            }
//        }
//        for (int i = 0; i < tmp.size(); i++) {
//            queue.getQueue().remove(tmp.get(i));
//        }
//        if (queueCurrentIndexCollision) {
//            if (queue.getQueue().size() > 0) {
//                queueCurrentIndex = 0;
//            } else {
//                queue = new Queue();
//            }
//        } else {
//            queueCurrentIndex -= indexCorrection;
//        }
//
//        tmp.clear();
//
//        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
//            UnifiedTrack ut = recentlyPlayed.getRecentlyPlayed().get(i);
//            if (ut.getType()) {
//                if (!checkTrack(ut.getLocalTrack())) {
//                    tmp.add(ut);
//                }
//            }
//        }
//        for (int i = 0; i < tmp.size(); i++) {
//            recentlyPlayed.getRecentlyPlayed().remove(tmp.get(i));
//        }
//
//        List<UnifiedTrack> temp = new ArrayList<>();
//        List<Playlist> tmpPL = new ArrayList<>();
//
//        for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
//            Playlist pl = allPlaylists.getPlaylists().get(i);
//            for (int j = 0; j < pl.getSongList().size(); j++) {
//                UnifiedTrack ut = pl.getSongList().get(j);
//                if (ut.getType()) {
//                    if (!checkTrack(ut.getLocalTrack())) {
//                        temp.add(ut);
//                    }
//                }
//            }
//            for (int j = 0; j < temp.size(); j++) {
//                pl.getSongList().remove(temp.get(j));
//            }
//            temp.clear();
//            if (pl.getSongList().size() == 0) {
//                tmpPL.add(pl);
//            }
//        }
//        for (int i = 0; i < tmpPL.size(); i++) {
//            allPlaylists.getPlaylists().remove(tmpPL.get(i));
//        }
//        tmpPL.clear();
    }
}
