package me.jcala.xmarket.util;


import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FileUtils {

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // TODO: 16-11-30  没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), descriptionString);
    }

    /**
     * 使用RxJava异步批量压缩文件
     * 除去最后一张系统添加图片的图标
     */
    public static List<File> compressMultiFilesExceptLast(Context context,List<String> picUrls){
        List<File> files=new ArrayList<>();
        for (int i=0;i < picUrls.size()-1;i++){
            File img=new File(picUrls.get(i));
            if (img.exists()){
                Compressor.getDefault(context)
                        .compressToFileAsObservable(img)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((File file) -> {
                            files.add(file);
                        });
            }
        }
        return files;
    }


}