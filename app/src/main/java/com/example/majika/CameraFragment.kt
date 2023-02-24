package com.example.majika

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
//    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var currentCamera: Int = CameraSelector.LENS_FACING_FRONT
    private lateinit var myViewFinder: PreviewView

    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

//    private fun getStatusBarHeight(): Int {
//        val resourceId = safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
//        return if (resourceId > 0) {
//            safeContext.resources.getDimensionPixelSize(resourceId)
//        } else 0
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)

    }

    companion object {
        val TAG = "CameraXFragment"
//        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        var isOffline = false // prevent app crash when goes offline
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajikaText.setText("Twibbon")
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)

        myViewFinder = view.findViewById(R.id.viewFinder)
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Setup the listener for take photo button
        val cameraCaptureButton: ImageView = view.findViewById(R.id.captureButton)
        cameraCaptureButton.setOnClickListener { takePhoto() }

        val changeCameraButton: ImageView = view.findViewById(R.id.changeCamera)
        changeCameraButton.setOnClickListener { changeCamera() }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
//        cameraExecutor = Executors.newCachedThreadPool()
    }

    private fun startCamera() {
//        OpenCVLoader.initDebug()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

//            imageAnalyzer = ImageAnalysis.Builder().build().apply {
//                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
//                    val bitmap = viewFinder.bitmap
//                    val img = Mat()
//                    Utils.bitmapToMat(bitmap, img)
//                    bitmap?.recycle()
//                    // Do image analysis here if you need bitmap
//                })
//            }
            // Select back camera
            val cameraSelector = CameraSelector.Builder().requireLensFacing(currentCamera).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                preview?.setSurfaceProvider(myViewFinder?.createSurfaceProvider())
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))

    }

    private fun takePhoto() {
        val now = Date()
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath: String =
                outputDirectory.toString() + "/" + now + ".jpg"

            // create bitmap screen capture
            val v1 :PreviewView = myViewFinder
            val twibbon: ImageView =  view?.findViewById(R.id.twibbon) ?: return
            v1.isDrawingCacheEnabled = true
            val bitmap1 = v1.bitmap
            val bitmap2 = twibbon.drawToBitmap()
            val bitmap = bitmap1?.let { overlayTwibbon(it, bitmap2) }
            v1.isDrawingCacheEnabled = false
            val imageFile = File(mPath)
            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }
            outputStream.flush()
            outputStream.close()
//            openScreenshot(imageFile)
            val savedUri = Uri.fromFile(imageFile)
            val msg = "Photo capture succeeded: $savedUri"
            Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
            val shownBitmap = bitmap?.let { addWhiteBorder(it, 10) }
            val shownImage = view?.findViewById<ImageView>(R.id.twibbonresult)
            shownImage?.setImageBitmap(shownBitmap)
            if (shownImage != null) {
                shownImage.visibility = View.VISIBLE
                Handler().postDelayed(Runnable { shownImage.visibility = View.GONE }, 2000)
            }
            val changeText: TextView = requireView().findViewById(R.id.textView)
            changeText.setText("Take Again?")
            // Log.d(TAG, msg)
        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }
    }

    fun overlayTwibbon(image: Bitmap, Twibbon: Bitmap): Bitmap {
        val bmOverlay = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig())
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(image, Matrix(), null)
        canvas.drawBitmap(Twibbon, Matrix(), null)
        return bmOverlay
    }

    private fun addWhiteBorder(bmp: Bitmap, borderSize: Int): Bitmap? {
        val bmpWithBorder =
            Bitmap.createBitmap(bmp.width + borderSize * 2, bmp.height + borderSize * 2, bmp.config)
        val canvas = Canvas(bmpWithBorder)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bmp, borderSize.toFloat(), borderSize.toFloat(), null)
        return bmpWithBorder
    }

    fun changeCamera(){
        currentCamera = if (currentCamera == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(safeContext, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }


//    private class CornerAnalyzer(private val listener: CornersListener) : ImageAnalysis.Analyzer {
//
//        private fun ByteBuffer.toByteArray(): ByteArray {
//            rewind()    // Rewind the buffer to zero
//            val data = ByteArray(remaining())
//            get(data)   // Copy the buffer into a byte array
//            return data // Return the byte array
//        }
//
//        @SuppressLint("UnsafeExperimentalUsageError")
//        override fun analyze(imageProxy: ImageProxy) {
//            if (!isOffline) {
//                listener()
//            }
//            imageProxy.close() // important! if it is not closed it will only run once
//        }
//
//    }
}