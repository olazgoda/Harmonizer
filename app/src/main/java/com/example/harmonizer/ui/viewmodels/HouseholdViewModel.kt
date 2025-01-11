package com.example.harmonizer.ui.viewmodels

import RetrofitClient
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmonizer.helpers.getBearerValue
import com.example.harmonizer.helpers.toZonedDateTime
import com.example.harmonizer.remote.api.errors.parseError
import com.example.harmonizer.remote.api.models.requests.CreateInvitationRequest
import com.example.harmonizer.remote.api.models.requests.CreateTaskRequest
import com.example.harmonizer.remote.api.models.requests.UpdateTaskRequest
import com.example.harmonizer.remote.api.models.responses.HouseholdEventResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.time.ZonedDateTime

class HouseholdViewModel(context: Context) : ViewModel() {
    private val jwtBearerHeaderValue: String = getBearerValue(context);
    val household: MutableLiveData<HouseholdResponse> = MutableLiveData();
    val householdEvents: MutableLiveData<List<HouseholdEventResponse>> = MutableLiveData();
    val isErrorActive: MutableLiveData<Boolean> = MutableLiveData(false);
    val errorMessage: MutableLiveData<String> = MutableLiveData("");
    val selectedHouseholdId: MutableLiveData<Int> = MutableLiveData(1);

    fun refreshHouseholdData() = viewModelScope.launch {
        try {
            val householdResponse = RetrofitClient.instance.getHousehold(
                selectedHouseholdId.value!!,
                jwtBearerHeaderValue
            )
            household.postValue(householdResponse);

            val householdEventsResponse = RetrofitClient.instance.getHouseholdEvents(
                selectedHouseholdId.value!!,
                jwtBearerHeaderValue
            )
            if(householdEventsResponse.isSuccessful)
            {
                householdEvents.postValue(householdEventsResponse.body());
            }

        } catch (e: Exception) {
            isErrorActive.postValue(true)
            Log.e("MainActivity", "Exception occurred", e)
            errorMessage.postValue("Failed to fetch household with id $selectedHouseholdId")
        }
    }

    fun assignTaskToMember(taskId: Int, memberId: Int) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(assignedMemberId = memberId))
    }

    fun createTask(
        title: String,
        description: String,
        dueDate: ZonedDateTime,
        assignedMemberId: Int?
    ) = viewModelScope.launch {
        try{
            val response = RetrofitClient.instance.createTask(
                jwtToken = jwtBearerHeaderValue,
                householdId = selectedHouseholdId.value!!,
                request = CreateTaskRequest(
                    name = title,
                    description = description,
                    dueDate = dueDate,
                    assignedMemberId = assignedMemberId
                )
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            refreshHouseholdData();
        }
        catch (e: Exception) {
            raiseError("Could not create task", e)
        }
    }

    fun updateTaskDueDate(taskId: Int, selectedDateMillis: Long) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(dueDate = selectedDateMillis.toZonedDateTime()))
    }

    fun updateTaskDoneStatus(taskId: Int, isDone: Boolean) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(isDone = isDone))
    }
    
    fun inviteNewMember(newMemberEmail: String) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance
                .inviteMember(
                    selectedHouseholdId.value!!,
                    CreateInvitationRequest(newMemberEmail),
                    jwtBearerHeaderValue
                )
            
            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            refreshHouseholdData();
        }
        catch(e: Exception) {
            raiseError("Could not invite member", e);
        }
    }

    fun deleteMember(memberId: Int) = viewModelScope.launch{
        try {
            val response = RetrofitClient.instance
                .deleteMember(
                    selectedHouseholdId.value!!,
                    memberId,
                    jwtBearerHeaderValue
                )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            refreshHouseholdData();
        }
        catch(e: Exception) {
            raiseError("Could not invite member", e);
        }
    }

    private fun updateTask(taskId: Int, request: UpdateTaskRequest) = viewModelScope.launch{
        try {
            val response = RetrofitClient.instance.updateTask(
                jwtToken = jwtBearerHeaderValue,
                householdId = selectedHouseholdId.value!!,
                taskId = taskId,
                request = request
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            refreshHouseholdData();

        } catch (e: Exception) {
            raiseError("Could not update task", e);
        }
    }

    private fun handleErrorResponse(errorBody: ResponseBody?, responseCode: Int)
    {
        val errorBodyRaw = errorBody?.string()
        Log.i("HttpResponse", "HttpResponseCode $responseCode.toString()")
        val errorMessageText = parseError(errorBodyRaw ?: "{}")
        raiseError(errorMessageText)
    }

    private fun raiseError(errorMessageText: String, exception: Throwable? = null)
    {
        errorMessage.postValue("Error: $errorMessageText")
        isErrorActive.postValue(true)
        Log.e("HouseholdViewModel", errorMessageText)
        if(exception != null)
        {
            Log.e("HouseholdViewModel", errorMessageText, exception)
        }
    }
}