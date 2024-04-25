package com.timewise.timewise

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.ActivityAuthBinding
import com.timewise.timewise.databinding.FragmentSignInBinding
import com.timewise.timewise.databinding.FragmentSignUpGoalBinding

class Auth : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var bindingSignIn: FragmentSignInBinding
    private lateinit var bindingSignUp: FragmentSignUpGoalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(layoutInflater)
        bindingSignIn = FragmentSignInBinding.inflate(layoutInflater)
        bindingSignUp = FragmentSignUpGoalBinding.inflate(layoutInflater)
        setContentView(binding.root);
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,SignInFragment())
            .commit();

    }


}

