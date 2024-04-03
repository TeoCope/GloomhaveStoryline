package com.example.gloomhavestoryline2.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.gloomhavestoryline2.MainActivity
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentSingupBinding
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicator
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.example.gloomhavestoryline2.view_model.AuthViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

class SingupFragment : Fragment(), ProgressIndicator, ToastMessage {

    private lateinit var binding: FragmentSingupBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_singup,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.singupFragment = this

        val nicknameEditText = binding.nicknameEditText
        val emaiEditText = binding.emailEditText
        val passwordEditText = binding.passwordEditText

        val nicknameErrorObserver = Observer<Int?> {newError ->
            if (newError != null)
                nicknameEditText.error = getString(newError)
            else
                nicknameEditText.error = newError
        }
        authViewModel.nickname_error.observe(viewLifecycleOwner, nicknameErrorObserver)

        val emailErrorObserver = Observer<Int?> {newError ->
            if (newError != null)
                emaiEditText.error = getString(newError)
            else
                emaiEditText.error = null
        }
        authViewModel.email_error.observe(viewLifecycleOwner, emailErrorObserver)

        val passwordErrorObserver = Observer<Int?> {newError ->
            if (newError != null)
                passwordEditText.error = getString(newError)
            else
                passwordEditText.error = newError
        }
        authViewModel.password_error.observe(viewLifecycleOwner, passwordErrorObserver)

        val statusObserver = Observer<RequestStatus> {newStatus ->
            when {
                newStatus == RequestStatus.LOADING -> {
                    setVisible()
                }
                newStatus == RequestStatus.DONE -> {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                else -> setGone()
            }
        }
        authViewModel.status.observe(viewLifecycleOwner,statusObserver)
    }

    fun singup() {
        val nickname = binding.nicknameEditText.editText?.text.toString().trim()
        val email = binding.emailEditText.editText?.text.toString().trim()
        val password = binding.passwordEditText.editText?.text.toString().trim()

        authViewModel.singUp(nickname,email,password)
    }

    override fun setVisible() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility = View.VISIBLE
    }

    override fun setGone() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility = View.GONE
    }

    override fun showSnackbar(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}