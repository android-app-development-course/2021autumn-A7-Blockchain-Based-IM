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
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_set_profile_picture.*
import java.io.ByteArrayOutputStream
import java.io.File

class SetProfilePictureActivity : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this,"IM_app.db", 2)
    private val fromAlbum = 1
    private val takePhoto = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File

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
        if(cursor.moveToFirst()) {
            val userPhotoByteChar:ByteArray = cursor.getBlob(cursor.getColumnIndex("profile_photo"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(userPhotoByteChar,0,userPhotoByteChar.size)
            currentProfilePhoto.setImageBitmap(bitmap)
        }
        cursor.close()

        btn_set_photo_from_album.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent,fromAlbum)
        }

        btn_take_photo.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.scnu.blockchain_based_im_app.ui.setting.fileprovider", outputImage);
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
        super.onActivityResult(requestCode, resultCode, data)
        val db = dbHelper.writableDatabase
        when (requestCode) {
            fromAlbum -> {
                if(resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        val bitmap = getBitmapFromUri(uri)
                        currentProfilePhoto.setImageBitmap(bitmap)
                        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG,100, baos)
                        db.execSQL("update user set profile_photo=? where id=?", arrayOf(baos.toByteArray(),MainActivity.userID))
                    }
                }
            }
            takePhoto -> {
                // 将拍摄的照片显示出来
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                currentProfilePhoto.setImageBitmap(rotateIfRequired(bitmap))
                val baos: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG,100, baos)
                db.execSQL("update user set profile_photo=? where id=?", arrayOf(baos.toByteArray(),MainActivity.userID))
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
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
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotatedBitmap
    }

}