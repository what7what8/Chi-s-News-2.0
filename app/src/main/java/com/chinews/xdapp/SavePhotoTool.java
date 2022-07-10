package com.chinews.xdapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SavePhotoTool {
    /**
     * android 11及以上保存图片到相册
     *
     * @param context
     * @param image
     */
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void saveImageToGalleryOnVersionR(Context context, Bitmap image, long time, String folder, String name) {
        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, Environment.DIRECTORY_DOWNLOADS
                + File.separator + folder); //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".png");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATE_ADDED, time / 1000);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time / 1000);
        values.put(MediaStore.MediaColumns.DATE_EXPIRES, (time + DateUtils.DAY_IN_MILLIS) / 1000);
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        ContentResolver resolver = context.getContentResolver();
        final Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        //try {
        //    context.startIntentSender(
        //            MediaStore.createWriteRequest
        //                    (resolver, Collections.singletonList(uri))
        //                    .getIntentSender(),
        //            null,0,0,0);
        //} catch (IntentSender.SendIntentException e) {
        //    e.printStackTrace();
        //}
        try {
            // First, write the actual data for our screenshot
            try (OutputStream out = resolver.openOutputStream(uri)) {
                if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw new IOException("Failed to compress");
                }
            }
            // Everything went well above, publish it!
            values.clear();
            values.put(MediaStore.MediaColumns.IS_PENDING, 0);
            values.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
            resolver.update(uri, values, null, null);
        } catch (IOException e) {
            resolver.delete(uri, null);
            e.printStackTrace();
        }
    }

    private void saveImageToGalleryOnVersionQ(Context context, Bitmap image, long time, String folder, String name) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, bytes);
        //File path = new File(, Environment.DIRECTORY_DCIM);
        try {
            File fd = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath(), folder);
            fd.mkdir();
            File f = new File(fd.getPath(), name + ".png");
            try (FileOutputStream fo = new FileOutputStream(f)) {
                fo.write(bytes.toByteArray());
            }
            f.setLastModified(time);
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        f.getAbsolutePath(), name, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getAbsolutePath())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveImageToGallery(Context context, Bitmap image, long time, String folder, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            saveImageToGalleryOnVersionR(context, image, time, folder, name);
        } else {
            saveImageToGalleryOnVersionQ(context, image, time, folder, name);
        }
    }
}
