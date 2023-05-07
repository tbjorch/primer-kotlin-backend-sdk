package http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.logging.Level
import java.util.logging.Logger

class LoggingInterceptor : Interceptor {

    private val logger = Logger.getLogger("OkhttpApiClient")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            logUnsuccessfulCalls(response)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.Builder()
                .request(request)
                .code(999)
                .message(e.message ?: "unknown")
                .body("{$e}".toResponseBody(null))
                .build()
        }
    }

    private fun logUnsuccessfulCalls(response: Response) {
        if (response.code > 399) {
            val message = response.message
            val body = response.body?.string()!!
            val logMessage = "statusCode: ${response.code}, message: $message, body:$body"
            logger.log(Level.WARNING, logMessage)
        }
    }
}
