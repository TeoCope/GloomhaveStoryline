package com.example.gloomhavestoryline2.ui.auth

import android.content.DialogInterface
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
import androidx.navigation.fragment.findNavController
import com.example.gloomhavestoryline2.MainActivity
import com.example.gloomhavestoryline2.R
import com.example.gloomhavestoryline2.databinding.FragmentLoginBinding
import com.example.gloomhavestoryline2.other.enum_class.RequestStatus
import com.example.gloomhavestoryline2.other.listeners.ToastMessage
import com.example.gloomhavestoryline2.other.listeners.ProgressIndicator
import com.example.gloomhavestoryline2.other.navAnimations
import com.example.gloomhavestoryline2.view_model.AuthViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment(), ProgressIndicator, ToastMessage {

    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.toastMessage = this

        binding.loginFragment = this
        val emaiEditText = binding.emailEditText
        val passwordEditText = binding.passwordEditText

        val emailErrorObserver = Observer<Int?> { newError ->
            if (newError != null)
                emaiEditText.error = getString(newError)
            else
                emaiEditText.error = newError
        }
        authViewModel.email_error.observe(viewLifecycleOwner, emailErrorObserver)

        val passwordErrorObserver = Observer<Int?> { newError ->
            if (newError != null)
                passwordEditText.error = getString(newError)
            else
                passwordEditText.error = newError
        }
        authViewModel.password_error.observe(viewLifecycleOwner, passwordErrorObserver)

        val statusObserver = Observer<RequestStatus> { newStatus ->
            when {
                newStatus == RequestStatus.LOADING -> {
                    setVisible()
                }
                newStatus == RequestStatus.DONE -> {
                    setGone()
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
                else -> setGone()
            }
        }
        authViewModel.status.observe(viewLifecycleOwner, statusObserver)
    }

    fun resetPassword() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_reset_password, null)
        val emailEditText = view.findViewById<TextInputLayout>(R.id.dialogEmailEditText)
        context?.let {
            val dialog = MaterialAlertDialogBuilder(it)
                .setView(view)
                .setPositiveButton(R.string.reset_password) { _, _ -> }
                .show()

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val email = emailEditText.editText?.text.toString().trim()
                val emailErrorObserver = Observer<Int?> { newError ->
                    if (newError != null)
                        emailEditText.error = getString(newError)
                    else
                        emailEditText.error = newError
                }
                authViewModel.email_error.observe(viewLifecycleOwner, emailErrorObserver)
                if (authViewModel.resetPassword(email)) {
                    Toast.makeText(context, "Check your email", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            dialog.setOnCancelListener {
                authViewModel.resetError(null)
            }
        }
    }

    fun login() {
        val email = view?.findViewById<TextInputLayout>(R.id.emailEditText)?.editText?.text?.trim()
        val password = view?.findViewById<TextInputLayout>(R.id.passwordEditText)?.editText?.text?.trim()
        authViewModel.login(email.toString(), password.toString())
    }

    fun goToSingup() {
        authViewModel.resetError(null)
        findNavController().navigate(R.id.singupFragment, null, navAnimations)
    }

    override fun setVisible() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility =
            View.VISIBLE
    }

    override fun setGone() {
        activity?.findViewById<LinearProgressIndicator>(R.id.linearProgressIndicator)?.visibility =
            View.GONE
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}