package http.client

import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OkhttpApiClientTest {

    private val client = OkhttpApiClient()
    private lateinit var server: MockWebServer
    private lateinit var url: HttpUrl

    @BeforeEach
    fun beforeAll() {
        server = MockWebServer()
        server.start()
        url = server.url("/")
    }

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `Should support get http method`() {
        // Arrange
        server.enqueue(
            MockResponse().setResponseCode(200)
                .setBody("{\"hello\":\"world\",\"deep\": {\"nested1\":\"value1\", \"nested2\":\"value2\"}}"),
        )

        // Act
        val testObject = client.get(url.toUrl(), emptyMap(), null, TestObject::class.java)

        // Assert
        assertThat(testObject.hello).isEqualTo("world")
        assertThat(testObject.deep).isEqualTo(mapOf("nested1" to "value1", "nested2" to "value2"))
    }

    @Test
    fun `Should support post http method`() {
        // Arrange
        server.enqueue(
            MockResponse().setResponseCode(200).setBody("Ok"),
        )

        // Act
        val requestBody = mapOf("key" to "value")
        val requestHeaders = mapOf("header" to "something")
        val urlWithPath = url.newBuilder().addPathSegment("pathSegment").build()
        val responseBody = client.post(urlWithPath.toUrl(), requestBody, requestHeaders, String::class.java)

        // Assert
        assertThat(responseBody).isEqualTo("Ok")
        val recordedRequest = server.takeRequest()
        assertThat(recordedRequest.requestUrl).isEqualTo(urlWithPath)
        assertThat(recordedRequest.method).isEqualTo("POST")
        assertThat(recordedRequest.path).isEqualTo("/pathSegment")
        val bodyAsString = String(recordedRequest.body.inputStream().readAllBytes())
        assertThat(bodyAsString).isEqualTo(Gson().toJson(requestBody))
    }

    @Test
    fun `Should support delete http method`() {
        // Arrange
        server.enqueue(
            MockResponse().setResponseCode(200).setBody("Ok"),
        )
        val requestHeaders = mapOf("header" to "something")
        val urlWithPath = url.newBuilder().addPathSegment("resourceToDelete").build()

        // Act
        val responseBody = client.delete(urlWithPath.toUrl(), requestHeaders, String::class.java)

        // Assert
        assertThat(responseBody).isEqualTo("Ok")
        val recordedRequest = server.takeRequest()
        assertThat(recordedRequest.requestUrl).isEqualTo(urlWithPath)
        assertThat(recordedRequest.method).isEqualTo("DELETE")
        assertThat(recordedRequest.path).isEqualTo("/resourceToDelete")
    }

    data class TestObject(
        val hello: String,
        val deep: Map<String, String>,
    )
}
