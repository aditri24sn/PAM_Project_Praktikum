package com.example.uap.ui.uii.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uap.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Auth
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener {
            // Ambil nilai dari setiap EditText dan hapus spasi di awal/akhir
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etKonfirmasiPassword.text.toString().trim()

            // Validasi input
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak cocok", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses membuat user baru di Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Jika registrasi berhasil
                        Toast.makeText(this, "Registrasi berhasil.", Toast.LENGTH_SHORT).show()

                        // Pindah ke halaman Login agar pengguna bisa masuk
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        // Tutup activity ini agar tidak bisa kembali dengan tombol back
                        finish()
                    } else {
                        // Jika gagal, tampilkan pesan error dari Firebase
                        Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}