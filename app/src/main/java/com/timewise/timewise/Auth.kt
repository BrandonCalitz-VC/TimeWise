package com.timewise.timewise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.timewise.timewise.databinding.ActivityAuthBinding
import com.timewise.timewise.databinding.FragmentSignInBinding

class Auth : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityAuthBinding
    private lateinit var bindingSignIn: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(layoutInflater)
        bindingSignIn = FragmentSignInBinding.inflate(layoutInflater)
        setContentView(binding.root);
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,SignInFragment())
            .commit();

        bindingSignIn.SignInBtnSignIn.setOnClickListener{
            var email = bindingSignIn.SignInEtEmail.text.toString()
            println(email)
        }
    }
}
