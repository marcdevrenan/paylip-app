package br.edu.infnet.paylipapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        appAuth = FirebaseAuth.getInstance()

        val btLogin = findViewById<Button>(R.id.btLogin)

        btLogin.setOnClickListener{
            try {
                val etEmail = findViewById<EditText>(R.id.etEmail)
                val etPassword = findViewById<EditText>(R.id.etPassword)

                appAuth
                    .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            appUser = appAuth.currentUser

                            intent()

                        } else {

                            appUser = null
                            Toast.makeText(
                                this,
                                "Dados de credenciais inválidos ou Usuário não cadastrado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            } catch (ex: IllegalArgumentException) {
                Toast.makeText(
                    this,
                    "O campo de e-mail e senha não podem ser nulos.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        val btNewRegister = findViewById<Button>(R.id.btNewRegister)


    }


    override fun onStart() {
        super.onStart()
        val currentUser = appAuth.currentUser
        appUser = currentUser


        if (currentUser != null) {
            Toast.makeText(this, "Logado: ${appUser?.email}", Toast.LENGTH_LONG).show()
            intent()
        }else{
            Toast.makeText(this, "NULL: ${appUser?.email}", Toast.LENGTH_LONG).show()
        }
    }

    private fun intent(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
//            activity?.let { finishAfterTransition(it) }
    }



}