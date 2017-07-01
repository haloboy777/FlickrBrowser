package me.haloboy777.flickerbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by haloboy777 on 6/26/17.
 */
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIAISED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String>{
    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }
    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallback;

    public GetRawData(OnDownloadComplete callback){
        Log.d(TAG, "GetRawData: #1");
        this.mCallback = callback;
        this.mDownloadStatus = DownloadStatus.IDLE;
        Log.d(TAG, "GetRawData: #2");
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");
//        onPostExecute(doInBackground(s));
        if(mCallback != null){
            mCallback.onDownloadComplete(doInBackground(s), mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: #3");
//        Log.d(TAG, "onPostExecute: parameter:"+ s);
        if (mCallback!=null){
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
        Log.d(TAG, "onPostExecute: #4");
//        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground: #9");
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if(params == null){
            mDownloadStatus = DownloadStatus.NOT_INITIAISED;
            return null;
        }
        try{
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code is : "+response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null!=(line = reader.readLine())){
                result.append(line).append('\n');
            }
            mDownloadStatus = DownloadStatus.OK;
            Log.d(TAG, "doInBackground: #10");
            return result.toString();

        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+e.getMessage() );
        } catch (IOException e){
            Log.e(TAG, "doInBackground: IO exception reading data: "+e.getMessage() );
        } catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception? Need Permision: "+ e.getMessage() );
        } finally {
            if (connection!=null){
                connection.disconnect();
            }
            if (reader!=null){
                try {
                    reader.close();
                } catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing Stream" + e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        Log.d(TAG, "doInBackground: #10");
        return null;
    }


}
