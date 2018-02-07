/*
 * Clover - 4chan browser https://github.com/Floens/Clover/
 * Copyright (C) 2014  Floens
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.floens.chan.core.site.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import org.floens.chan.core.model.Post;
import org.floens.chan.core.model.json.site.SiteConfig;
import org.floens.chan.core.model.orm.Board;
import org.floens.chan.core.model.orm.Loadable;
import org.floens.chan.core.settings.json.JsonSettings;
import org.floens.chan.core.site.Site;
import org.floens.chan.core.site.SiteActions;
import org.floens.chan.core.site.SiteAuthentication;
import org.floens.chan.core.site.SiteBase;
import org.floens.chan.core.site.SiteEndpoints;
import org.floens.chan.core.site.SiteIcon;
import org.floens.chan.core.site.SiteRequestModifier;
import org.floens.chan.core.site.SiteUrlHandler;
import org.floens.chan.core.site.http.DeleteRequest;
import org.floens.chan.core.site.http.HttpCall;
import org.floens.chan.core.site.http.LoginRequest;
import org.floens.chan.core.site.http.Reply;
import org.floens.chan.core.site.http.ReplyResponse;
import org.floens.chan.core.site.parser.ChanReader;
import org.floens.chan.core.site.parser.PostParser;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CommonSite extends SiteBase {
    private final Random secureRandom = new SecureRandom();

    private String name;
    private SiteIcon icon;
    private BoardsType boardsType;
    private CommonConfig config;
    private CommonSiteUrlHandler resolvable;
    private CommonEndpoints endpoints;
    private CommonActions actions;
    private CommonApi api;
    private CommonParser parser;
    private CommonRequestModifier requestModifier;

    @Override
    public void initialize(int id, SiteConfig config, JsonSettings userSettings) {
        super.initialize(id, config, userSettings);
        setup();

        if (name == null) {
            throw new NullPointerException("setName not called");
        }

        if (icon == null) {
            throw new NullPointerException("setIcon not called");
        }

        if (boardsType == null) {
            throw new NullPointerException("setBoardsType not called");
        }

        if (this.config == null) {
            throw new NullPointerException("setConfig not called");
        }

        if (resolvable == null) {
            throw new NullPointerException("setResolvable not called");
        }

        if (endpoints == null) {
            throw new NullPointerException("setEndpoints not called");
        }

        if (actions == null) {
            throw new NullPointerException("setActions not called");
        }

        if (api == null) {
            throw new NullPointerException("setApi not called");
        }

        if (parser == null) {
            throw new NullPointerException("setParser not called");
        }

        if (requestModifier == null) {
            // No-op implementation.
            requestModifier = new CommonRequestModifier() {
            };
        }
    }

    public abstract void setup();

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(SiteIcon icon) {
        this.icon = icon;
    }

    public void setBoardsType(BoardsType boardsType) {
        this.boardsType = boardsType;
    }

    public void setConfig(CommonConfig config) {
        this.config = config;
    }

    public void setResolvable(CommonSiteUrlHandler resolvable) {
        this.resolvable = resolvable;
    }

    public void setEndpoints(CommonEndpoints endpoints) {
        this.endpoints = endpoints;
    }

    public void setActions(CommonActions actions) {
        this.actions = actions;
    }

    public void setApi(CommonApi api) {
        this.api = api;
    }

    public void setParser(CommonParser parser) {
        this.parser = parser;
    }

    public void setRequestModifier(CommonRequestModifier requestModifier) {
        this.requestModifier = requestModifier;
    }

    /*
     * Site implementation:
     */

    @Override
    public String name() {
        return name;
    }

    @Override
    public SiteIcon icon() {
        return icon;
    }

    @Override
    public BoardsType boardsType() {
        return boardsType;
    }

    @Override
    public SiteUrlHandler resolvable() {
        return resolvable;
    }

    @Override
    public boolean feature(Feature feature) {
        return config.feature(feature);
    }

    @Override
    public boolean boardFeature(BoardFeature boardFeature, Board board) {
        return config.boardFeature(boardFeature, board);
    }

    @Override
    public SiteEndpoints endpoints() {
        return endpoints;
    }

    @Override
    public SiteActions actions() {
        return actions;
    }

    @Override
    public SiteRequestModifier requestModifier() {
        return requestModifier;
    }

    @Override
    public ChanReader chanReader() {
        return api;
    }

    public abstract class CommonConfig {
        public boolean feature(Feature feature) {
            return false;
        }

        public boolean boardFeature(BoardFeature boardFeature, Board board) {
            return false;
        }
    }

    public static abstract class CommonSiteUrlHandler implements SiteUrlHandler {
        @Override
        public boolean matchesName(String value) {
            return false;
        }

        @Override
        public boolean respondsTo(HttpUrl url) {
            return false;
        }

        @Override
        public String desktopUrl(Loadable loadable, @Nullable Post post) {
            return null;
        }

        @Override
        public Loadable resolveLoadable(Site site, HttpUrl url) {
            return null;
        }
    }

    public abstract class CommonEndpoints implements SiteEndpoints {
        @NonNull
        public SimpleHttpUrl from(String url) {
            return new SimpleHttpUrl(url);
        }

        @Override
        public HttpUrl catalog(Board board) {
            return null;
        }

        @Override
        public HttpUrl thread(Board board, Loadable loadable) {
            return null;
        }

        @Override
        public HttpUrl imageUrl(Post.Builder post, Map<String, String> arg) {
            return null;
        }

        @Override
        public HttpUrl thumbnailUrl(Post.Builder post, boolean spoiler, Map<String, String> arg) {
            return null;
        }

        @Override
        public HttpUrl icon(Post.Builder post, String icon, Map<String, String> arg) {
            return null;
        }

        @Override
        public HttpUrl boards() {
            return null;
        }

        @Override
        public HttpUrl reply(Loadable thread) {
            return null;
        }

        @Override
        public HttpUrl delete(Post post) {
            return null;
        }

        @Override
        public HttpUrl report(Post post) {
            return null;
        }

        @Override
        public HttpUrl login() {
            return null;
        }
    }

    public class SimpleHttpUrl {
        @NonNull
        public HttpUrl.Builder url;

        public SimpleHttpUrl(String from) {
            HttpUrl res = HttpUrl.parse(from);
            if (res == null) {
                throw new NullPointerException();
            }
            url = res.newBuilder();
        }

        public SimpleHttpUrl(@NonNull HttpUrl.Builder from) {
            url = from;
        }

        public SimpleHttpUrl builder() {
            return new SimpleHttpUrl(url.build().newBuilder());
        }

        public SimpleHttpUrl s(String segment) {
            url.addPathSegment(segment);
            return this;
        }

        public HttpUrl url() {
            return url.build();
        }
    }

    public abstract class CommonActions implements SiteActions {
        public void setupPost(Reply reply, MultipartHttpCall call) {
        }

        public void handlePost(ReplyResponse response, Response httpResponse, String responseBody) {
        }

        @Override
        public void boards(BoardsListener boardsListener) {
        }

        @Override
        public void post(Reply reply, PostListener postListener) {
            ReplyResponse replyResponse = new ReplyResponse();

            reply.password = Long.toHexString(secureRandom.nextLong());
            replyResponse.password = reply.password;

            MultipartHttpCall call = new MultipartHttpCall(CommonSite.this) {
                @Override
                public void process(Response response, String result) throws IOException {
                    handlePost(replyResponse, response, result);
                }
            };

            setupPost(reply, call);

            httpCallManager.makeHttpCall(call, new HttpCall.HttpCallback<HttpCall>() {
                @Override
                public void onHttpSuccess(HttpCall httpCall) {
                    postListener.onPostComplete(httpCall, replyResponse);
                }

                @Override
                public void onHttpFail(HttpCall httpCall, Exception e) {
                    postListener.onPostError(httpCall);
                }
            });
        }

        @Override
        public boolean postRequiresAuthentication() {
            return false;
        }

        @Override
        public SiteAuthentication postAuthenticate() {
            return SiteAuthentication.fromNone();
        }

        @Override
        public void delete(DeleteRequest deleteRequest, DeleteListener deleteListener) {

        }

        @Override
        public void login(LoginRequest loginRequest, LoginListener loginListener) {

        }

        @Override
        public void logout() {

        }

        @Override
        public boolean isLoggedIn() {
            return false;
        }

        @Override
        public LoginRequest getLoginDetails() {
            return null;
        }
    }

    public abstract class CommonApi implements ChanReader {
        @Override
        public PostParser getParser() {
            return parser;
        }
    }

    public abstract class CommonParser implements PostParser {
    }

    public abstract class CommonRequestModifier implements SiteRequestModifier {
        @Override
        public void modifyHttpCall(HttpCall httpCall, Request.Builder requestBuilder) {
        }

        @Override
        public void modifyWebView(WebView webView) {
        }
    }
}
