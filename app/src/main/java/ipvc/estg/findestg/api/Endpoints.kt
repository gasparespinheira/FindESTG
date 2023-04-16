package ipvc.estg.findestg.api

import com.google.firebase.firestore.auth.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun registerUser(@Body requestBody: RequestBody): Call<ResponseBody>
}