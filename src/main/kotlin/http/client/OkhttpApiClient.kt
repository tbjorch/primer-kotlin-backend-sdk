package http.client

import com.google.gson.GsonBuilder
import http.deserializers.InstantTypeAdapter
import http.interceptor.LoggingInterceptor
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import primer.api.ApiClient
import java.net.URL
import java.time.Instant

class OkhttpApiClient : ApiClient {

    private val client = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()
    private val gson = GsonBuilder()
        .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
        .create()

    override fun <T> get(
        url: URL,
        headers: Map<String, String>,
        queryParams: Map<String, String>?,
        responseType: Class<T>,
    ): T {
        val urlBuilder = baseUrlBuilder(url)
        queryParams?.entries?.forEach { urlBuilder.addQueryParameter(it.key, it.value) }
        val request = baseRequestBuilder(urlBuilder.build(), headers).build()
        val response = client.newCall(request).execute()
        return gson.fromJson(response.body?.string(), responseType)
    }

    override fun <T, B> post(url: URL, body: B, headers: Map<String, String>, responseType: Class<T>): T {
        val url = baseUrlBuilder(url).build()
        val requestBuilder = baseRequestBuilder(url, headers)
        val requestBody = gson.toJson(body).toRequestBody("application/json".toMediaType())
        val request = requestBuilder.post(requestBody).build()
        val response = client.newCall(request).execute()
        return gson.fromJson(response.body?.string(), responseType)
    }

    override fun <T, B> patch(url: URL, body: B, headers: Map<String, String>, responseType: Class<T>): T {
        TODO("Not yet implemented")
    }

    override fun <T> delete(url: URL, headers: Map<String, String>, responseType: Class<T>): T {
        val urlBuilder = baseUrlBuilder(url)
        val request = baseRequestBuilder(urlBuilder.build(), headers).delete().build()
        val response = client.newCall(request).execute()
        return gson.fromJson(response.body?.string(), responseType)
    }

    private fun baseRequestBuilder(
        url: HttpUrl,
        headers: Map<String, String>,
    ) = Request.Builder()
        .url(url)
        .headers(headers.toHeaders())
        .addHeader("accept", "application/json")

    private fun baseUrlBuilder(url: URL) = HttpUrl.Builder()
        .scheme(url.protocol)
        .port(url.port)
        .host(url.host)
        .addPathSegments(url.path.substring(1))
}
