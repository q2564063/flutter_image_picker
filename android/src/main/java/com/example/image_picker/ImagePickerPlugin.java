/*
 * Description: 
 * Created: 2022-09-03 15:29:53
 * --------------------------------------------
 * Author: ZZQ
 * Email: 597238072@qq.com
 * --------------------------------------------
 * Last Modified: 2022-09-29 16:41:46
 * Modified By: ZZQ
 */

package com.example.image_picker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.luck.picture.lib.basic.PictureSelectionCameraModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.SdkVersionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

/**
 * ImagePickerPlugin
 */
public class ImagePickerPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context curContext;
    private Activity curActivity;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "image_picker");
        channel.setMethodCallHandler(this);

        this.curContext = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        this.curContext = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        this.curActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        this.curActivity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        this.curActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        this.curActivity = null;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("openGallery")) {
            HashMap<String, Object> params = (HashMap<String, Object>)call.arguments;
            boolean openCameraDirectly = (boolean) params.get("openCameraDirectly");
            if (openCameraDirectly){
                openCamera(params, result);
            }else{
                openGallery(params, result);
            }
        } else {
            result.notImplemented();
        }
    }

    private void openCamera(HashMap<String, Object> params, @NonNull Result fluResult) {
        double compressImageQuality = (double) params.get("compressImageQuality");
        PictureSelectionCameraModel pictureSelectionCameraModel = PictureSelector.create(curActivity)
                .openCamera(SelectMimeType.ofImage());
        if (compressImageQuality < 1){
            pictureSelectionCameraModel.setCompressEngine(new CompressFileEngine() {
                @Override
                public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
                    Luban.with(context).load(source).ignoreBy(500)
                            .setCompressListener(new OnNewCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(String source, File compressFile) {
                                    if (call != null) {
                                        call.onCallback(source, compressFile.getAbsolutePath());
                                    }
                                }

                                @Override
                                public void onError(String source, Throwable e) {
                                    if (call != null) {
                                        call.onCallback(source, null);
                                    }
                                }
                            }).launch();
                }
            });
        }
        pictureSelectionCameraModel.forResult(new OnResultCallbackListener<LocalMedia>() {
            @Override
            public void onResult(ArrayList<LocalMedia> result) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 0);
                resultMap.put("msg", "Success");
                List<Map<String, Object>> arr = new ArrayList<>();
                for (LocalMedia media : result) {
                    arr.add(createImageInfo(media));
                }
                resultMap.put("data", arr);
                fluResult.success(resultMap);
            }

            @Override
            public void onCancel() {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("code", 1);
                resultMap.put("msg", "User cancelled image selection");
                fluResult.success(resultMap);
            }
        });

    }

    private void openGallery(HashMap<String, Object> params, @NonNull Result fluResult) {
        boolean enableMutipleSelect = (boolean) params.get("enableMutipleSelect");
        int minSelectCount = (int) params.get("minSelectCount");
        int maxSelectCount = (int) params.get("maxSelectCount");
        double compressImageQuality = (double) params.get("compressImageQuality");

        PictureSelectionModel selectionModel = PictureSelector.create(curActivity)
                .openGallery(SelectMimeType.ofImage())
                .setSelectionMode(enableMutipleSelect ? SelectModeConfig.MULTIPLE : SelectModeConfig.SINGLE)
                .setMaxSelectNum(maxSelectCount)
                .setMinSelectNum(minSelectCount)
                .setImageEngine(GlideEngine.createGlideEngine());
        if (compressImageQuality < 1) {
            selectionModel.setCompressEngine(new CompressFileEngine() {
                @Override
                public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
                    Luban.with(context).load(source).ignoreBy(500)
                            .setCompressListener(new OnNewCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(String source, File compressFile) {
                                    if (call != null) {
                                        call.onCallback(source, compressFile.getAbsolutePath());
                                    }
                                }

                                @Override
                                public void onError(String source, Throwable e) {
                                    if (call != null) {
                                        call.onCallback(source, null);
                                    }
                                }
                            }).launch();
                }
            });
        }
        selectionModel.forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("code", 0);
                        resultMap.put("msg", "Success");
                        List<Map<String, Object>> arr = new ArrayList<>();
                        for (LocalMedia media : result) {
                            arr.add(createImageInfo(media));
                        }
                        resultMap.put("data", arr);
                        fluResult.success(resultMap);
                    }

                    @Override
                    public void onCancel() {
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("code", 1);
                        resultMap.put("msg", "User cancelled image selection");
                        fluResult.success(resultMap);
                    }
                });
    }

    private Map<String, Object> createImageInfo(LocalMedia media) {
        Map<String, Object> map = new HashMap<>();
        String compressPath = media.getCompressPath();
        int width = media.getWidth();
        int height = media.getHeight();
        if (width == 0 && height == 0) {
            Map<String, Integer> size = getImageSize(compressPath);
            width = size.get("width");
            height = size.get("height");
        }
        map.put("path", compressPath);
        map.put("localIdentifier", media.getId());
        map.put("filename", media.getFileName());
        map.put("width", width);
        map.put("height", height);
        map.put("mime", media.getMimeType());
        map.put("size", media.getSize());
        map.put("creationDate", media.getDateAddedTime());
        map.put("duration", media.getDuration());
        return map;
    }

    private Map<String, Integer> getImageSize(String imageLocalPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imageLocalPath, options);
        int width = options.outWidth;
        int height = options.outHeight;

        int orientation = getImageOrientation(imageLocalPath);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
            case ExifInterface.ORIENTATION_ROTATE_270: {
                Map<String, Integer> size = new HashMap<String, Integer>();
                size.put("width", height);
                size.put("height", width);
                return size;
            }
            default: {
                Map<String, Integer> size = new HashMap<String, Integer>();
                size.put("width", width);
                size.put("height", height);
                return size;
            }
        }
    }

    private int getImageOrientation(String imageLocalPath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imageLocalPath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            return orientation;
        } catch (IOException e) {
            e.printStackTrace();
            return ExifInterface.ORIENTATION_NORMAL;
        }
    }
}
