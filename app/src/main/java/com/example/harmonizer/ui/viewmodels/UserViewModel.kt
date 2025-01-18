package com.example.harmonizer.ui.viewmodels

import RetrofitClient
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmonizer.helpers.getBearerValue
import com.example.harmonizer.remote.api.errors.parseError
import com.example.harmonizer.remote.api.models.requests.UpdateUserFirstName
import com.example.harmonizer.remote.api.models.requests.UpdateUserLastName
import com.example.harmonizer.remote.api.models.responses.UserResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class UserViewModel(context: Context) : ViewModel() {
    private val jwtBearerHeaderValue: String = getBearerValue(context)
    val errorMessage: MutableLiveData<String> = MutableLiveData("");
    val isErrorActive: MutableLiveData<Boolean> = MutableLiveData(false);
    val user: MutableLiveData<UserResponse?> = MutableLiveData()

    fun refreshUser() = viewModelScope.launch {
        try {

            val response = RetrofitClient.instance.getUser(jwtBearerHeaderValue)

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            user.postValue(response.body())

        } catch (e: Exception) {
            raiseError("Wystąpił błąd, spróbuj ponownie", e);
        }
    }


    fun updateUserFirstName(userId: Int, firstName: String) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance.updateUserFirstName(
                userId = userId,
                jwtToken = jwtBearerHeaderValue,
                request = UpdateUserFirstName(firstName)
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

        } catch (e: Exception) {
            raiseError("Wystąpił błąd, spróbuj ponownie", e);
        }
    }

    fun updateUserLastName(userId: Int, lastName: String) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance.updateUserLastName(
                jwtToken = jwtBearerHeaderValue,
                userId = userId,
                request = UpdateUserLastName(lastName)
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }
        } catch (e: Exception) {
            raiseError("Wystąpił błąd, spróbuj ponownie", e);
        }
    }

    private fun handleErrorResponse(errorBody: ResponseBody?, responseCode: Int) {
        val errorBodyRaw = errorBody?.string()
        Log.i("HttpResponse", "HttpResponseCode $responseCode.toString()")
        val errorMessageText = parseError(errorBodyRaw ?: "{}")
        raiseError(errorMessageText)
    }

    private fun raiseError(errorMessageText: String, exception: Throwable? = null) {
        errorMessage.postValue("Error: $errorMessageText")
        isErrorActive.postValue(true)
        Log.e("HouseholdViewModel", errorMessageText)
        if (exception != null) {
            Log.e("HouseholdViewModel", errorMessageText, exception)
        }
    }

    fun logout() {
        user.postValue(null)
    }
}