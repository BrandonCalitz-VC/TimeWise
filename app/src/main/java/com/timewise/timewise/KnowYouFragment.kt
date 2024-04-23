package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val email = arguments?.getString("email")
        return view
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment KnowYouFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            KnowYouFragment().apply {
//                arguments = Bundle().apply {
//                    putString("email", param1)
//                    putString("password", param2)
//                }
//            }
//    }
}