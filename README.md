# DLCTool4Cloudfront
This is a android library to download your private content on AWS Cloudfront
This util depends on two library: 
- Ion 2.1.9, https://github.com/koush/ion
- jets3t-0.9.4, http://jets3t.s3.amazonaws.com/index.html


# Usage
refer to the demo in project: /app/src/main/java/mobi/ovoy/dlc/demo/MainActivity.java . 


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
