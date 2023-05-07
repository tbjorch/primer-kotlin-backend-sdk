package primer.api

import http.client.OkhttpApiClient
import primer.Primer
import java.net.URL

object ApiResource {

    val client: ApiClient = OkhttpApiClient()
    inline fun <reified T> get(
        path: String,
        id: String? = null,
        params: Map<String, String>? = null,
    ): T {
        var fullPath = id?.let { "$path/$id" } ?: path
        var resourceUrl = URL(Primer.baseUrl, fullPath)
        return client.get(
            url = resourceUrl,
            headers = mapOf(
                PrimerHeader.X_API_KEY.value to Primer.apiKey,
                PrimerHeader.X_API_VERSION.value to Primer.apiVersion,
            ),
            queryParams = params,
            responseType = T::class.java,
        )
    }

    inline fun <reified T, B> post(
        path: String,
        idempotencyKey: String? = null,
        body: B,
    ): T {
        var resourceUrl = URL(Primer.baseUrl, path)
        val headers = mutableMapOf(
            PrimerHeader.X_API_KEY.value to Primer.apiKey,
            PrimerHeader.X_API_VERSION.value to Primer.apiVersion,
        )
        idempotencyKey?.let { headers.plus(PrimerHeader.IDEMPOTENCY_KEY.value to idempotencyKey) }
        return client.post(
            url = resourceUrl,
            body = body,
            headers = headers,
            responseType = T::class.java,
        )
    }

    inline fun <reified T, B> patch(
        path: String,
        idempotencyKey: String?,
        body: B,
    ): T {
        var resourceUrl = URL(Primer.baseUrl, path)
        val headers = mutableMapOf(
            PrimerHeader.X_API_KEY.value to Primer.apiKey,
            PrimerHeader.X_API_VERSION.value to Primer.apiVersion,
        )
        idempotencyKey?.let { headers.plus(PrimerHeader.IDEMPOTENCY_KEY.value to idempotencyKey) }
        return client.patch(
            url = resourceUrl,
            body = body,
            headers = headers,
            responseType = T::class.java,
        )
    }

    inline fun <reified T> delete(path: String): T {
        var resourceUrl = URL(Primer.baseUrl, path)
        val headers = mutableMapOf(
            PrimerHeader.X_API_KEY.value to Primer.apiKey,
            PrimerHeader.X_API_VERSION.value to Primer.apiVersion,
        )
        return client.delete(
            url = resourceUrl,
            headers = headers,
            responseType = T::class.java,
        )
    }

    enum class PrimerHeader(val value: String) {
        X_API_KEY("X-API-KEY"),
        IDEMPOTENCY_KEY("X-IDEMPOTENCY-KEY"),
        X_API_VERSION("X-API-VERSION"),
    }
}
