package com.timewise.timewise

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Debug
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.FragmentSignInBinding
import java.io.Console
import kotlin.math.log

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_sign_in, container, false);
        binding = FragmentSignInBinding.bind(view);
        auth = Firebase.auth

        binding.SignInSignUp.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,SignUpFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        }

        binding.SignInBtnSignIn.setOnClickListener{
            val email: String = binding.SignInEtEmail.text.toString().trim()
            val password: String = binding.SignInEtPassword.text.toString().trim()

            var isValid = true
            if(email.isEmpty()){
                binding.SignInEtEmailLayout.handleError("Invalid Email")
                isValid = false
            }
            if(password.isEmpty()){
                binding.SignInEtPasswordLayout.handleError("Invalid Password")
                isValid = false
            }
            if (!isValid)return@setOnClickListener

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "SignInWithCredential:success")
                    startActivity(Intent(activity,MainActivity::class.java))
//                    val db = Firebase.firestore
//
//                    db.collection("users").add(user)
//                        .addOnSuccessListener { documentReference ->
//                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                            startActivity(Intent(activity,Dashboard::class.java))
//
//                        }
//                        .addOnFailureListener { e ->
//                            Log.w(ContentValues.TAG, "Error adding document", e)
//                        }
                } else {
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                }
            }

        }

        return view
    }


}