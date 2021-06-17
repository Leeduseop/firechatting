package com.project.firechatting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.UserManager
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.firechatting.databinding.ActivitySignUpBinding
import com.project.firechatting.databinding.ActivitySplashBinding
import com.project.firechatting.model.UserModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val PICK_FROM_ALBUM: Int = 10

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {

            signupImg.setOnClickListener {
                var intent = Intent(Intent.ACTION_PICK)
                intent.type = MediaStore.Images.Media.CONTENT_TYPE

                startActivityForResult(intent, PICK_FROM_ALBUM)
            }

            signUp.setOnClickListener {
                if(emailEdit.text.toString().isNullOrEmpty() || passwordEdit.text.toString().isNullOrEmpty()){
                    return@setOnClickListener
                }else{
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(emailEdit.text.toString(), passwordEdit.text.toString())
                        .addOnCompleteListener { it ->
                            val userModel: UserModel? = null
                            val uid: String = it.result.user!!.uid
                            FirebaseStorage.getInstance().reference.child("userImages").child(it.result.user!!.uid).putFile(imageUri!!).addOnCompleteListener {
                                userModel?.userName = nameEdit.text.toString()
                                userModel?.profileImageUrl = it.result.storage.downloadUrl.toString()

                                FirebaseDatabase.getInstance().reference.child("users").child(uid).setValue(userModel)
                            }

                        }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            binding.signupImg.setImageURI(data?.data)
            imageUri = data?.data

        }
    }

}