package com.peco2282.transfer

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peco2282.transfer.data.HistoryEntity
import com.peco2282.transfer.ui.theme.TransferTheme
import com.peco2282.transfer.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
  private val viewModel: HistoryViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val sharedText = if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
      intent.getStringExtra(Intent.EXTRA_TEXT)
    } else {
      null
    }

    setContent {
      TransferTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          MainScreen(
            sharedText = sharedText,
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

fun extractUrlsAndroid(text: String): List<String> {
  val urls = mutableListOf<String>()
  val matcher = Patterns.WEB_URL.matcher(text)

  while (matcher.find()) {
    urls.add(matcher.group())
  }
  return urls
}

@Composable
fun MainScreen(
  sharedText: String?,
  viewModel: HistoryViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var status by remember { mutableStateOf(if (sharedText != null) "Shortening..." else "Ready") }
  var resultUrl by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(sharedText != null) }

  val historyList by viewModel.allHistory.collectAsState(initial = emptyList())

  if (sharedText != null) {
    val url = extractUrlsAndroid(sharedText).firstOrNull()

    LaunchedEffect(url) {
      if (!isValidUrl(url)) {
        status = "Invalid URL"
        isLoading = false
        return@LaunchedEffect
      }
      val result = shortenUrl(url)
      if (result != null) {
        resultUrl = result
        status = "Short URL created!"
        viewModel.insert(
          HistoryEntity(
            originalUrl = sharedText,
            shortenedUrl = result
          )
        )
      } else {
        status = "Failed to create short URL"
      }
      isLoading = false
    }
  }

  Column(modifier = modifier.fillMaxSize()) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = status, style = MaterialTheme.typography.headlineSmall)
        if (isLoading) {
          CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
        resultUrl?.let { url ->
          Text(
            text = url,
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
          )

          Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = {
              val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = android.content.ClipData.newPlainText("Shortened URL", url)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
                .show()
            }) {
              Text("Copy")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
              val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, null)
              context.startActivity(shareIntent)
            }) {
              Text("Share")
            }
          }
        }
      }
    }

    HorizontalDivider()

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1.5f)
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "History",
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.weight(1f)
        )
        if (historyList.isNotEmpty()) {
          IconButton(onClick = { viewModel.deleteAll() }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete all")
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LazyColumn {
        items(historyList) { history ->
          HistoryItem(history)
          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
      }
    }
  }
}

@Composable
fun HistoryItem(history: HistoryEntity) {
  val context = LocalContext.current
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        val clipboard =
          context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip =
          android.content.ClipData.newPlainText("Shortened URL", history.shortenedUrl)
        clipboard.setPrimaryClip(clip)
        Toast
          .makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
          .show()
      }
      .padding(vertical = 4.dp)
  ) {
    Text(
      text = history.shortenedUrl,
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.primary
    )
    Text(
      text = history.originalUrl,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

fun extractUrl(body: String): String? {
    // Expected body: "Short URL created!\nShort: https://example.com/api/s/xxxxxx"
    val prefix = "Short: "
    val index = body.indexOf(prefix)
    return if (index != -1) {
        body.substring(index + prefix.length).trim()
  } else {
    null
  }
}

@OptIn(ExperimentalContracts::class)
fun isValidUrl(url: String?): Boolean {
  contract {
    returns(true) implies (url != null)
  }
  @Suppress("HttpUrlsUsage")
  return url != null && (url.startsWith("http://") || url.startsWith("https://")) && url.length > 7
}

suspend fun shortenUrl(longUrl: String): String? = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val url = BuildConfig.SHORTENER_BASE_URL.toHttpUrlOrNull()
        ?.newBuilder()
        ?.addQueryParameter("url", longUrl)
        ?.build() ?: return@withContext null

  val request = Request.Builder()
    .url(url)
    .post("".toRequestBody())
    .build()

  try {
    client.newCall(request).execute().use { response ->
      if (!response.isSuccessful) return@withContext null
      val body = response.body?.string() ?: return@withContext null
      extractUrl(body)
    }
  } catch (_: Exception) {
    null
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  TransferTheme {
    Greeting("Android")
  }
}
