package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.timewise.timewise.databinding.FragmentKnowYouBinding

class KnowYouFragment : Fragment() {
    private lateinit var binding : FragmentKnowYouBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view : View = inflater.inflate(R.layout.fragment_know_you, container, false);
        binding = FragmentKnowYouBinding.bind(view)
//        val email = arguments?.getString("email")

        binding.KYBack.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
        binding.KYBtnContinue.setOnClickListener{
            val firstName:String = binding.KYEtFirstName.text.toString().trim()
            val lastName:String = binding.KYEtLastName.text.toString().trim()

            var isValid = true
            if(firstName.isEmpty()){
                binding.KYEtFirstNameLayout.handleError("Invalid First Name")
                isValid = false
            }
            if(lastName.isEmpty()){
                binding.KYEtLastNameLayout.handleError("Invalid Last Name")
                isValid = false
            }
            if(!isValid) return@setOnClickListener

            val args = arguments ?: Bundle()
            args.putString("firstName",firstName)
            args.putString("lastName",lastName)

            val fragmentTwo = SignUpGoalFragment()
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