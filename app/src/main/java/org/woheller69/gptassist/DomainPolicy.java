// SPDX-License-Identifier: GPL-3.0-or-later
package org.woheller69.gptassist;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

/** URL policy only: not a firewall for WebView or DownloadManager redirects. */
final class DomainPolicy {
    private static final String[] ALLOWED_DOMAINS = {
            "cdn.auth0.com",
            "auth.openai.com",
            "chatgpt.com",
            "openai.com",
            "fileserviceuploadsperm.blob.core.windows.net",
            "cdn.oaistatic.com",
            "oaiusercontent.com"
    };

    private DomainPolicy() {}

    static boolean isAllowedNavigationUrl(String url) {
        return "about:blank".equals(url) || isAllowedHttpsUrl(url);
    }

    static boolean isAllowedHttpsUrl(String url) {
        URI uri = parseHttpsUrl(url);
        if (uri == null) return false;

        String host = uri.getHost().toLowerCase(Locale.ROOT);
        for (String domain : ALLOWED_DOMAINS) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the initial download URL only, not subsequent redirects.
     * Unrestricted mode skips only the domain allowlist, not URL validation.
     * about:blank and non-HTTPS schemes are never download targets.
     */
    static boolean isAllowedDownloadUrl(String url, boolean restricted) {
        if (restricted) return isAllowedHttpsUrl(url);
        return parseHttpsUrl(url) != null;
    }

    /**
     * Fail closed for ambiguous authorities, credentials and nonstandard ports.
     * No repair of malformed URLs: validate exactly the supplied URL.
     * A trailing DNS dot is intentionally rejected rather than normalized away.
     */
    static URI parseHttpsUrl(String url) {
        if (url == null) return null;

        try {
            URI uri = new URI(url);
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || uri.isOpaque()
                    || uri.getHost() == null
                    || uri.getRawUserInfo() != null) {
                return null;
            }

            String host = uri.getHost();
            if (host.isEmpty() || host.endsWith(".")) return null;

            String authority = uri.getRawAuthority();

            // Require the authority spelling to match host or host:443,
            // ignoring case but without percent-decoding.
            if (!host.equalsIgnoreCase(authority)
                    && !(host + ":443").equalsIgnoreCase(authority)) {
                return null;
            }

            return uri;
        } catch (URISyntaxException exception) {
            return null;
        }
    }
}