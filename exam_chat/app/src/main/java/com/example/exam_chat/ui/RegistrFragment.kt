package com.example.exam_chat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.exam_chat.R
import com.example.exam_chat.databinding.FragmentRegistrBinding
import com.example.exam_chat.models.UserRegModel
import com.example.exam_chat.resourse.Resourse
import com.example.exam_chat.viewmodels.MainViewModel


class RegistrFragment : Fragment() {


    lateinit var binding: FragmentRegistrBinding

    val viewModel by activityViewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resourse.Success -> {
                    Toast.makeText(requireContext(), "registr success", Toast.LENGTH_SHORT).show()
                    Log.d("check", response.data.toString())
                    viewModel.user.value = null
                    findNavController().navigate(R.id.action_registrFragment_to_loginFragment)
                }
                is Resourse.Loading -> {

                }
                is Resourse.Error -> {
                    Toast.makeText(requireContext(), "registr failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_registrFragment_to_loginFragment)
        }
        binding.btnRegistr.setOnClickListener {
            registration()
        }
    }
    private fun registration(){
        if (binding.login.editText!!.text.isNotEmpty()) {
            if (binding.password.editText!!.text.isNotEmpty() && binding.repeatPassword.editText!!.text.isNotEmpty()) {
                if (binding.password.editText!!.text.toString().equals(binding.repeatPassword.editText!!.text.toString())) {
                    viewModel.registration(
                        UserRegModel(
                            binding.login.editText!!.text.toString(),
                            binding.password.editText!!.text.toString(),
                            binding.repeatPassword.editText!!.text.toString()
                        )
                    )
                }else {
                    binding.password.error = resources.getString(R.string.password_confirmation_error)
                    binding.repeatPassword.error = resources.getString(R.string.password_confirmation_error)
                }
            } else {
                binding.password.error = resources.getString(R.string.input_empty_error)
                binding.repeatPassword.error = resources.getString(R.string.input_empty_error)
            }
        }else binding.login.error = resources.getString(R.string.input_empty_error)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrBinding.inflate(inflater)
        return binding.root
    }


}