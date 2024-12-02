package rpt.tool.mementobibere.basic.appbasiclibs.utils

import org.apache.http.conn.ssl.SSLSocketFactory
import java.io.IOException
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SSLSocket_Helper(truststore: KeyStore?) : SSLSocketFactory(null) {
    private var sslFactory: javax.net.ssl.SSLSocketFactory =
        HttpsURLConnection.getDefaultSSLSocketFactory()

    init {
        try {
            val context = SSLContext.getInstance("TLS")


            // Create a trust manager that does not validate certificate chains and simply accept all type of certificates
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            }
            )


            // Initialize the socket factory
            context.init(null, trustAllCerts, SecureRandom())

            sslFactory = context.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(socket: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return sslFactory.createSocket(socket, host, port, autoClose)
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        return sslFactory.createSocket()
    }
}