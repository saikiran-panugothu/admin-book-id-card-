package com.example.admin_bookidcard

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class register : AppCompatActivity() {
    lateinit var progressDialog : ProgressDialog
    lateinit var storageReference : StorageReference
    lateinit var imageUri : Uri
    lateinit var databaseReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
       val ordersbtn = findViewById<Button>(R.id.orders)
       val registerBtn = findViewById<Button>(R.id.registerbutton)
      val  studentId = findViewById<EditText>(R.id.studentid)
      val  studentName = findViewById<EditText>(R.id.studentname)
       val studentPassword = findViewById<EditText>(R.id.studentpassword)
       val coursebranch = findViewById<EditText>(R.id.course_branch)
     val   batch = findViewById<EditText>(R.id.batchy)
      val  stuimage = findViewById<ImageView>(R.id.studentimg)
        stuimage.setOnClickListener {
            val imgintent = Intent()
            imgintent.type = "image/*"
            imgintent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(imgintent, 100)
        }
        registerBtn.setOnClickListener {
            progressDialog = ProgressDialog(this@register)
            progressDialog.setTitle("uploading Data")
            progressDialog.show()
            val formatter =
                SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
            val now = Date()
            val imagename = formatter.format(now)
            storageReference = FirebaseStorage.getInstance().getReference("images/$imagename")
            storageReference.putFile(imageUri)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    storageReference.getDownloadUrl()
                        .addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                            val StudentId = studentId.text.toString()
                            val StudentName = studentName.text.toString()
                            val StudentPassword = studentPassword.text.toString()
                            val Coursebranch = coursebranch.text.toString()
                            val Batch = batch.text.toString()
                            val students = Students(StudentName ,StudentId , Batch ,StudentPassword ,Coursebranch,uri.toString())
                            databaseReference =
                                FirebaseDatabase.getInstance().getReference("Students")
                            databaseReference.child(StudentId).setValue(students)
                                .addOnCompleteListener(
                                    OnCompleteListener<Void?> { task ->
                                        if (task.isSuccessful) {
                                            val dialog = Dialog(this@register)
                                            dialog.setContentView(R.layout.ordersplaceddailog)
                                            val btnok = dialog.findViewById<Button>(R.id.okbtn)
                                            btnok.setOnClickListener { dialog.dismiss() }
                                            dialog.show()
                                            studentId.setText("")
                                            studentName.setText("")
                                            studentPassword.setText("")
                                            coursebranch.setText("")
                                            batch.setText("")
                                            stuimage.setImageResource(R.drawable.ic_launcher_background)
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss()
                                            }
                                        } else {
                                            Toast.makeText(
                                                this@register,
                                                "not registered",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                        })
                }).addOnFailureListener(OnFailureListener {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(this@register, "failed to upload image", Toast.LENGTH_SHORT)
                        .show()
                })
        }
        ordersbtn.setOnClickListener {
            val intent = Intent(applicationContext, orders::class.java)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            val  stuimage = findViewById<ImageView>(R.id.studentimg)
            imageUri = data.data!!
            stuimage.setImageURI(imageUri)
        }
    }
}