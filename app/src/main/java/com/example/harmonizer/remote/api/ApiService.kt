import com.example.harmonizer.remote.api.models.requests.CreateHouseholdRequest
import com.example.harmonizer.remote.api.models.requests.CreateInvitationRequest
import com.example.harmonizer.remote.api.models.requests.CreateTaskRequest
import com.example.harmonizer.remote.api.models.requests.LoginRequest
import com.example.harmonizer.remote.api.models.requests.RegisterRequest
import com.example.harmonizer.remote.api.models.requests.UpdateTaskRequest
import com.example.harmonizer.remote.api.models.responses.HouseholdEventResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdInvitationResponse
import com.example.harmonizer.remote.api.models.responses.HouseholdResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Household Endpoints
    @POST("households")
    suspend fun createHousehold(@Body request: CreateHouseholdRequest): Int

    @GET("households/{householdId}")
    suspend fun getHousehold(
        @Path("householdId") householdId: Int,
        @Header("Authorization") jwtToken: String
    ): HouseholdResponse

    @GET("households/{householdId}/events")
    suspend fun getHouseholdEvents(
        @Path("householdId") householdId: Int,
        @Header("Authorization") jwtToken: String
    ): Response<List<HouseholdEventResponse>>

    @GET("households/me/owned")
    suspend fun getOwnedHouseholds(@Header("Authorization") jwtToken: String): List<HouseholdResponse>

    @GET("households/me")
    suspend fun getMyHouseholds(@Header("Authorization") jwtToken: String): List<HouseholdResponse>

    @POST("households/{householdId}/invite")
    suspend fun inviteMember(
        @Path("householdId") householdId: Int,
        @Body request: CreateInvitationRequest,
        @Header("Authorization") jwtToken: String
    ): Response<Int>

    @POST("households/{householdId}/tasks")
    suspend fun createTask(
        @Path("householdId") householdId: Int,
        @Body request: CreateTaskRequest,
        @Header("Authorization") jwtToken: String
    ): Response<Int>

    @DELETE("households/{householdId}/tasks/{taskId}")
    suspend fun deleteTask(
        @Path("householdId") householdId: Int,
        @Path("taskId") taskId: Int,
        @Header("Authorization") jwtToken: String
    ): Response<Boolean>

    @PATCH("households/{householdId}/tasks/{taskId}")
    suspend fun updateTask(
        @Path("householdId") householdId: Int,
        @Path("taskId") taskId: Int,
        @Body request: UpdateTaskRequest,
        @Header("Authorization") jwtToken: String
    ): Response<Unit>

    @DELETE("households/{householdId}/members/memberId")
    suspend fun deleteMember(
        @Path("householdId") householdId: Int,
        @Path("memberId") memberId: Int,
        @Header("Authorization") jwtToken: String
    ): Response<Boolean>


    // Invitation Endpoints
    @PATCH("invitations/{id}/accept")
    suspend fun acceptInvitation(
        @Path("id") invitationId: Int,
        @Header("Authorization") jwtToken: String
    ): Unit

    @GET("invitations/me")
    suspend fun getMyInvitations(@Header("Authorization") jwtToken: String): List<HouseholdInvitationResponse>


    // Authentication Endpoints
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): String

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Unit
}
