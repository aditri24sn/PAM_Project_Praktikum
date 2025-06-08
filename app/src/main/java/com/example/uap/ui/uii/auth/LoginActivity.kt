package com.example.uap.ui.uii.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uap.databinding.ActivityLoginBinding
import com.example.uap.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses login user dengan Firebase
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Jika login berhasil
                        Toast.makeText(this, "Login berhasil.", Toast.LENGTH_SHORT).show()

                        // Pindah ke halaman utama aplikasi (MainActivity)
                        val intent = Intent(this, MainActivity::class.java)

                        // Baris ini penting: Hapus semua activity sebelumnya (Opsi, Login)
                        // dari history, sehingga pengguna tidak bisa kembali ke halaman login
                        // dengan menekan tombol "back" setelah berhasil masuk.
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()

                    } else {
                        // Jika login gagal (misal: password salah, user tidak ada)
                        Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}