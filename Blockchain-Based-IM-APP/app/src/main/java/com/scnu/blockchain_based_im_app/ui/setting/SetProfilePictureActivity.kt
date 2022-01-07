package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import kotlinx.android.synthetic.main.activity_set_profile_picture.*
import java.io.ByteArrayOutputStream
import java.io.File

import android.os.Environment
import android.util.Base64
import android.widget.Button
import android.widget.Toast
import com.scnu.blockchain_based_im_app.R


class SetProfilePictureActivity : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
    private val fromAlbum = 1
    private val takePhoto = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    //-----------------------------------------------------------------------------------
    //private var photo: Bitmap? = null
    //var ii:Int=0

    /*companion object {
        /* 头像文件 */
        private const val IMAGE_FILE_NAME = "temp_head_image.jpg"

        /* 请求识别码 */
        private const val CODE_GALLERY_REQUEST = 11
        private const val CODE_CAMERA_REQUEST = 12
        private const val CODE_RESULT_REQUEST = 13

        // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。（生成bitmap貌似有时要报错？可试下把大小弄小点）
        private const val output_X = 480
        private const val output_Y = 480
    }*/

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity_set_profile_picture)

        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=?", arrayOf(MainActivity.userID))
        if (cursor.moveToFirst()) {
            val userPhotoByteChar: ByteArray =
                cursor.getBlob(cursor.getColumnIndex("profile_photo"))
            val bitmap: Bitmap =
                BitmapFactory.decodeByteArray(userPhotoByteChar, 0, userPhotoByteChar.size)
            currentProfilePhoto.setImageBitmap(bitmap)
        }
        cursor.close()

        btn_set_photo_from_album.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)


            //------------------------------------------------------
            //choseHeadImageFromGallery()
        }

        btn_take_photo.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this,
                    "com.scnu.blockchain_based_im_app.ui.setting.fileprovider",
                    outputImage
                );
            } else {
                Uri.fromFile(outputImage);
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }

        Return.setOnClickListener {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // 用户没有进行有效的设置操作，返回
        /*if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "已取消更改", Toast.LENGTH_LONG).show()
            return
        }*/
        super.onActivityResult(requestCode, resultCode, data)
        val db = dbHelper.writableDatabase
        when (requestCode) {
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        val bitmap = getBitmapFromUri(uri)
                        currentProfilePhoto.setImageBitmap(bitmap)
                        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        db.execSQL(
                            "update user set profile_photo=? where id=?",
                            arrayOf(baos.toByteArray(), MainActivity.userID)
                        )
                    }
                }
            }
            takePhoto -> {
                // 将拍摄的照片显示出来
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                currentProfilePhoto.setImageBitmap(rotateIfRequired(bitmap))
                val baos: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                db.execSQL(
                    "update user set profile_photo=? where id=?",
                    arrayOf(baos.toByteArray(), MainActivity.userID)
                )
            }

            //------------------------

            //SetProfilePictureActivity.Companion.CODE_RESULT_REQUEST -> intent?.let {
            //Toast.makeText(this,"JDGG",Toast.LENGTH_SHORT).show()
            //没进来
            //setImageToHeadView(it) }

            //SetProfilePictureActivity.Companion.CODE_GALLERY_REQUEST -> cropRawPhoto(intent!!.data)

        }
        //-----------------------------------------------------------
        //super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }
}
//
//
//    //-________-------__________________________________________________________
//    /*fun cropRawPhoto(uri: Uri?) {
//
//        //ii++
//
//        val intent = Intent("com.android.camera.action.CROP")
//        intent.setDataAndType(uri, "image/*")
//        intent.putExtra("crop", "true")
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1)
//        intent.putExtra("aspectY", 1)
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 250)
//        intent.putExtra("outputY", 250)
//        // 图片格式
//        intent.putExtra("outputFormat", "JPEG")
//        intent.putExtra("noFaceDetection", true) // 取消人脸识别
//        intent.putExtra("return-data", true)
//
//        //ii++
//        setImageToHeadView(intent)
//
//        //startActivityForResult(intent, SetProfilePictureActivity.Companion.CODE_RESULT_REQUEST)
//    }
//
//    /**
//     * 提取保存裁剪之后的图片数据，并设置头像部分的View
//     */
//    private fun setImageToHeadView(intent: Intent) {
//        val extras = intent.extras
//        if (extras != null) {
//            photo = extras.getParcelable("data")
//            currentProfilePhoto.setImageBitmap(photo)
//        }
//    }
//
//    // 将图片转换成base64编码
//    fun getBase64(bitmap: Bitmap): String {
//        val out = ByteArrayOutputStream()
//        //压缩的质量为60%
//        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out)
//        //生成base64字符
//        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
//    }
//
//
//    // 从本地相册选取图片作为头像
//    private fun choseHeadImageFromGallery() {
//
//        //val intent = Intent(Intent.ACTION_GET_CONTENT)
//        //intent.type = "image/*"
//        //startActivityForResult(intent, fromAlbum)
//
//        val intentFromGallery = Intent(Intent.ACTION_GET_CONTENT)
//        // 设置文件类型
//        intentFromGallery.type = "image/*"
//        startActivityForResult(intentFromGallery, SetProfilePictureActivity.Companion.CODE_GALLERY_REQUEST)
//
//        //val intentFromGallery = Intent(Intent.ACTION_PICK)
//        //        // 设置文件类型
//        //        intentFromGallery.type = "image/*"
//        //        startActivityForResult(intentFromGallery, SetProfilePictureActivity.Companion.CODE_GALLERY_REQUEST)
//    }
//
//    fun hasSdcard(): Boolean {
//        val state = Environment.getExternalStorageState()
//        return if (state == Environment.MEDIA_MOUNTED) {
//            // 有存储的SDCard
//            true
//        } else {
//            false
//        }
//    }*/
//
//
//
//
//
//    //-----------------------------------------------
///*
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//
//        // 用户没有进行有效的设置操作，返回
//        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show()
//            return
//        }
//        when (requestCode) {
//            SetProfilePictureActivity.Companion.CODE_GALLERY_REQUEST -> cropRawPhoto(intent!!.data)
//
//            SetProfilePictureActivity.Companion.CODE_RESULT_REQUEST -> intent?.let { setImageToHeadView(it) }
//        }
//        super.onActivityResult(requestCode, resultCode, intent)
//    }
//
//    /**
//     * 裁剪原始的图片
//     */
//    fun cropRawPhoto(uri: Uri?) {
//        val intent = Intent("com.android.camera.action.CROP")
//        intent.setDataAndType(uri, "image/*")
//        intent.putExtra("crop", "true")
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1)
//        intent.putExtra("aspectY", 1)
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 250)
//        intent.putExtra("outputY", 250)
//        // 图片格式
//        intent.putExtra("outputFormat", "JPEG")
//        intent.putExtra("noFaceDetection", true) // 取消人脸识别
//        intent.putExtra("return-data", true)
//        startActivityForResult(intent, SetProfilePictureActivity.Companion.CODE_RESULT_REQUEST)
//    }
//
//    /**
//     * 提取保存裁剪之后的图片数据，并设置头像部分的View
//     */
//    private fun setImageToHeadView(intent: Intent) {
//        val extras = intent.extras
//        if (extras != null) {
//            photo = extras.getParcelable("data")
//            currentProfilePhoto.setImageBitmap(photo)
//        }
//    }
//
//    // 将图片转换成base64编码
//    fun getBase64(bitmap: Bitmap): String {
//        val out = ByteArrayOutputStream()
//        //压缩的质量为60%
//        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out)
//        //生成base64字符
//        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
//    }
//
//
//    // 从本地相册选取图片作为头像
//    private fun choseHeadImageFromGallery() {
//        val intentFromGallery = Intent(Intent.ACTION_PICK)
//        // 设置文件类型
//        intentFromGallery.type = "image/*"
//        startActivityForResult(intentFromGallery, SetProfilePictureActivity.Companion.CODE_GALLERY_REQUEST)
//    }
//
//    companion object {
//        /* 头像文件 */
//        private const val IMAGE_FILE_NAME = "temp_head_image.jpg"
//
//        /* 请求识别码 */
//        private const val CODE_GALLERY_REQUEST = 0xa0
//        private const val CODE_CAMERA_REQUEST = 0xa1
//        private const val CODE_RESULT_REQUEST = 0xa2
//
//        // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。（生成bitmap貌似有时要报错？可试下把大小弄小点）
//        private const val output_X = 480
//        private const val output_Y = 480
//    }*/
//
//
//
//
//
//
////________________________________________________________________________________________________________-
//
///*
//class MainActivity : AppCompatActivity(), View.OnClickListener {
//    var ivAvatar: RoundedImageView? = null
//    var photoButton: Button? = null
//    var picButton: Button? = null
//    private var photo: Bitmap? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        ivAvatar = findViewById<View>(R.id.ivAvatar) as RoundedImageView
//        photoButton = findViewById<View>(R.id.btShot) as Button
//        picButton = findViewById<View>(R.id.btAlbum) as Button
//        photoButton!!.setOnClickListener(this)
//        picButton!!.setOnClickListener(this)
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.btShot -> choseHeadImageFromCameraCapture()
//            R.id.btAlbum -> choseHeadImageFromGallery()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//
//        // 用户没有进行有效的设置操作，返回
//        if (resultCode == RESULT_CANCELED) {
//            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show()
//            return
//        }
//        when (requestCode) {
//            MainActivity.Companion.CODE_GALLERY_REQUEST -> cropRawPhoto(intent!!.data)
//            MainActivity.Companion.CODE_CAMERA_REQUEST -> if (hasSdcard()) {
//                val tempFile = File(
//                    Environment.getExternalStorageDirectory(),
//                    MainActivity.Companion.IMAGE_FILE_NAME
//                )
//                cropRawPhoto(Uri.fromFile(tempFile))
//            } else {
//                Toast.makeText(this, "没有SDCard!", Toast.LENGTH_LONG).show()
//            }
//            MainActivity.Companion.CODE_RESULT_REQUEST -> intent?.let { setImageToHeadView(it) }
//        }
//        super.onActivityResult(requestCode, resultCode, intent)
//    }
//
//    /**
//     * 裁剪原始的图片
//     */
//    fun cropRawPhoto(uri: Uri?) {
//        val intent = Intent("com.android.camera.action.CROP")
//        intent.setDataAndType(uri, "image/*")
//        intent.putExtra("crop", "true")
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1)
//        intent.putExtra("aspectY", 1)
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 250)
//        intent.putExtra("outputY", 250)
//        // 图片格式
//        intent.putExtra("outputFormat", "JPEG")
//        intent.putExtra("noFaceDetection", true) // 取消人脸识别
//        intent.putExtra("return-data", true)
//        startActivityForResult(intent, MainActivity.Companion.CODE_RESULT_REQUEST)
//    }
//
//    /**
//     * 提取保存裁剪之后的图片数据，并设置头像部分的View
//     */
//    private fun setImageToHeadView(intent: Intent) {
//        val extras = intent.extras
//        if (extras != null) {
//            photo = extras.getParcelable("data")
//            ivAvatar.setImageBitmap(photo)
//        }
//    }
//
//    // 将图片转换成base64编码
//    fun getBase64(bitmap: Bitmap): String {
//        val out = ByteArrayOutputStream()
//        //压缩的质量为60%
//        bitmap.compress(Bitmap.CompressFormat.PNG, 60, out)
//        //生成base64字符
//        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
//    }
//
//    // 从本地相册选取图片作为头像
//    private fun choseHeadImageFromGallery() {
//        val intentFromGallery = Intent(Intent.ACTION_PICK)
//        // 设置文件类型
//        intentFromGallery.type = "image/*"
//        startActivityForResult(intentFromGallery, MainActivity.Companion.CODE_GALLERY_REQUEST)
//    }
//
//    // 启动手机相机拍摄照片作为头像
//    private fun choseHeadImageFromCameraCapture() {
//        val intentFromCapture = Intent("android.media.action.IMAGE_CAPTURE")
//
//        // 判断存储卡是否可用，存储照片文件
//        if (hasSdcard()) {
//            intentFromCapture.putExtra(
//                MediaStore.EXTRA_OUTPUT, Uri
//                    .fromFile(
//                        File(
//                            Environment
//                                .getExternalStorageDirectory(),
//                            MainActivity.Companion.IMAGE_FILE_NAME
//                        )
//                    )
//            )
//        }
//        startActivityForResult(intentFromCapture, MainActivity.Companion.CODE_CAMERA_REQUEST)
//    }
//
//    fun hasSdcard(): Boolean {
//        val state = Environment.getExternalStorageState()
//        return if (state == Environment.MEDIA_MOUNTED) {
//            // 有存储的SDCard
//            true
//        } else {
//            false
//        }
//    }
//
//    companion object {
//        /* 头像文件 */
//        private const val IMAGE_FILE_NAME = "temp_head_image.jpg"
//
//        /* 请求识别码 */
//        private const val CODE_GALLERY_REQUEST = 0xa0
//        private const val CODE_CAMERA_REQUEST = 0xa1
//        private const val CODE_RESULT_REQUEST = 0xa2
//
//        // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。（生成bitmap貌似有时要报错？可试下把大小弄小点）
//        private const val output_X = 480
//        private const val output_Y = 480
//    }
//}
//*/