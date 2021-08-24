package com.moh.moclock.MoTensorFlowLite;

import android.app.Activity;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.List;

public class MoImageClassifier  {


    // designed specifically for one file
    private final String MODEL_PATH = "detect.tflite";
    private final String LABELS_PATH = "labelmap.txt";


    private static final int NUM_DETECTIONS = 10;
    // Float model
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;
    // Number of threads in the java app
    private static final int NUM_THREADS = 4;
    private boolean isModelQuantized;
    // Config values.
    private int inputSize;
    private int[] intValues;
    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private float[][][] outputLocations;
    // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the classes of detected boxes
    private float[][] outputClasses;
    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private float[][] outputScores;
    // numDetections: array of shape [Batchsize]
    // contains the number of detected boxes
    private float[] numDetections;

    private ByteBuffer imgData;

    private Interpreter tfLite;
    private MappedByteBuffer tfliteModel;
    private Interpreter.Options tfliteOptions = new Interpreter.Options();
    private List<String> labels;

    public MoImageClassifier(Activity activity,int numThreads, int inputSize,boolean isModelQuantized) throws IOException {
        tfliteModel = FileUtil.loadMappedFile(activity, MODEL_PATH);
        tfliteOptions.setNumThreads(numThreads);
        tfLite = new Interpreter(tfliteModel, tfliteOptions);
        labels = FileUtil.loadLabels(activity, LABELS_PATH);
        this.inputSize =  inputSize;
        tfLite.setNumThreads(NUM_THREADS);
        this.intValues = new int[inputSize * inputSize];
        outputLocations = new float[1][NUM_DETECTIONS][4];
        outputClasses = new float[1][NUM_DETECTIONS];
        outputScores = new float[1][NUM_DETECTIONS];
        numDetections = new float[1];

        this.isModelQuantized = isModelQuantized;

        int numBytesPerChannel;
        if (isModelQuantized) {
            numBytesPerChannel = 1; // Quantized
        } else {
            numBytesPerChannel = 4; // Floating point
        }
        imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * numBytesPerChannel);
        imgData.order(ByteOrder.nativeOrder());
    }

//    public List<Recognition> recognizeImage(final Bitmap bitmap) {
//        if (bitmap == null){
//            System.out.println("=============Bitmap was null=================");
//            return new ArrayList<>();
//        }
//        // Log this method so that it can be analyzed with systrace.
//        Trace.beginSection("recognizeImage");
//
//        Trace.beginSection("preprocessBitmap");
//        // Preprocess the image data from 0-255 int to normalized float based
//        // on the provided parameters.
////        bitmap.getPixels(intValues,
////                0, bitmap.getWidth(),
////                0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//        bitmap.getPixels(intValues,
//                0, inputSize,
//                0, 0, inputSize, inputSize);
//
//        imgData.rewind();
//        for (int i = 0; i < inputSize; ++i) {
//            for (int j = 0; j < inputSize; ++j) {
//                int pixelValue = intValues[i * inputSize + j];
//                if (isModelQuantized) {
//                    // Quantized model
//                    imgData.put((byte) ((pixelValue >> 16) & 0xFF));
//                    imgData.put((byte) ((pixelValue >> 8) & 0xFF));
//                    imgData.put((byte) (pixelValue & 0xFF));
//                } else { // Float model
//                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
//                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
//                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
//                }
//            }
//        }
//        Trace.endSection(); // preprocessBitmap
//
//        // Copy the input data into TensorFlow.
//        Trace.beginSection("feed");
//        outputLocations = new float[1][NUM_DETECTIONS][4];
//        outputClasses = new float[1][NUM_DETECTIONS];
//        outputScores = new float[1][NUM_DETECTIONS];
//        numDetections = new float[1];
//
//        Object[] inputArray = {imgData};
//        Map<Integer, Object> outputMap = new HashMap<>();
//        outputMap.put(0, outputLocations);
//        outputMap.put(1, outputClasses);
//        outputMap.put(2, outputScores);
//        outputMap.put(3, numDetections);
//        Trace.endSection();
//
//        // Run the inference call.
//        Trace.beginSection("run");
//        tfLite.runForMultipleInputsOutputs(inputArray, outputMap);
//        Trace.endSection();
//
//        // Show the best detections.
//        // after scaling them back to the input size.
//        final ArrayList<Recognition> recognitions = new ArrayList<>(NUM_DETECTIONS);
//        for (int i = 0; i < NUM_DETECTIONS; ++i) {
//            final RectF detection =
//                    new RectF(
//                            outputLocations[0][i][1] * inputSize,
//                            outputLocations[0][i][0] * inputSize,
//                            outputLocations[0][i][3] * inputSize,
//                            outputLocations[0][i][2] * inputSize);
//            // SSD Mobilenet V1 Model assumes class 0 is background class
//            // in label file and class labels start from 1 to number_of_classes+1,
//            // while outputClasses correspond to class index from 0 to number_of_classes
//            int labelOffset = 1;
//            recognitions.add(
//                    new Recognition(
//                            "" + i,
//                            labels.get((int) outputClasses[0][i] + labelOffset),
//                            outputScores[0][i],
//                            detection));
//        }
//        Trace.endSection(); // "recognizeImage"
//        return recognitions;
//    }





}
