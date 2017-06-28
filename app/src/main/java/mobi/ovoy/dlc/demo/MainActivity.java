package mobi.ovoy.dlc.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ProgressCallback;

import org.jets3t.service.security.EncryptionUtil;

import java.io.File;
import java.io.InputStream;

import mobi.ovoy.dlc.sdk.Ion4PrivateCDN;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_main);

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(mContext.getResources().getString(R.string.button_downloading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(false);

    }
    public void testDLC(View view) {

        //get the private key which you will use it to sign url
        byte[] privatekey = null;
        try{
            InputStream is = getResources().openRawResource(R.raw.yourpk);
            privatekey = EncryptionUtil.convertRsaPemToDer(is);
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"get private key failure,ex:"+ex.toString());
        }


        if(privatekey == null){
            Log.e(TAG,"testDLC failure,can't find private key");
            return;
        }

        Ion4PrivateCDN.with(this, "http://xxxxxx.cloudfront.net/your_file_path",privatekey)
                .progressDialog(mProgressDialog)
                .progressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        if(mProgressDialog != null) {
                            mProgressDialog.show();
                        }
                    }
                })
                .write(new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "my_download_file"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        // download done...
                        // do stuff with the File or error
                        Log.d(TAG,"DLC downloaded!");
                        mProgressDialog.hide();
                    }
                });

    }


}
