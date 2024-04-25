package com.timewise.timewise

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.textfield.TextInputLayout
import com.timewise.timewise.databinding.FragmentSignUpBinding
import kotlin.math.log

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_sign_up, container, false);
        binding = FragmentSignUpBinding.bind(view);
        
        binding.SignUpSignIn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment())
                .commit()
        }

        binding.SignUpEtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isPasswordValid(s.toString())){
                    binding.SignUpEtPasswordLayout.handleError(
                        if(!s.toString().isLongEnough()) "Password must be 8 characters long;\n" else "" +
                        if(!s.toString().isMixedCase()) "Password must have Upper and lower case;\n" else "" +
                        if(!s.toString().hasEnoughDigits()) "Password must have Digits;\n" else "" +
                        if(!s.toString().hasSpecialChar()) "Password must have Special Characters eg. (@!);\n" else "")
                }else{
                    binding.SignUpEtPasswordLayout.handleError(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.SignUpBtnSignUp.setOnClickListener{
            val email: String = binding.SignUpEtEmail.text.toString().trim()
            val password: String = binding.SignUpEtPassword.text.toString().trim()
            val confirmPassword: String = binding.SignUpEtConfirmpassword.text.toString().trim()

            var isValid = true;
            if (!isValidEmail(email)){
                binding.SignUpEtEmailLayout.handleError("Invalid Email")
                isValid = false
            }else{
                binding.SignUpEtEmailLayout.isErrorEnabled = false
            }
            if (!isPasswordValid(password)) isValid = false

            if (password != confirmPassword){
                binding.SignUpEtConfirmpasswordLayout.handleError("Password do not match")
                isValid = false
            }else{
                binding.SignUpEtConfirmpasswordLayout.handleError(null)
            }
            if(!isValid) return@setOnClickListener

            val args = arguments ?: Bundle()
            args.putString("email", email)
            args.putString("password", password)

            val fragmentTwo = KnowYouFragment()
            fragmentTwo.arguments = args
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentTwo)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

        return view
    }
}


