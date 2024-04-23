package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.timewise.timewise.databinding.FragmentSignInBinding
import com.timewise.timewise.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_sign_up, container, false);
        binding = FragmentSignUpBinding.bind(view);
        binding.SignUpSignIn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SignInFragment())
                .commit()
        }

        binding.SignUpBtnSignUp.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("email", binding.SignUpEtEmail.text.toString())
            bundle.putString("password", binding.SignUpEtPassword.text.toString())

            val transaction = parentFragmentManager.beginTransaction()
            val fragmentTwo = KnowYouFragment()
            fragmentTwo.arguments = bundle
            transaction.replace(R.id.fragmentContainer, fragmentTwo)
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        }


        return view
    }
}