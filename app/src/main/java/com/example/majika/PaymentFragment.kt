package com.example.majika

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import com.example.majika.retrofit.MajikaAPI
import com.example.majika.retrofit.RetrofitClient
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.json.JSONException
import java.lang.Runnable
import java.nio.ByteBuffer


interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun qrCodeNotFound()
}

class QRCodeImageAnalyzer(listener: QRCodeFoundListener) : ImageAnalysis.Analyzer {
    private val listener: QRCodeFoundListener

    init {
        this.listener = listener
    }

    override fun analyze(image: ImageProxy) {
        if (image.format == YUV_420_888 || image.format == YUV_422_888 || image.format == YUV_444_888) {
            val byteBuffer: ByteBuffer = image.planes[0].buffer
            val imageData = ByteArray(byteBuffer.capacity())
            byteBuffer.get(imageData)
            val source = PlanarYUVLuminanceSource(
                imageData,
                image.width, image.height,
                0, 0,
                image.width, image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = QRCodeMultiReader().decode(binaryBitmap)
                listener.onQRCodeFound(result.getText())
            } catch (e: FormatException) {
                listener.qrCodeNotFound()
            } catch (e: ChecksumException) {
                listener.qrCodeNotFound()
            } catch (e: NotFoundException) {
                listener.qrCodeNotFound()
            }
        }
        image.close()
    }
}

class PaymentFragment : Fragment() {
    private var price = 0
    private lateinit var toolbarMajika: Toolbar
    private lateinit var toolbarMajikaText: TextView

    private var preview: Preview? = null
    private lateinit var myViewFinder: PreviewView
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    private lateinit var safeContext: Context
    private lateinit var tvResult: TextView

    private var qrCode: String? = null
    private lateinit var qrResult: ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        toolbarMajika = view.findViewById(R.id.majikaToolbar)
        toolbarMajikaText = toolbarMajika.findViewById(R.id.majikaToolbarTitle)
        toolbarMajika.title = "Pembayaran"
        toolbarMajikaText.setText(toolbarMajika.title)
        (activity as AppCompatActivity).setSupportActionBar(toolbarMajika)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        myViewFinder = view.findViewById(R.id.qrViewFinder)
        tvResult = view.findViewById(R.id.qrPrice)
        qrResult = view.findViewById(R.id.qrResult)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        @JvmStatic
        fun newInstance(price: Int) = PaymentFragment().apply {
            arguments = Bundle().apply {
                putInt("Price", price)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        price = arguments?.getInt("Price") ?: 0

        if (allPermissionsGranted()) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        //        OpenCVLoader.initDebug()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            this.context?.let { ContextCompat.getMainExecutor(it) }?.let {
                imageAnalysis.setAnalyzer(
                    it,
                    QRCodeImageAnalyzer(object : QRCodeFoundListener {
                        //TODO: UBAH LOGIC JADI NEMBAK API
                        override fun onQRCodeFound(_qrCode: String?) {
                            var status : String = "FAILED"
                            val quotesApi = RetrofitClient.getInstance().create(MajikaAPI::class.java)

                            val statusResult = CoroutineScope(IO).async {
                                val result = _qrCode?.let { it1 -> quotesApi.pay(it1) }
                                if (result != null){
                                    status = result.body()?.status.toString()
                                    Log.d("HASIL: ", status)
                                }
                            }

                            runBlocking(IO) {
                                statusResult.await()
                            }

                            if(status == "SUCCESS"){
                                Log.d("QR: ", status)
                                cameraProvider.unbindAll()
                                qrResult.setImageDrawable(ContextCompat.getDrawable(safeContext, R.drawable.berhasil_no_backgroud))
                                qrResult.visibility = View.VISIBLE
                                Handler().postDelayed({
//                                    openCamera() TODO: REROUTE TO MENU
                                }, 5000)
                            }
                            else{
                                Log.d("QR: ", status)
                                cameraProvider.unbindAll()
                                qrResult.setImageDrawable(ContextCompat.getDrawable(safeContext, R.drawable.gagal_no_backgroud))
                                qrResult.visibility = View.VISIBLE
                                Handler().postDelayed({
                                    openCamera()
                                }, 500)

                            }


                        }

                        override fun qrCodeNotFound() {
                        }
                    })
                )
            }

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
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                //ASLI
//                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
                //ANALYSER DI KOMEN
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview, imageCapture)
                preview?.setSurfaceProvider(myViewFinder?.createSurfaceProvider())
            } catch (exc: Exception) {
                Log.e(CameraFragment.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null)
        {
            //if qrcode has nothing in it
            if (result.contents == null){
                Toast.makeText(safeContext, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                //if qr contains data
                try {
                    val contents = result.contents
                    Toast.makeText(safeContext, contents, Toast.LENGTH_LONG).show()
                    tvResult.text = contents
                }
                catch (e: JSONException) {
                    e.printStackTrace()
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(safeContext, result.contents, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}