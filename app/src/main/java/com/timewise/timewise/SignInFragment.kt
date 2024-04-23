package com.timewise.timewise

import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Debug
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.timewise.timewise.databinding.FragmentSignInBinding
import java.io.Console
import kotlin.math.log

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentSignInBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_sign_in, container, false);
        binding = FragmentSignInBinding.bind(view);
        binding.SignInSignUp.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,SignUpFragment())
                .commit();
        }

//        view.findViewById<TextView>(R.id.SignIn_SignUp).setOnClickListener{
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer,SignUpFragment(),null)
//                .commit();
//        }
        return view
    }


}