package com.kpopnara.kpn.models

import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.models.ObtainTokenRequest
import com.squareup.square.models.ObtainTokenResponse
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.apache.http.client.utils.URLEncodedUtils
import java.io.IOException
import java.util.*

object OAuthHandler {
    // Options are Environment.SANDBOX, Environment.PRODUCTION
    private val ENVIRONMENT = Environment.SANDBOX

    // Your application's ID and secret, available from the OAuth tab in the
    // Developer Dashboard
    // If you are testing the OAuth flow in the sandbox, use your sandbox
    // application
    // ID and secret. If you are testing in production, use the production
    // application ID and secret.
    private const val APPLICATION_ID = "sandbox-sq0idb-cT_j-f5QFWf-jjG0dW9POA"
    private const val APPLICATION_SECRET = "sandbox-sq0csb-2Fm62JPnkVN63Xea4C71FSiqlVFbhMQtbKtUP8A0RMk"

    private const val SQUARE_ACCESS_TOKEN_ENV_VAR = "EAAAEPLRBeVy9JYymCS-NJi9sd35V-7MC8WBDu4x7fxS3wn856_qLcnlMkPy9H3d"

    // Modify this list as needed
    private val SCOPES = arrayOf("INVENTORY_READ", "ITEMS_READ")

    // Serves the authorize link
    internal class AuthorizeHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            val connect_host: String
            // For testing in sandbox, the base url is
            // "https://connect.squareupsandbox.com",
            // and https://connect.squareup.com for production mode
            connect_host = if (ENVIRONMENT == Environment.SANDBOX) {
                "https://connect.squareupsandbox.com"
            } else {
                "https://connect.squareup.com"
            }

            // Reject non-GET requests
            if (t.requestMethod != "GET") {
                t.sendResponseHeaders(405, 0)
                t.responseBody.close()
            }
            val authorizeURL = String.format(
                "<a href=\"%s/oauth2/authorize?client_id=%s&scope=%s\">Click here</a> ",
                connect_host,
                APPLICATION_ID,
                java.lang.String.join("+", *SCOPES)
            ) + "to authorize the application."
            println(authorizeURL)
            val out = authorizeURL.toByteArray(charset("UTF-8"))
            t.sendResponseHeaders(200, out.size.toLong())
            t.responseBody.write(out)
            t.responseBody.close()
        }
    }

    // Serves requests from Square to your application's redirect URL
    // Note that you need to set your application's Redirect URL to
    // http://localhost:8000/callback from your application dashboard
    internal class CallbackHandler : HttpHandler {
        @Throws(IOException::class)
        override fun handle(t: HttpExchange) {
            println("Request received")
            if (t.requestMethod != "GET") {
                t.sendResponseHeaders(405, 0)
                t.responseBody.close()
            }

            // Extract the returned authorization code from the URL
            val requestUri = t.requestURI
            val queryParameters = URLEncodedUtils.parse(requestUri, "UTF-8")
            var authorizationCode: String? = null
            for (param in queryParameters) {
                println(param.name)
                println(param.value)
                if (param.name == "code") {
                    authorizationCode = param.value
                    break
                }
            }
            if (authorizationCode == null) {

                // The request to the Redirect URL did not include an authorization code.
                // Something went wrong.
                println("Authorization failed!")
                t.sendResponseHeaders(200, 0)
                t.responseBody.close()
                return
            }
            val client = SquareClient.Builder()
                .environment(ENVIRONMENT)
                .accessToken(SQUARE_ACCESS_TOKEN_ENV_VAR)
                .build()
            val bodyScopes: MutableList<String> = LinkedList()
            for (scope in SCOPES) {
                bodyScopes.add(scope)
            }

//            println(authorizationCode)

            // Create obtain token request body
            val body = ObtainTokenRequest.Builder(
                APPLICATION_ID,
                APPLICATION_SECRET,
                "authorization_code"
            )
                .code(authorizationCode)
                .scopes(bodyScopes)
                .build()
            val oAuthApi = client.oAuthApi

            // Call obtain token API and print the results on success
            // In production, you should never write tokens to the page.
            // You should encrypt the tokens and handle them securely.
//            val response = oAuthApi.obtainToken(body)
//            if (response != null) {
//                println(response.accessToken);
//            } else {
//                println("ERROR")
//            }
            oAuthApi.obtainTokenAsync(body).thenAccept { result: ObtainTokenResponse? ->
                if (result != null) {
                    println("Access token: " + result.accessToken)
                    println("Refresh token: " + result.refreshToken)
                    println("Merchant id: " + result.merchantId)
                    println("Authorization succeeded!")
                    try {
                        t.sendResponseHeaders(200, 0)
                        t.responseBody.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }.exceptionally { exception: Throwable? ->
                println(exception.toString())
                try {
                    t.sendResponseHeaders(405, 0)
                    t.responseBody.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                null
            }
        }
    } // Start up the server, listening on port 8000
}