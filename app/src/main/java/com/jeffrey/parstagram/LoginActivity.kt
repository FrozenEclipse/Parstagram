package com.jeffrey.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if(ParseUser.getCurrentUser() != null){
//          Auto logout after rerun for testing
//            ParseUser.logOut()
            goToMainActivity()
        }

//        Test Connection Successful!
//        val firstObject = ParseObject("FirstClass")
//        firstObject.put("message","Hey ! Second message from android. Parse is now connected")
//        firstObject.saveInBackground {
//            if (it != null) {
//                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
//            } else {
//                Log.d("MainActivity", "Object saved.")
//            }
//        }

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }
        findViewById<Button>(R.id.signupBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String){
        val user = ParseUser()

// Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                goToMainActivity()
                Log.i("SignUp", "Successfully created a new account")
                Toast.makeText(this, "You have succesfully created a new account!",Toast.LENGTH_SHORT).show()
            } else {
                Log.i("SignUp", "Failed to signup")
                Toast.makeText(this, "Uh oh! Signup was unsuccessful...",Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Successfully logged in user")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error logging in",Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Failed $username and $password")
            }})
        )
    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}