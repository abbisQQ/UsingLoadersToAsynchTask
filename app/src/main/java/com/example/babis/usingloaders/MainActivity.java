package com.example.babis.usingloaders;

import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    Button btn;
    TextView tv;
    ProgressBar bar;
    HttpURLConnection connection;
    URL url;
    BufferedReader reader;
    InputStream stream;
    private final int LOADER_ID = 1234;
    private final String LOADER_STRING_URL = "http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.btn);
        tv = (TextView)findViewById(R.id.tv);
        bar = (ProgressBar)findViewById(R.id.myBar);
        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //new myTask().execute("https://api.themoviedb.org/3/movie/550?api_key=");


                getSupportLoaderManager().initLoader(LOADER_ID,null,MainActivity.this).forceLoad();


            }
        });

    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {


            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                Toast.makeText(getContext(),"onstartloading",Toast.LENGTH_SHORT).show();
                btn.setVisibility(View.GONE);
                tv.setVisibility(View.INVISIBLE);
                bar.setVisibility(View.VISIBLE);
            }

            @Override
            public String loadInBackground() {

                try {

                    url = new URL(LOADER_STRING_URL);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.connect();

                    stream = connection.getInputStream();

                    //passing the stream to the reader
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();


                    String line = "";

                    while ((line =  reader.readLine())!=null){
                        buffer.append(line);

                    }

                    return buffer.toString();

                }catch (MalformedURLException e){
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();

                }finally {
                    if(connection!=null) connection.disconnect();
                    try {
                        if(reader!=null) reader.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Toast.makeText(this,"onLoadFinish",Toast.LENGTH_SHORT).show();
        btn.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        bar.setVisibility(View.INVISIBLE);
        tv.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }




}