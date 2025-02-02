package com.example.harmonizer.ui.viewmodels

import RetrofitClient
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmonizer.helpers.getBearerValue
import com.example.harmonizer.helpers.getHouseholdData
import com.example.harmonizer.helpers.saveHouseholdData
import com.example.harmonizer.helpers.toZonedDateTime
import com.example.harmonizer.remote.api.errors.parseError
import com.example.harmonizer.remote.api.models.requests.CreateHouseholdRequest
import com.example.harmonizer.remote.api.models.requests.CreateInvitationRequest
import com.example.harmonizer.remote.api.models.requests.CreateTaskRequest
import com.example.harmonizer.remote.api.models.requests.MarkAsReadRequest
import com.example.harmonizer.remote.api.models.requests.UpdateTaskRequest
import com.example.harmonizer.remote.api.models.responses.HouseholdEventResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdResponse
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.time.ZonedDateTime

class HouseholdViewModel(
    private val applicationContext: Context
) : ViewModel() {
    private val jwtBearerHeaderValue: String = getBearerValue(applicationContext);
    val household: MutableLiveData<HouseholdResponse> = MutableLiveData()
    val householdEvents: MutableLiveData<List<HouseholdEventResponse>> = MutableLiveData();
    val isErrorActive: MutableLiveData<Boolean> = MutableLiveData(false);
    val errorMessage: MutableLiveData<String> = MutableLiveData("");
    var selectedHouseholdId: MutableLiveData<Int?> = MutableLiveData(getHouseholdData(applicationContext));
    val selectedHouseholdName: MutableLiveData<String> = MutableLiveData("Wybierz lub stwórz nowe");

    fun refreshHouseholdData() = viewModelScope.launch {
        try {
            if (selectedHouseholdId.value == null) {
                return@launch
            }
            val householdResponse = RetrofitClient.instance.getHousehold(
                selectedHouseholdId.value!!,
                jwtBearerHeaderValue
            )
            household.value = householdResponse;
            selectedHouseholdName.value = householdResponse.name

            val householdEventsResponse = RetrofitClient.instance.getHouseholdEvents(
                selectedHouseholdId.value!!,
                jwtBearerHeaderValue
            )
            if (householdEventsResponse.isSuccessful) {
                householdEvents.value = householdEventsResponse.body()
            }

        } catch (e: Exception) {
            isErrorActive.postValue(true)
            Log.e("MainActivity", "Exception occurred", e)
            errorMessage.postValue("Failed to fetch household with id $selectedHouseholdId")
        }
    }

    fun markEventsAsRead() = viewModelScope.launch {
        val readEventIds = householdEvents.value?.map { it.id }
        val householdId = selectedHouseholdId.value
        if (!readEventIds.isNullOrEmpty() && householdId != null) {
            val response = RetrofitClient.instance.markEventsAsRead(
                householdId,
                MarkAsReadRequest(readEventIds),
                jwtBearerHeaderValue
            )

            if (response.isSuccessful) {
                refreshHouseholdData()
            }
        }
    }

    fun createHousehold(name: String) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance.createHousehold(
                CreateHouseholdRequest(name),
                jwtBearerHeaderValue
            )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            selectedHouseholdId.value = response.body()
            refreshHouseholdData()

        } catch (e: Exception) {
            isErrorActive.postValue(true)
            errorMessage.postValue("Could not create household")
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
        try {
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
        } catch (e: Exception) {
            raiseError("Could not create task", e)
        }
    }

    fun updateTaskDueDate(taskId: Int, selectedDateMillis: Long) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(dueDate = selectedDateMillis.toZonedDateTime()))
    }

    fun updateTaskTitle(taskId: Int, taskCurrentTitle: String) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(name = taskCurrentTitle))
    }

    fun updateTaskDesc(taskId: Int, taskCurrentDesc: String) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(description = taskCurrentDesc))
    }

    fun updateTaskDoneStatus(taskId: Int, isDone: Boolean) = viewModelScope.launch {
        updateTask(taskId, UpdateTaskRequest(isDone = isDone))
    }

    fun deleteTask(taskId: Int) = viewModelScope.launch {
        try {
            val response = RetrofitClient.instance
                .deleteTask(
                    selectedHouseholdId.value!!,
                    taskId,
                    jwtBearerHeaderValue
                )

            if (!response.isSuccessful) {
                Log.d("HttpErrorBody", response.raw().body.toString())
                handleErrorResponse(response.errorBody(), response.code())
                return@launch
            }

            refreshHouseholdData();
        } catch (e: Exception) {
            raiseError("Could not delete task", e);
        }
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
        } catch (e: Exception) {
            raiseError("Could not invite member", e);
        }
    }

    fun deleteMember(memberId: Int) = viewModelScope.launch {
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
        } catch (e: Exception) {
            raiseError("Could not invite member", e);
        }
    }

    private fun updateTask(taskId: Int, request: UpdateTaskRequest) = viewModelScope.launch {
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

            refreshHouseholdData()

        } catch (e: Exception) {
            raiseError("Could not update task", e);
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

    fun updateSelectedHousehold(newHouseholdId: Int) {
        selectedHouseholdId.value = newHouseholdId
        refreshHouseholdData()
        saveHouseholdData(applicationContext, newHouseholdId)
    }

    fun clearErrorState() {
        isErrorActive.value = false
        errorMessage.value = ""
    }
}