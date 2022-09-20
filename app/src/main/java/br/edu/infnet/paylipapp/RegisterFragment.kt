package br.edu.infnet.paylipapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterFragment : Fragment() {

    private lateinit var appAuth: FirebaseAuth
    private var appUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        appAuth = FirebaseAuth.getInstance()

        val etEmailRegister = view.findViewById<EditText>(R.id.etEmailRegister)
        val etPasswordRegister = view.findViewById<EditText>(R.id.etPasswordRegister)
        val etCheckPasswordRegister = view.findViewById<EditText>(R.id.etCheckPasswordRegister)
        val btSaveRegister = view.findViewById<Button>(R.id.btSaveRegister)

        btSaveRegister.setOnClickListener {
            if (etPasswordRegister.text.toString() == etCheckPasswordRegister.text.toString()) {
                if (checkEmail(etEmailRegister) && checkPassword(etPasswordRegister)) {
                    appAuth
                        .createUserWithEmailAndPassword(
                            etEmailRegister.text.toString(),
                            etPasswordRegister.text.toString()
                        )
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                appUser = appAuth.currentUser
                                Toast.makeText(
                                    activity,
                                    "Cadastro realizado com sucesso! Login efetuado: ${appUser?.email}",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                activity?.let { ActivityCompat.finishAfterTransition(it) }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                activity,
                                "Erro no processo. Usuário cadastrado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }

            } else {
                etPasswordRegister.error = "As senhas digitadas não são correspondentes"
                etCheckPasswordRegister.error = "As senhas digitadas não são correspondentes"
            }
        }
        return view
    }


    private fun checkEmail(email: EditText): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            return true
        } else {
            email.error = "Formato de e-mail inválido"
            return false
        }
    }


    private fun checkPassword(password: EditText): Boolean {
        if (password.text.toString().length >= 6) {
            return true
        } else {
            password.error = "Mínimo de 6 caracteres"
            return false
        }
    }

}