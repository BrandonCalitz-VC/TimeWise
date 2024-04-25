package com.timewise.timewise

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.FragmentSignUpGoalBinding
import java.lang.Exception

class SignUpGoalFragment : Fragment() {
    lateinit var binding: FragmentSignUpGoalBinding
    private lateinit var auth: FirebaseAuth;
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view :View = inflater.inflate(R.layout.fragment_sign_up_goal, container, false)
        binding = FragmentSignUpGoalBinding.bind(view)
        auth = Firebase.auth
        binding.GoalBack.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.GoalBtnGetStarted.setOnClickListener{
            try {
                val args = arguments
                val goal : Int;
                try {
                    goal =  Integer.parseInt(binding.GoalEtGoal.text.toString());
                }catch (e:Exception){
                    binding.GoalEtGoalLayout.handleError("Invalid Goal")
                    return@setOnClickListener
                }

                if (args != null) {
                    signUp(
                        args.getString("email")!!,
                        args.getString("password")!!,
                        args.getString("firstName")!!,
                        args.getString("lastName")!!,
                        goal)

                }
            }catch (e:Exception) {
                Log.e(null,"ERROR: SignUp", e)
            }
        }

        return view
    }
    private fun signUp(email:String, password:String, firstName:String, lastName:String, goal: Int){
         auth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener { task->
             if (task.isSuccessful) {
                 Log.d(TAG, "SignUpWithCredential:success")
                 val db = Firebase.firestore
                val user: User = User(firstName,lastName,email,auth.currentUser!!.uid,goal)
                 db.collection("users").add(user)
                     .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                         startActivity(Intent(activity,Dashboard::class.java))

                    }
                     .addOnFailureListener { e ->
                         Log.w(TAG, "Error adding document", e)
                     }
             } else {
                 Log.w(TAG, "signInWithCredential:failure", task.exception)
             }
         }

    }
}