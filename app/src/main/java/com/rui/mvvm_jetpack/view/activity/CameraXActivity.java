package com.rui.mvvm_jetpack.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.experimental.UseExperimental;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.util.concurrent.ListenableFuture;
import com.rui.mvvm_jetpack.base.BaseActivity;
import com.rui.mvvm_jetpack.databinding.ActivityCameraXBinding;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraXActivity extends BaseActivity {
    private ActivityCameraXBinding mBinding;

    public static final String CAMERAX_PHOTO_PATH = "CAMERAX_PHOTO_PATH";
    public static final String IS_LENS_FACING_BACK = "IS_LENS_FACING_BACK";
    private static final double RATIO_4_3_VALUE = 4.0 / 3.0;
    private static final double RATIO_16_9_VALUE = 16.0 / 9.0;

    private String photoFilePath;
    private Executor mainExecutor;

    private File mPhotoDir;
    private File mImageFile;
    private Display display;
    private ImageCapture imageCapture;
    private int mLensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview mPreview;
    private ImageAnalysis mImageAnalysis;
    private int mDisplayId = -1;

    /**  图片文件保存路径 (FileProvider不能将内容共享到私有文件 getExternalFilesDir) **/
    public static final String ID_PHOTO_DIR = "/Test/Images";
    //    public static final String ID_PHOTO_DIR = Environment.getExternalStorageDirectory() + "/IDVerify/Images";
    // 照片文件名字
    public static final String ID_PHOTO_NAME = "test_photo.jpg";

    public static void start(Context from) {
        Intent intent = new Intent(from, CameraXActivity.class);
        from.startActivity(intent);
    }

    @Override
    public void initViewBinding() {
        mBinding = ActivityCameraXBinding.inflate(LayoutInflater.from(this), mHostLayout, true);
    }

    @Override
    public void initData() {
        mainExecutor = ContextCompat.getMainExecutor(this);
        display = getWindowManager().getDefaultDisplay();

        boolean isLensFacingBack = getIntent().getBooleanExtra(IS_LENS_FACING_BACK, true);
        if (isLensFacingBack) {
            mLensFacing = CameraSelector.LENS_FACING_BACK;
            mBinding.idPhotoMask.setVisibility(View.VISIBLE);
        }
        else {
            mLensFacing = CameraSelector.LENS_FACING_FRONT;
            mBinding.idPhotoMask.setVisibility(View.GONE);
        }
        photoFilePath = getIntent().getStringExtra(CAMERAX_PHOTO_PATH);

        createView();
    }

    @Override
    public String setToolbarTitle() {
        return "<Camera Activity>";
    }
    @Override
    protected boolean isNeedToolbar() {
        return true;
    }

    /**
     * 监听屏幕方向变化
     **/
    DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() {
        @Override
        public void onDisplayAdded(int displayId) {}

        @Override
        public void onDisplayRemoved(int displayId) {}

        @Override
        public void onDisplayChanged(int display_id) {
            if (display_id == mDisplayId) {
                int rotation = getWindowManager().getDefaultDisplay().getRotation();
                imageCapture.setTargetRotation(rotation);
                mImageAnalysis.setTargetRotation(rotation);
            }
        }
    };

    /**
     * 定义在拍照并保存到磁盘后将会触发的回调
     **/
    ImageCapture.OnImageSavedCallback imageSavedListener = new ImageCapture.OnImageSavedCallback() {
        @Override
        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
            Log.e("HRX", "onImageSaved: " + outputFileResults.getSavedUri());
//            hideProgressBar();
            Glide.with(CameraXActivity.this)
                    .load(mImageFile)
                    .circleCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mBinding.photoViewButton);
//            setResult(RESULT_OK);
//            finish();
        }

        @Override
        public void onError(@NonNull final ImageCaptureException exception){
            hideProgressBar();
            Log.e("HRX", "ImageCaptureException: "+ exception.getMessage());
            exception.printStackTrace();
        }
    };

    ImageCapture.OnImageCapturedCallback imageCapturedCallback = new ImageCapture.OnImageCapturedCallback() {
        @Override
        @UseExperimental(markerClass = ExperimentalGetImage.class)
        public void onCaptureSuccess(@NonNull ImageProxy image) {
            image.close();
        }

        public void onError(@NonNull final ImageCaptureException exception) {
            Log.e("HRX", "ImageCaptureException: "+ exception.getMessage());
            exception.printStackTrace();
        }
    };

    private void createView() {
        // 每次设备方向改变时，都要重新计算布局
        DisplayManager mDisplayManager;
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        mDisplayManager.registerDisplayListener(displayListener, null);

        // 确定输出目录
//        outputDirectory = getOutputDirectory();

        // 每次提供的纹理视图更改时，都重新计算布局
//        mBinding.viewFinder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                updateTransform();
//            }
//        });

        // 等待视图正确布局
        mBinding.viewFinder.post(new Runnable() {
            @Override
            public void run() {
                mDisplayId = getWindowManager().getDefaultDisplay().getDisplayId();
                // 构建UI控件并绑定所有相机用例
                initCamera();
            }
        });
        mBinding.cameraCancelButton.setOnClickListener(v -> finish());
    }

    /**
     * 初始化CameraX的相关配置
     */
    @SuppressLint("RestrictedApi")
    private void initCamera() {
        ListenableFuture<ProcessCameraProvider> processCameraProvider = ProcessCameraProvider.getInstance(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        int aspectRatio = aspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels);
        int rotation = display.getRotation();

        /** 1.初始化 Preview **/
        // 根据屏幕的像素的宽高，设置预览view的宽高比
        mPreview = new Preview.Builder()
                .setTargetRotation(rotation)
                .setTargetAspectRatio(aspectRatio)
                .build();
        /** 2.初始化 ImageCapture **/
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(aspectRatio)
//                .setTargetAspectRatio(rotation)
                /** 设置旋转值，可以将摄像头旋转以下四种角度（顺时针）拍摄照片
                 *  {@link Surface#ROTATION_0}{@link Surface#ROTATION_90}{@link Surface#ROTATION_180}{@link Surface#ROTATION_270}
                 * **/
                .setTargetRotation(Surface.ROTATION_90)
                .build();
        initTakePicture();
        /** 3.初始化 ImageAnalysis **/
        mImageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetRotation(rotation)
                .setTargetAspectRatio(aspectRatio)
//                .setTargetResolution(new Size(1280, 720))
                .build();
        mImageAnalysis.setAnalyzer(mainExecutor, new MyAnalyzer());
        /** 4.相机选择器——选择前后摄像头 **/
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(mLensFacing)
                .build();
        /** 5.绑定 processCameraProvider **/
        // 使用相同的生命周期所有者将声明的配置应用于 processCameraProvider
        try {
            Camera camera = processCameraProvider.get()
                    .bindToLifecycle(this, cameraSelector, mPreview, imageCapture, mImageAnalysis);
            mPreview.setSurfaceProvider(mBinding.viewFinder.createSurfaceProvider(camera.getCameraInfo()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新变化
     */
    private void updateTransform() {
        Matrix matrix = new Matrix();
        // 计算取景器的中心
        float centerX = mBinding.viewFinder.getWidth() / 2f;
        float centerY = mBinding.viewFinder.getHeight() / 2f;

        float[] rotations = {0,90,180,270};
        // 纠正预览输出以适应显示旋转
        float rotationDegrees = rotations[mBinding.viewFinder.getDisplay().getRotation()];

        matrix.postRotate(-rotationDegrees, centerX, centerY);

        // 将转换应用于我们的TextureView
//        mBinding.viewFinder.setTransform(matrix);
    }

    /**
     *  初始化拍照功能
     */
    private void initTakePicture() {
        mBinding.cameraCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置图像捕获元数据
                ImageCapture.Metadata metadata = new ImageCapture.Metadata();
                // 使用前置摄像头时镜像
                    metadata.setReversedHorizontal(mLensFacing == CameraSelector.LENS_FACING_FRONT);
                // 创建输出文件以保存图像
//                if (photoFilePath == null || TextUtils.isEmpty(photoFilePath)) {
//                    Log.e("HRX", "onClick:photoFilePath is null ");
////                    return;
//                } else
//                    {
//                    File photoFile = new File(photoFilePath);
                    mImageFile = getOutputFile(ID_PHOTO_NAME);
                    // 设置拍摄照片后触发的图像捕获监听器
                    ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions
                            .Builder(mImageFile)
                            .setMetadata(metadata).build();
//                }
//                showProgressBar();
//                imageCapture.takePicture(mainExecutor, imageCapturedCallback);
                imageCapture.takePicture(outputFileOptions, mainExecutor, imageSavedListener);
            }
        });
    }

    /**
     * 需要枚举值
     * 目前，它的值为4：3或者16：9
     * 通过将预览比率的绝对值计数为提供的值之一，来检测@params中提供的尺寸的最合适比率。
     */
    private int aspectRatio(int width, int height) {
        double previewRatio = Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private class MyAnalyzer implements ImageAnalysis.Analyzer {
        @Override
        @ExperimentalGetImage
        public void analyze(ImageProxy imageProxy) {
            final Image image = imageProxy.getImage();
            if (image != null) {
                imageProxy.close();
//                Log.d("HRX", image.getWidth() + "," + image.getHeight());
//                imageView.input(image);
            }
        }
    }

    /**
     * 使用外部媒体（如果可用），否则使用我们应用程序的文件目录
     */
    private File getOutputFile(String fileName) {
        Context appContext = getApplicationContext();
        File mediaDir = appContext.getExternalMediaDirs()[0];
        if (!mediaDir.exists()) {
            mediaDir.mkdirs();
        }
        mPhotoDir = new File(mediaDir, ID_PHOTO_DIR);
        if (!mPhotoDir.exists()) {
            mPhotoDir.mkdirs();
        }
        if (mPhotoDir != null) {
//            Log.e("HRX", "photoFilePath 1: " + mPhotoDir);
            return new File(mPhotoDir, fileName);
        } else {
            mPhotoDir = appContext.getFilesDir();
//            Log.e("HRX", "photoFilePath 2: " + mPhotoDir);
            return new File(mPhotoDir, fileName);
        }
    }
}
