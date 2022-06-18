package com.sample.camera;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;

public class SpaceRegion {

    private String secretAccessKey = "<digital ocean spaces secret access key>";
    private String accessKeyId = "<digital ocean spaces access key id>";
    private String spacename = "<digital ocean spaces name>";
    private String space_endpoint = "<digital ocean spaces end point>";
    TransferUtility transferUtility;
    Context context;
    Activity activity;
    AmazonS3Client amazonS3Client;
    String bucketName;

    public void init(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        AWSCredentialsProvider credentialsProvider = new StaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));
        amazonS3Client = new AmazonS3Client(credentialsProvider);
        amazonS3Client.setEndpoint(space_endpoint);
        transferUtility = new TransferUtility(amazonS3Client, context);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                bucketName = amazonS3Client.listBuckets().get(0).getName();
            }
        });
    }

    public void uploadFile(File file){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(getClass().getName(), "Called AWS");
                final TransferObserver observer = transferUtility.upload(
                        bucketName,  //this is the bucket name on S3
                        "test-images/sample_file.jpg", //this is the path and name
                        file, //path to the file locally
                        CannedAccessControlList.PublicRead //to make the file public
                );
                Log.d(getClass().getName(), "observer :: "+observer.getState());

                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        Log.d(getClass().getName(), "state : "+state.name()+" -- "+id);
                        if (state.equals(TransferState.COMPLETED)) {
                            //Success
                           Log.d(getClass().getName(), ""+space_endpoint+ bucketName+"test-images/sample_file.jpg");
                        } else if (state.equals(TransferState.FAILED)) {
                            //Failed
                        }

                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        Log.d(getClass().getName(), "ID: "+id+", bytesCurrent: "+bytesCurrent+", bytesTotal: "+bytesTotal);
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        Log.d(getClass().getName(), "onError :: "+ex.getMessage());
                    }
                });
            }
        });
    }

}
