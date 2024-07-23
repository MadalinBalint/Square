package com.mendelin.square.repositories_screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.twotone.Link
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.SwitchAccount
import androidx.compose.material.icons.twotone.Topic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.mendelin.square.R
import com.mendelin.square.ui.theme.SquareTheme
import com.mendelin.square.util.Constants

@Composable
fun OwnerAvatarImage(imageUrl: String, size: Dp, onClick: (() -> Unit)? = null) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .build(),
        contentDescription = stringResource(R.string.avatar_description),
        contentScale = ContentScale.Fit,
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.error),
        modifier = Modifier
            .size(size)
            .padding(8.dp)
            .clickable { onClick?.invoke() },
    )
}

@Composable
fun OwnerName(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.TwoTone.Person,
            contentDescription = stringResource(R.string.person_description),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.testTag(Constants.OWNER_NAME_TAG),
            text = name,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
fun RepositoryName(name: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.TwoTone.Topic,
            contentDescription = stringResource(R.string.topic_description),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.testTag(Constants.REPOSITORY_NAME_TAG),
            text = name,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}

@Composable
fun RepositoryTitle(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.TwoTone.SwitchAccount,
            contentDescription = stringResource(R.string.topic_description)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.testTag(Constants.REPOSITORY_TITLE_TAG),
            text = title,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun RepositoryDescription(description: String) {
    Column {
        Text(
            text = stringResource(R.string.description),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            modifier = Modifier.testTag(Constants.REPOSITORY_DESCRIPTION_TAG),
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 5,
        )
    }
}

@Composable
fun RepositoryUrl(url: String?) {
    if (!url.isNullOrEmpty() || url?.startsWith("http") == true) {
        Column {
            Text(
                text = stringResource(R.string.repository_url),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.TwoTone.Link,
                    contentDescription = stringResource(R.string.link_description)
                )
                Spacer(modifier = Modifier.width(8.dp))
                ClickableUrlContent(url = url, testTag = Constants.REPOSITORY_URL_TAG)
            }
        }
    }
}

@Composable
fun Language(language: String) {
    Column {
        Text(
            text = stringResource(R.string.programming_language),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.testTag(Constants.LANGUAGE_TAG),
            text = language, style = MaterialTheme.typography.bodyLarge,
        )
    }

}

@Composable
fun LicenseType(licenseType: String) {
    Column {
        Text(
            text = stringResource(R.string.license_type),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.testTag(Constants.LICENSE_TYPE_TAG),
            text = licenseType,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun LicenseUrl(licenseUrl: String?) {
    if (!licenseUrl.isNullOrEmpty() || licenseUrl?.startsWith("http") == true) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = stringResource(R.string.link_description)
            )
            Spacer(modifier = Modifier.width(8.dp))
            ClickableUrlContent(url = licenseUrl, testTag = Constants.LICENSE_URL_TAG)
        }
    }
}

@Composable
fun Topics(topics: String) {
    Column {
        Text(
            text = stringResource(R.string.topics),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier.testTag(Constants.TOPICS_TAG),
            text = topics,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun ClickableUrlContent(url: String, testTag: String) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
            append(url)
        }
        pushStringAnnotation(tag = "URL", annotation = url)
        pop()
    }

    ClickableText(
        modifier = Modifier.testTag(testTag),
        text = annotatedText,
        onClick = {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
        },
        style = MaterialTheme.typography.bodyLarge
    )
}

/** Previews of the screen items */
@Preview(showBackground = true)
@Composable
fun OwnerAvatarImagePreview() {
    SquareTheme {
        OwnerAvatarImage(
            imageUrl = "https://avatars.githubusercontent.com/u/7304399?v=4",
            size = 128.dp,
            onClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerNamePreview() {
    SquareTheme {
        OwnerName(name = "Steve Jobs")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryNamePreview() {
    SquareTheme {
        RepositoryName(name = "macOS")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryTitlePreview() {
    SquareTheme {
        RepositoryTitle(title = "macOS/Steve Jobs")
    }
}

@Preview(showBackground = true)
@Composable
fun RepositoryDescriptionPreview() {
    SquareTheme {
        RepositoryDescription(
            description = "macOS (previously known as OS X) is the operating system developed by Apple Inc. for its Mac line of personal computers and workstations. The abbreviation \"macOS\" stands for \"Macintosh Operating System. It was first introduced in 2001 as the successor to the classic Mac OS. Since then, it has undergone many updates and improvements to become the sophisticated and user-friendly operating system it is today.",
        )
    }
}


@Preview(showBackground = true)
@Composable
fun RepositoryUrlPreview() {
    SquareTheme {
        RepositoryUrl(
            url = "https://github.com/JetBrains",
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LanguagePreview() {
    SquareTheme {
        Language(language = "Kotlin")
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseTypePreview() {
    SquareTheme {
        LicenseType(licenseType = "MIT License")
    }
}

@Preview(showBackground = true)
@Composable
fun LicenseUrlPreview() {
    SquareTheme {
        LicenseUrl(licenseUrl = "https://api.github.com/licenses/mit")
    }
}

@Preview(showBackground = true)
@Composable
fun TopicsPreview() {
    SquareTheme {
        Topics(topics = "kotlin, kotlin-android")
    }
}
