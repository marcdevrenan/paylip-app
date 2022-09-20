package br.edu.infnet.paylipapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAfterTransition
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {


    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        appAuth = FirebaseAuth.getInstance()

        val btLogin = view.findViewById<Button>(R.id.btLogin)

        btLogin.setOnClickListener {
            try {
                val etEmail = view.findViewById<EditText>(R.id.etEmail)
                val etPassword = view.findViewById<EditText>(R.id.etPassword)

                appAuth
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener() {
                        if (it.isSuccessful) {
                            appUser = appAuth.currentUser

                            intent()

                        } else {
                            appUser = null
                            Toast.makeText(
                                activity,
                                "Dados de credenciais inválidos ou Usuário não cadastrado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } catch (ex: IllegalArgumentException) {
                Toast.makeText(
                    activity,
                    "O campo de e-mail e senha não podem ser nulos.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val btNewRegister = view.findViewById<Button>(R.id.btNewRegister)

        btNewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }


    override fun onStart() {
        super.onStart()
        val currentUser = appAuth.currentUser
        appUser = currentUser

        if (currentUser != null) {
            Toast.makeText(context, "Usuário: ${appUser?.email}", Toast.LENGTH_LONG).show()
            intent()
        } else {
            Toast.makeText(context, "Usuário não logado", Toast.LENGTH_LONG).show()
        }
    }

      fun intent() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.let { finishAfterTransition(it) }
    }


}