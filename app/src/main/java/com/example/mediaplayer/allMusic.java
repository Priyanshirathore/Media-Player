package com.example.mediaplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


public class allMusic extends Fragment {
ListView allMusicList;
ArrayAdapter<String> musicArrayAdapter;
String songs[];
ArrayList<File>music;


    public allMusic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_all_music, container, false);

       allMusicList=view.findViewById(R.id.MusicList);

        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener(){

            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                      music= findMusicFiles(Environment.getExternalStorageDirectory());
                      songs=new String[music.size()];
                      for(int i=0;i<music.size();i++){
                          songs[i] = music.get(i).getName();

                      }
//                      songs[1] = ""
             //   Toast.makeText(getContext(),"yeh hain toast "+songs.length, Toast.LENGTH_SHORT).show();
                      musicArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,songs);
                      allMusicList.setAdapter(musicArrayAdapter);

                      allMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                              Intent intent = new Intent(getActivity(),Player.class);
                              intent.putExtra("songList",music);
                              intent.putExtra("position",position);

                              startActivity(intent);
                          }
                      });
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                // keep asking permission until granted
                permissionToken.continuePermissionRequest();
            }
        }).check();

        return view;
    }
    private ArrayList<File> findMusicFiles(File file){
        String s= file.toString();
//        Toast.makeText(getContext(),"yeh hain toast "+s, Toast.LENGTH_SHORT).show();
        file = new File(s);
        ArrayList<File> allMusicFilesObject = new ArrayList<>();
        File [] files = file.listFiles();
//        Toast.makeText(getContext(),"yeh hain toast --> "+file.listFiles(), Toast.LENGTH_SHORT).show();

        for(File currentfile: files){
            if(currentfile.isDirectory() && !currentfile.isHidden())
            {
                  allMusicFilesObject.addAll(findMusicFiles(currentfile));
            }
            else{
                if(currentfile.getName().endsWith(".mp3")||currentfile.getName().endsWith(".mp4a")||currentfile.getName().endsWith(".wav") )
                {
                    allMusicFilesObject.add(currentfile);
                }

            }
        }
        return allMusicFilesObject;
    }
}