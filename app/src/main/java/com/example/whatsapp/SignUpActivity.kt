package com.example.whatsapp

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlin.coroutines.Continuation


class SignUpActivity : AppCompatActivity() {

    val storage by lazy{
        FirebaseStorage.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }
    val database by lazy{
        FirebaseFirestore.getInstance()
    }

    private lateinit var downloadUrl: String
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SafetyNet.getClient(this)
            .enableVerifyApps()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isVerifyAppsEnabled) {
                        Log.d("MY_APP_TAG", "The user gave consent to enable the Verify Apps feature.")
                    } else {
                        Log.d(
                            "MY_APP_TAG",
                            "The user didn't give consent to enable the Verify Apps feature."
                        )
                    }
                } else {
                    Log.e("MY_APP_TAG", "A general error occurred.")
                }
            }



        userImageView.setOnClickListener {
            checkPermissionForImage()
        }
        materialButton3.setOnClickListener {
            val name = EtView1.text.toString()
            val lastName = EtView2.text.toString()
            materialButton3.isEnabled = false
            if(name.isEmpty()){
                Toast.makeText(this,"Name cannot be empty",Toast.LENGTH_SHORT).show()
            }
            else if(!::downloadUrl.isInitialized){
                Toast.makeText(this,"Image cannot be empty",Toast.LENGTH_SHORT).show()
            }
            else{
                    val user = User(name,lastName,downloadUrl,downloadUrl,auth.uid!!)
                database.collection("users").document(auth.uid!!).set(user).addOnSuccessListener {

                    Log.d("Hi","Inside");
                    startActivity(Intent(this, MainActivity::class.java));
                    finish()

                }.addOnFailureListener {
                    materialButton3.isEnabled = true
                }

            }
        }

    }

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permission,
                    1001
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
                requestPermissions(
                    permissionWrite,
                    1002
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            } else {
               pickImageFromGallery()
                //performCrop()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
             intent,
            1000
        )
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TAG","INSIDE00");
        if(resultCode == Activity.RESULT_OK && requestCode == 1000){

            Log.d("TAG","INSIDE");
            data?.data?.let {
                CropImage.activity(it)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);

            }

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                userImageView.setImageURI(resultUri)
                uploadImage(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

    }

    private fun uploadImage(resultUri: Uri) {

        materialButton3.isEnabled = false
        val ref = storage.reference.child("uploads/"+auth.uid.toString())
        val uploadTask = ref.putFile(resultUri)
        progressDialog = createProgressDialog("Uploading..",false)
        progressDialog.show()

        uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot,Task<Uri>>{task ->
            if(!task.isSuccessful){
                task.exception.let {
                    throw it!!
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener {task ->
            progressDialog.dismiss()
            materialButton3.isEnabled = true
            if(task.isSuccessful){
                downloadUrl = task.result.toString()
            }
        }.addOnFailureListener {

        }
    }
}

