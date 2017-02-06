package com.bugchain.asynctaskloaderdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    private static final int TASK_ID = 1;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = (TextView)findViewById(R.id.textResult);

        getSupportLoaderManager().initLoader(TASK_ID,null,this);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if(id == TASK_ID){
            return new MyAsyncTaskLoader(MainActivity.this,textResult);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if(loader.getId() == TASK_ID){
            textResult.setText(String.valueOf(data));
        }

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {}


    static class MyAsyncTaskLoader extends AsyncTaskLoader<Object>{

        private Activity activity;
        private int result;
        private TextView textResult;

        MyAsyncTaskLoader(Activity activity,TextView textResult) {
            super(activity);
            this.activity = activity;
            this.textResult = textResult;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if(result > 0){
                deliverResult(result);
            }
            forceLoad();
        }

        @Override
        public Integer loadInBackground() {
            for(int i=1;i<=100;i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result += i;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textResult.setText(String.valueOf(result));
                    }
                });
            }
            return result;
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();
        }
    }
}
