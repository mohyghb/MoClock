package com.moh.moclock;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.moh.moclock.MoTensorFlowLite.MoImageClassifier;

import com.moh.moclock.R;

public class MoObjectDetectorActivity extends AppCompatActivity {


    private static final int INPUT_SIZE = 300;
    private static final boolean IS_QUANTIZED = true;

    private MoImageClassifier classifier;
    private TextureView textureView;
    private CameraDevice cameraDevice;
    private String cameraId;

    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSession;

    private Size dimen;
    private int orgPreviewWidth;
    private int orgPreviewHeight;
    private int DSI_height, DSI_width;

    private int finalWidth = 0;
    private int finalHeight = 0;

    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;
    private Runnable postInferenceCallback;
    private Runnable imageConverter;

    private ImageReader imageReader;
    private DisplayMetrics displayMetrics;

    private ImageView imageView;

    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_object_detector);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void init() throws IOException {
        this.classifier = new MoImageClassifier(this, 4,INPUT_SIZE,IS_QUANTIZED);
        this.imageView = findViewById(R.id.image_view_detect);
        this.textureView = findViewById(R.id.textureView_detector);
        this.textureView.setSurfaceTextureListener(surfaceTextureListener);



    }

    private void setAspectRatioTextureView(int ResolutionWidth , int ResolutionHeight )
    {
        if(ResolutionWidth > ResolutionHeight){
            int newWidth = DSI_width;
            int newHeight = ((DSI_width * ResolutionWidth)/ResolutionHeight);
            updateTextureViewSize(newWidth,newHeight);

        }else {
            int newWidth = DSI_width;
            int newHeight = ((DSI_width * ResolutionHeight)/ResolutionWidth);
            updateTextureViewSize(newWidth,newHeight);
        }

    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        this.finalWidth = viewWidth;
        this.finalHeight = viewHeight;
        //Log.d(TAG, "TextureView Width : " + viewWidth + " TextureView Height : " + viewHeight);
        System.out.println("----------------------------this happend " + viewWidth + "," + viewHeight);
        textureView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
    }


    private void initCamera() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;
        this.cameraId = cameraManager.getCameraIdList()[0];

        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap streamConfigurationMap =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        assert streamConfigurationMap != null;
        dimen = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DSI_height = displayMetrics.heightPixels;
        DSI_width = displayMetrics.widthPixels;
        setAspectRatioTextureView(dimen.getWidth(),dimen.getHeight());



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},10);
            return;
        }
        cameraManager.openCamera(cameraId, stateCallBack, null);


    }

    CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice d) {
            cameraDevice = d;
            try {
                startCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void startCameraPreview() throws CameraAccessException {
        // for getting previews
        //List<Surface> surfaces = new ArrayList<>();
        imageReader = ImageReader.newInstance(
                finalWidth,
                finalHeight,
                ImageFormat.YUV_420_888,
                2);

        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(finalWidth,finalHeight);
        Surface surface = new Surface(surfaceTexture);
        Surface frameCaptureSurface = imageReader.getSurface();


        //surfaces.add(surface);
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);
        // for getting previews
        //surfaces.add(frameCaptureSurface);
        captureRequestBuilder.addTarget(frameCaptureSurface);

        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = null;

                try {
                    image = imageReader.acquireLatestImage();
//        Log.d(TAG, String.format(Locale.getDefault(), "image w = %d; h = %d", image.getWidth(), image.getHeight()));
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    buffer.rewind();
                    byte[] data = new byte[buffer.capacity()];
                    buffer.get(data);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if(bitmap==null){
                        System.out.println("===================Bitmap Null========================");
                    }else{
                        System.out.println("===================Bitmap OKAY========================");
                    }
//use your bitmap
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (image != null) {
                    image.close();
                }
                //onImageChanged(imageReader);
//                Image image =  imageReader.acquireNextImage();
//                if(image!=null) {
//                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                    byte[] bytes = new byte[buffer.remaining()];
//                    buffer.get(bytes);
//                    Bitmap myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//                    Recognition.printAll(classifier.recognizeImage(myBitmap));
//
//                    // process this image
//                    //System.out.println("==================this is called=============");
//                    image.close();
//                }
            }
        },null);

//        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE);





        cameraDevice.createCaptureSession(Arrays.asList(surface,frameCaptureSurface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession s) {
                if(cameraDevice == null)
                    return;
                cameraCaptureSession = s;
                updatePreview();
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                System.out.println("===============configuration changed==============");
            }
        },null);
    }


    private void onImageChanged(ImageReader reader) {

    }

    protected void fillBytes(final Image.Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                //LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    private void updatePreview() {
        if (cameraDevice == null)
            return;
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
       // captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }




    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                initCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };



    Bitmap fromByteBuffer(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes, 0, bytes.length);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }






}
