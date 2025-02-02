package com.example.harmonizer.ui.viewmodels

import RetrofitClient
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmonizer.helpers.clearAuthData
import com.example.harmonizer.helpers.getBearerValue
import com.example.harmonizer.helpers.saveAuthData
import com.example.harmonizer.remote.api.errors.parseError
import com.example.harmonizer.remote.api.models.requests.LoginRequest
import com.example.harmonizer.remote.api.models.requests.UpdateUserName
import com.example.harmonizer.remote.api.models.responses.UserResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class UserViewModel(
    private val applicationContext: Context
) : ViewModel() {
    private val jwtBearerHeaderValue: String = getBearerValue(applicationContext)
    val errorMessage: MutableLiveData<String> = MutableLiveData("")
    val isErrorActive: MutableLiveData<Boolean> = MutableLiveData(false)
    val user: MutableLiveData<UserResponse?> = MutableLiveData()

    fun refreshUser() = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance.getUser(jwtBearerHeaderValue)

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            Log.d("UserResponse", response.body().toString())

            user.value = response.body()

        } catch (e: Exception) {
            raiseError("Wystąpił błąd, spróbuj ponownie", e);
        }
    }


    fun updateUserName(firstName: String, lastName: String) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance.updateUserName(
                jwtToken = jwtBearerHeaderValue,
                request = UpdateUserName(firstName, lastName)
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }
            refreshUser()

        } catch (e: Exception) {
            raiseError("Wystąpił błąd, spróbuj ponownie", e);
        }
    }

    suspend fun login(email: String, password: String){
        try {
            val token = RetrofitClient.instance.login(
                LoginRequest(email, password)
            )
            saveAuthData(applicationContext, token, email, password)
            refreshUser()
        }
        catch (e: Exception){
            raiseError("Wystąpił błąd, spróbuj ponownie", e)
            throw e
        }
    }

    fun logout() {
        user.postValue(null)
        clearAuthData(applicationContext)
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

    fun clearErrorState() {
        isErrorActive.value = false
        errorMessage.value = ""
    }
}