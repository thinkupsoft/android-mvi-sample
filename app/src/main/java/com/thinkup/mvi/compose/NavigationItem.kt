package com.thinkup.mvi.compose

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.thinkup.common.GsonUtil
import com.thinkup.models.app.Movie

enum class Flows(
    val route: String,
    val navArgs: List<NavArg> = emptyList(),
    val deepLinks: List<DeepLink> = emptyList()
) {
    SPLASH("splash"),
    HOME("home"),
    LOGIN("login"),
    REGISTER("register"),
    EDIT_PROFILE("edit_profile"),
    DELETE_ACCOUNT("delete_account"),
    CHANGE_PASSWORD("change_password"),
    FORGOT_PASSWORD("forgot_password"),
    REDEEM_PASSWORD("redeem_password", navArgs = listOf(NavArg.Token), deepLinks = listOf(DeepLink.RedeemPassword1, DeepLink.RedeemPassword2)),
    NEW_MOVIE("new_movie"),
    MOVIE_DETAIL("movie_detail/{movie}", navArgs = listOf(NavArg.MovieItem)),
}

sealed class NavCommand(
    internal val flow: Flows,
    private val subRoute: String = "home",
    private val navArgs: List<NavArg> = emptyList(),
    private val navLinks: List<DeepLink> = emptyList()
) {
    class ContentType(feature: Flows) : NavCommand(feature)

    val route = run {
        val argValues = navArgs.map { "{${it.key}}" }
        listOf(flow.route)
            .plus(subRoute)
            .plus(argValues)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    val deepLinks = navLinks.map {
        NavDeepLink(it.uriPattern)
    }
}

enum class NavArg(val key: String, val navType: NavType<*>) {
    Token("token", NavType.StringType),
    MovieItem("movie", ParamType(Movie::class.java));

    fun getRouteKey() = "{${this.key}}"
}

enum class DeepLink(val uriPattern: String) {
    RedeemPassword1("https://stg.thinkupsoft.com/forgot-password?token={token}"),
    RedeemPassword2("https://www.stg.thinkupsoft.com/forgot-password?toekn={token}")
}

class ParamType<T : Parcelable>(private val clazz: Class<T>) : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): T {
        return GsonUtil.fromJson(value, clazz)
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}