package mobi.ovoy.dlc.sdk;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;


import java.io.FileInputStream;

import org.jets3t.service.CloudFrontService;
import org.jets3t.service.utils.ServiceUtils;
import org.jets3t.service.security.EncryptionUtil;
/**
 * Created by Sam Chiu on 2017/2/10.
 */


public class Ion4PrivateCDN {
    private static final String TAG = "Ion4PrivateCDN";

    //define customs header if need
    private static final String CONST_CUSTOMS_HEADER = "my_customs_header";
    private static final String CONST_CUSTOMS_HEADER_VALUE = "*";

    //provide AWS Cloudfront key id and key path
    private static final String CONST_CLOUDFRONT_KEYPATH="pk-APKAIIBH727I4Y22H5UQ.pem";
    private static String CONST_CLOUDFRONT_KEYID="APKAIIBH727I4Y22H5UQ";
    private static String cloudfront_keypath = CONST_CLOUDFRONT_KEYPATH;

    public static void setCloudfrontKeyPath(String keypath){
        cloudfront_keypath = keypath;
    }
    public static void setCloudfrontKeyId(String keyid){
        CONST_CLOUDFRONT_KEYID = keyid;
    }

    public static Builders.Any.B with(Context context,String url) {
        String signedUrl =getSignedUrl(url);
        return Ion.with(context).load(signedUrl).addHeader(CONST_CUSTOMS_HEADER, CONST_CUSTOMS_HEADER_VALUE);
    }
    public static Builders.Any.B with(Context context,String url,byte[] privatekey) {
        String signedUrl =getSignedUrl(url,privatekey);
        return Ion.with(context).load(signedUrl).addHeader(CONST_CUSTOMS_HEADER, CONST_CUSTOMS_HEADER_VALUE);
    }

    private static String getSignedUrl(String source_url,byte[] privatekey){
        //Convert an RSA PEM private key file to DER bytes
        String signedUrlCanned="";
        try{

            //Generate a "canned" signed URL to allow access to a specific distribution and object
            signedUrlCanned = CloudFrontService.signUrlCanned(
                    source_url, // Resource URL or Path
                    CONST_CLOUDFRONT_KEYID,     // Certificate identifier, an active trusted signer for the distribution
                    privatekey, // DER Private key data
                    ServiceUtils.parseIso8601Date("2030-12-14T22:20:00.000Z") // DateLessThan
            );
        }catch(Exception ex){
            Log.e(TAG,"getSignedUrl failure,ex:"+ex.toString());
        }

        Log.d(TAG,"signedUrlCanned:"+signedUrlCanned);
        return signedUrlCanned;
    }

    private static String getSignedUrl(String source_url){

        byte[] derPrivateKey=null;

        try{
            derPrivateKey = EncryptionUtil.convertRsaPemToDer(
                    new FileInputStream(cloudfront_keypath));
        }catch(Exception ex){
            Log.e(TAG,"getSignedUrl failure,ex:"+ex.toString());
        }

        return getSignedUrl(source_url, derPrivateKey);

    }


}
